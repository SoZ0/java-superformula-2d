import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.awt.event.*;
/**
 * Main
 */
public class Main extends JPanel implements ActionListener {
    
    private JFrame frame;
    private JSlider resolutionSlider, aSlider, bSlider, mSlider, n1Slider, n2Slider, n3Slider;

    private final int APPLET_WIDTH = 500;
    private final int APPLET_HEIGHT = 500;
    private final int FREQ = 0;
    private final int SCALE = 50;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Main()::init);
    }

    public void actionPerformed (ActionEvent evt)
    {
       repaint();
    }

    public void init(){
        Timer timer;
        timer = new Timer(FREQ, this);
        timer.start();  
        aSlider = createSlider("A", 100, 1);
        bSlider = createSlider("B", 100, 1);
        mSlider = createSlider("M", 100, 1);
        n1Slider = createSlider("N1", 100, 1);
        n2Slider = createSlider("N2", 100, 1);
        n3Slider = createSlider("N3", 100, 1);
        resolutionSlider = createSlider("Resolution", 20, 1);
        setLayout(new FlowLayout());
        add(aSlider);
        add(n1Slider);
        add(bSlider); 
        add(n2Slider);
        add(mSlider);
        add(n3Slider);
        add(resolutionSlider);


        createAndShowGui();
    }

    private JSlider createSlider(String name, int max, int min){
        JLabel label = new JLabel(name);
        label.setForeground(Color.white);
        label.setFont(new Font("TimesRoman", Font.BOLD, 10));
        label.setBorder(new EmptyBorder(new Insets(5, 10, 0, 10)));
        JSlider slider = new JSlider();
        slider.setMaximum(max);
        slider.setMinimum(min);
        slider.setValue(1);
        slider.setLayout(new FlowLayout());
        slider.add(label);
        slider.setOpaque(false);
        slider.setBorder(new EmptyBorder(new Insets(5, 10, 10, 10)));

        return slider;
    }

    private void createAndShowGui(){
        frame = new JFrame(getClass().getSimpleName());
        this.setOpaque(true);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.black);
        frame.setResizable(false);
    }

    @Override
    public void paintComponent(Graphics page){

        Graphics2D graphics2d = (Graphics2D) page;

        graphics2d.setColor(Color.black);
        graphics2d.fillRect(0, 0, APPLET_WIDTH, APPLET_HEIGHT);
        ArrayList<double[]> points = new ArrayList<>();
        for (float theta = 0; theta < 2*Math.PI; theta += resolutionSlider.getValue() / 10d) {
            double rad = r(theta,
            aSlider.getValue()/10d,
            bSlider.getValue()/10d,
            mSlider.getValue()/10d,
            n1Slider.getValue()/10d,
            n2Slider.getValue()/10d,
            n3Slider.getValue()/10d);
            points.add(polarToCartesianCord(rad, theta, SCALE));
        }
        graphics2d.setColor(Color.WHITE);
        new Thread(){
            public void run() {
                for (int i = 0; i < points.size(); i++) {
                    double[] cartesianXY = points.get(i);
                    Point2D start = new Point2D.Double(cartesianXY[0]+APPLET_WIDTH/2, cartesianXY[1]+APPLET_HEIGHT/2);
                    if(i == points.size()-1) cartesianXY = points.get(0);
                    else cartesianXY = points.get(i + 1);
                    Point2D end = new Point2D.Double(cartesianXY[0]+APPLET_WIDTH/2, cartesianXY[1]+APPLET_HEIGHT/2);
                    graphics2d.draw(new Line2D.Double(start, end));
                }
            };
        }.run();
    }

    public double r(double theta,double a, double b, double m, double n1, double n2, double n3){
        return StrictMath.pow(StrictMath.pow(StrictMath.abs(StrictMath.cos(m * theta / 4d) / a), n2) + StrictMath.pow(StrictMath.abs(StrictMath.sin(m * theta / 4d) / b), n3), -1d/n1);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(APPLET_WIDTH, APPLET_HEIGHT);
    }

    public double[] polarToCartesianCord(double[] polarXY, float scale){
        double cartesianX = polarXY[0] * StrictMath.cos(polarXY[1]) * scale;
        double cartesianY = polarXY[0] * StrictMath.sin(polarXY[1]) * scale;
        return new double[]{cartesianX, cartesianY};
    }

    public double[] polarToCartesianCord(double polarX, double polarY,  float scale){
        return polarToCartesianCord(new double[]{polarX, polarY}, scale);
    }
}