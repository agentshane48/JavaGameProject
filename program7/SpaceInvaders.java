
package program7;

//Part 2
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.*;

public class SpaceInvaders extends JFrame 
{
		
	//Sets the font for the display
	Font f = new Font("Serif", Font.BOLD, 18);
	//Global variables
	public spacePanel sp;
	public int x_pic = 0;
	public int y_pic = 400;
	public int x_offset;
	public int y_offset;
	public int[] randomX=new int[10];
	public int[] randomY=new int[10];
	public boolean cannon_touched = false; 
	public int initialize=0;
	public int started=0;
	public int delete[]=new int[10];
	public int ball;
	public int ballx;
	public int bally;
	public int balli=0;
	public int time=0;
	public int fired=0;
	public int shotsfired=0;
	int deleted=0;
	boolean win = false;
	boolean lose = false;
	boolean reset = false;
	public boolean timerbool = false;
	public java.util.Timer timer;
	public boolean rerun = false;
	JLabel displaytext = new JLabel("Press Down to Start Game");
	public Timer clock;
	public int seconds = 0;
	public int minutes = 0;
	public int hours = 0;
	private DecimalFormat decimalformat = new DecimalFormat("00");
	//Global image declarations
	public Image cannon = Toolkit.getDefaultToolkit().getImage("program7/cannon1.png");
	public Image background=Toolkit.getDefaultToolkit().getImage("program7/bg2.png");
	public Image target=Toolkit.getDefaultToolkit().getImage("program7/target.png");
	public Image cannonball=Toolkit.getDefaultToolkit().getImage("program7/cannonBall.png");
	Image[] targets =new Image[10];
	Image cannonballs;
	//Global Score and Label Declarations
	public int Score=0;
	JLabel scoretext = new JLabel("Score: " + Score);
	JLabel loselabel= new JLabel("YOU LOST!! Press down key to try again");
	JLabel winlabel = new JLabel("YOU WIN!!   Press down key to try again");

	//Class constructor
	public SpaceInvaders(boolean reset) 
	{
		//Sets display for the Frame/Panel
		super("SpaceInvaders");
		sp= new spacePanel(reset);
		sp.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.add(sp,BorderLayout.WEST);
		this.setSize(640, 480);
		this.setVisible(true);
	}

	public static void main(String[] args) 
	{ 
		boolean reset=false;
		new SpaceInvaders(reset);
	}
	
	//Class for the spacePanel so that paintComponent can be used later on
	public class spacePanel extends JPanel implements ActionListener
	{
		public spacePanel(boolean reset)
		{
			//Sets the panel for Paintcomponent display
			super();
			setPreferredSize(new Dimension(640,480));
			this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			this.addMouseMotionListener(new MouseHandler());
			this.addMouseListener(new MouseClickedHandler());
			this.addKeyListener(new KeyHandler());
			this.setFocusable(true);
			
			//Adds the text to the display
			scoretext.setFont(scoretext.getFont().deriveFont(14.0f));
			add(scoretext);
			scoretext.setLocation(540+40,320);
			scoretext.setFont(scoretext.getFont().deriveFont(14.0f));
			//scoretext.setPreferredSize(new Dimension(100,100));

			//Prepares the clock
			clock = new Timer(10, this);
			if (reset==true)
			{
				clock.start();
				timer = new java.util.Timer();
				TimerFired tf = new TimerFired();
				timer.scheduleAtFixedRate( tf, 0, 10 );
			}
		}	
		//Actionperformed
		public void actionPerformed(ActionEvent e)
		{

			if (e.getSource() == clock)
			{
				seconds++;
			}

			if (seconds == 60)
			{
				minutes++;
				seconds = 0;
			}

			if (minutes == 60)
			{
				hours++;
				minutes = 0;
				seconds = 0;
			}

			if (hours == 24)
			{
				hours = 0;
				minutes = 0;
				seconds = 0;
			}
			
			displaytext.setText(decimalformat.format(hours) + ":" + 
							decimalformat.format(minutes) + ":" + 
							decimalformat.format(seconds));
		}
		
		//Location of paintComponent method
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			//Draws the items to the panel, which in turn draws to the screen
			g.drawImage(background,0,0,640,480,this);
			g.setFont(f);
			g.drawString("Time: " + displaytext.getText(), 100, 40);
			g.drawString("Shots:  " + shotsfired, 100, 55);

			//Control for assigning random numbers to the target point
			if (rerun==false)
			{
				for (int i=0; i<10;i++)
				{
					Random randomGenerator = new Random();
					randomX[i]=displayrandom(40,590,randomGenerator);
					randomY[i]=displayrandom(50,150,randomGenerator);
				}
			}
			
			rerun=true;

			if (fired==1)
			{
				//Controls the cannonball display
				cannonballs=cannonball;
				g.drawImage(cannonballs,ballx,bally=bally--,10,10,this);
				bally=bally-2;
				if (bally<0)
				{
					shotsfired++;
					fired=0;
				}
			}
			for (int i =0; i < 10; ++i)
			{
				if((ballx>=randomX[i]-10)&&(ballx<=randomX[i]+30)&&(bally<=randomY[i]+30)&&(bally>=randomY[i]-10)&&(delete[i]==0))
				{
					shotsfired++;
					fired=0;
					delete[i]=1;
					deleted++;
					Scorekeeper();

				}
				if (randomY[i]>=400){
					g.drawString("You lost Press down key to try again",180, 20);
					Glorifiedloser();
				}
				if (delete[i]==0)
				{
					targets[i] = target;
					g.drawImage(targets[i],randomX[i],randomY[i],30,30,this);
				}
			}

			g.drawImage(cannon, x_pic, y_pic, this);

		}
		

		private void Scorekeeper() 
		{
			Score++;
			scoretext.setText("Score: " + Score);
			if (Score==10)
			{
				clock.stop();
				Smugwinner();
			}

		}
		public void Smugwinner()
		{
			add(winlabel);
			remove(scoretext);
			win=true;
		}
		public void Glorifiedloser()
		{
			clock.stop();
			remove(scoretext);
			add(loselabel);
			lose=true;	
		}
	}

	//Mouseclickedhandler
	class MouseClickedHandler extends MouseAdapter
	{
		public void mousePressed(MouseEvent me)
		{
			if ( (me.getX() > x_pic) && (me.getX() < x_pic + 100) && (me.getY() > y_pic) && (me.getY() < y_pic + 100))
			{
				cannon_touched = true;
				x_offset = me.getX() - x_pic;
				y_offset = me.getY() - y_pic;
			}
			else
				cannon_touched = false;
		}
	}
	//Mousehandler
	class MouseHandler extends MouseMotionAdapter
	{

		public void mouseDragged(MouseEvent e)
		{
			if ((cannon_touched))
			{
				x_pic = e.getX() - x_offset;
				if(x_pic>580)
					x_pic=580;
				else if(x_pic<=0)
					x_pic=0;
			}
			sp.repaint();
		}
	}
	private static int displayrandom(int first, int last, Random rand)
	{
		//This is where the random numbers are generated for the targets to be displayed
		if (first > last) 
			throw new IllegalArgumentException("");
		long range = (long)last - (long)first + 1;
		long fraction = (long)(range * rand.nextDouble());
		int randomNumber = (int)(fraction + first);
		return randomNumber;
	}

	public class TimerFired extends TimerTask
	{
		
		int dummy=0;
		
		public void run()
		{
			dummy++;
			for (int i = 0; i < 10; ++i)
			{
				if ((randomY[i] >= 30)&&(dummy%10 == 0))
					randomY[i]++;
			}
			
			repaint();
		}
	}
	
	//Keyhandler
	class KeyHandler implements KeyListener
	{
		public void keyPressed(KeyEvent e) 
		{

			if (e.getKeyCode() == KeyEvent.VK_DOWN)
			{

				clock.start();
				timer = new java.util.Timer();
				TimerFired tf = new TimerFired();
				timer.scheduleAtFixedRate( tf, 0, 10 );
				sp.repaint();
				if (win==true || lose==true)
					reset();
			}
			else if (e.getKeyCode() == KeyEvent.VK_UP)
			{

				if (fired == 0)
				{
					ballx = x_pic+15;
					bally = 400;
					fired = 1;
				}
				sp.repaint();
			}
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			{
				x_pic = x_pic+5;
				if(x_pic > 580)
					x_pic = 580;
				sp.repaint();
			}
			else if (e.getKeyCode() == KeyEvent.VK_LEFT)
			{
				x_pic = x_pic-5;
				if(x_pic <= 0)
					x_pic = 0;
				sp.repaint();
			}
		}
		//method stubs
		public void keyReleased(KeyEvent e) 
		{

		}
		public void keyTyped(KeyEvent e) 
		{

		}

		//reset method
		public void reset()
		{
			reset=true;
			new SpaceInvaders(reset);
		}
	}
}