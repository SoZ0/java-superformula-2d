import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.awt.event.*;
/**
 * Main
 */
public class Main extends JPanel implements ActionListener {
    
    private JFrame frame;
    private JSlider aSlider, bSlider, mSlider, n1Slider, n2Slider, n3Slider;

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
        setLayout(new BorderLayout());
        aSlider = createSlider("a");
        // bSlider = new JSlider();
        // mSlider = new JSlider();
        // n1Slider = new JSlider();
        // n2Slider = new JSlider();
        // n3Slider = new JSlider();

         add(aSlider, "North");
        // add(bSlider); 
        // add(mSlider);
        // add(n1Slider);
        // add(n2Slider);
        // add(n3Slider);


        createAndShowGui();
    }

    private JSlider createSlider(String name){
        JLabel label = new JLabel(name);
        label.setForeground(Color.white);
        JSlider slider = new JSlider();
        slider.setOpaque(false);
        slider.setBackground(Color.black);
        slider.add(label, "NORTH");
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

    private long lastTime = System.nanoTime();

    @Override
    public void paintComponent(Graphics page){

        Graphics2D graphics2d = (Graphics2D) page;
        long time = System.currentTimeMillis();
        graphics2d.translate(APPLET_HEIGHT/2, APPLET_WIDTH/2);

        startShape();
        graphics2d.setColor(Color.black);
        graphics2d.fillRect(-APPLET_HEIGHT/2, -APPLET_WIDTH/2, APPLET_WIDTH, APPLET_HEIGHT);
        ArrayList<double[]> points = new ArrayList<>();
        for (float theta = 0; theta < 2*Math.PI; theta += 0.1) {
            double rad = r(theta,
            mousePosition().getX() / 100,
            mousePosition().getY() / 100,
            2d,
            4d,
            6d,
            8d);
            points.add(polarToCartesianCord(rad, theta, SCALE));
        }
        graphics2d.setColor(Color.WHITE);
        new Thread(){
            public void run() {
                for (int i = 0; i < points.size(); i++) {
                    double[] cartesianXY = points.get(i);
                    Point2D start = new Point2D.Double(cartesianXY[0], cartesianXY[1]);
                    if(i == points.size()-1) cartesianXY = points.get(0);
                    else cartesianXY = points.get(i + 1);
                    Point2D end = new Point2D.Double(cartesianXY[0], cartesianXY[1]);
                    graphics2d.draw(new Line2D.Double(start, end));
                }
            };
        }.run();
        double timeBetweenFrams = System.nanoTime()- lastTime;
        lastTime = System.nanoTime();
        graphics2d.setFont(new Font("TimesRoman", Font.BOLD, 10)); 
        graphics2d.drawString("MilliPerFrame "+timeBetweenFrams, 0, 0);
    }

    public double r(double theta,double a, double b, double m, double n1, double n2, double n3){
        return StrictMath.pow(StrictMath.pow(StrictMath.abs(StrictMath.cos(m * theta / 4d) / a), n2) + StrictMath.pow(StrictMath.abs(StrictMath.sin(m * theta / 4d) / b), n3), -1d/n1);
    }

    public void startShape(){

    }

    public void endShape(){
        
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

    private Point lastPosition = new Point(0,0);
    public Point mousePosition(){
        try{
            double x = getMousePosition().getX();
            lastPosition.setLocation(x, lastPosition.getY());
        }catch(NullPointerException ex){}

        try{
            double y = getMousePosition().getY();
            lastPosition.setLocation(lastPosition.getX(), y);
        }catch(NullPointerException ex){}
       return lastPosition;
    }
}