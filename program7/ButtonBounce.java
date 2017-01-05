package program7;

// Part 1

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ButtonBounce extends JFrame
{

	//Global variables
    public int delay = 10;
    protected Timer timer;
    public int x_val = 0;
    public int y_val = 0;
    public int w_val = 10;
    public int h_val = 10;
    public int dx = 2;
    public int dy = 2;


    //Constructor
    ButtonBounce()
    {

        super("Button Bounce");
        setVisible(true);
        setSize(600,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel outer = new JPanel();
        JPanel east = new JPanel();
        JPanel west = new JPanel(new GridLayout(4,1,10,50));
        JPanel south = new JPanel();

        JButton start = new JButton("Start");
        start.setPreferredSize(new Dimension(75,25));
        start.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
               timer.start();
            }
        });

        JButton stop = new JButton("Stop");
        stop.setPreferredSize(new Dimension(75,25));
        stop.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                timer.stop();
            }
        });
        //button panel
        JPanel button = new JPanel(new FlowLayout(FlowLayout.CENTER));
        button.add(start);
        button.add(stop);

        //panel for the x scrollbar
        JPanel xpanel = new JPanel(new GridLayout(2,1,1,1));
        JScrollBar xInc = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 49);
        JLabel xInclabel = new JLabel("X increment: " + String.valueOf(xInc.getValue()));
        xInc.addAdjustmentListener(new AdjustmentListener() 
        {
            public void adjustmentValueChanged(AdjustmentEvent e) 
            {
                xInclabel.setText("X increment: " + e.getValue());
                x_val = e.getValue();
            }
        });
        //adds to the x panel
        xpanel.add(xInc);
        xpanel.add(xInclabel);

        //panel for the y scrollbar
        JPanel ypanel = new JPanel(new GridLayout(2,1,1,1));
        JScrollBar yInc = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 49);
        JLabel yInclabel = new JLabel("Y increment: " + String.valueOf(yInc.getValue()));
        yInc.addAdjustmentListener(new AdjustmentListener()
        {
            public void adjustmentValueChanged(AdjustmentEvent e) 
            {
                yInclabel.setText("Y increment: " + e.getValue());
                y_val = e.getValue();
            }
        });
        //adds to the y panel
        ypanel.add(yInc);
        ypanel.add(yInclabel);
        
        //panel for the width scrollbar
        JPanel wpanel = new JPanel(new GridLayout(2,1,1,1));
        JScrollBar w = new JScrollBar(JScrollBar.HORIZONTAL, 5, 1, 1, 99);
        JLabel wlabel = new JLabel("Width: " + String.valueOf(w.getValue()));
        w.addAdjustmentListener(new AdjustmentListener() 
        {
            public void adjustmentValueChanged(AdjustmentEvent e)
            {
                wlabel.setText("Width: " + e.getValue());
                w_val = e.getValue();
            }
        });

        wpanel.add(w);
        wpanel.add(wlabel);
        
        //panel for height scrollbar
        JPanel hpanel = new JPanel(new GridLayout(2,1,1,1));
        JScrollBar height = new JScrollBar(JScrollBar.HORIZONTAL, 5, 1, 1, 99);
        JLabel heightlabel = new JLabel("Height: " + String.valueOf(height.getValue()));
        height.addAdjustmentListener(new AdjustmentListener() 
        {
            public void adjustmentValueChanged(AdjustmentEvent e) 
            {
                heightlabel.setText("Height: " + e.getValue());
                h_val = e.getValue();
            }
        });

        //adds to the height panel
        hpanel.add(height);
        hpanel.add(heightlabel);
        
        //BallPanel class for the paintcomponent
        class BallPanel extends JPanel implements ActionListener {

        	//class constructor
            public BallPanel() 
            {
                setPreferredSize(new Dimension(475,400));
                timer = new Timer(delay, this);
                timer.start();
            }

            public void actionPerformed(ActionEvent e)
            {
                repaint();
            }


            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                g.setColor(Color.WHITE);
                this.setBackground(Color.WHITE);
                g.setColor(Color.blue);

                //Determines what to do with the values based on the boundaries
                if (x_val < w_val)
                    dx = Math.abs(dx);

                if (x_val > getWidth() - w_val)
                    dx = -Math.abs(dx);

                if (y_val < h_val)
                    dy = Math.abs(dy);

                if (y_val > getHeight() - h_val)
                    dy = -Math.abs(dy);

                //increments the ball
                x_val += dx;
                y_val += dy;
                
                //Draws ball
                g.fillOval(x_val, y_val, w_val, h_val);
            }

        }
        //sets the display 
        BallPanel bp = new BallPanel();

        //Adds ballpanel to the east panel
        east.add(bp);
        
        //adds the 4 variable panels to the west panel
        west.add(xpanel);
        west.add(ypanel);
        west.add(wpanel);
        west.add(hpanel);
        
        //adds button panel to the southpanel
        south.add(button);
        
        //sets the layout for outer and adds the directional panels for primary display
        outer.setLayout(new BorderLayout());
        outer.add(east, BorderLayout.WEST);
        outer.add(west, BorderLayout.EAST);
        outer.add(south, BorderLayout.SOUTH);
        //Adds outer to the frame for actual display
        this.add(outer);
        this.pack();
    }

    public static void main(String args[]) 
    {
        ButtonBounce bb = new ButtonBounce();
    }
}

