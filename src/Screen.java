import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Screen extends JPanel implements MouseMotionListener{
	
	private static final long serialVersionUID = -616365487435413161L;
	static int W = 1800, H = 900;
	static JFrame frame;
	int x,y;
	boolean frame1 = true;
	ParticleCollection world = new ParticleCollection(1000);
	int count = 0;
	public Screen() {
		setSize(new Dimension(W,H));
		setPreferredSize(new Dimension(W,H));
		addMouseMotionListener((MouseMotionListener) this);
		setFocusable(true);
	}
	public static void main(String[] args) {	
		Screen screen = new Screen();	
		frame = new JFrame("My Drawing");
		frame.add(screen);
		frame.pack();
    	frame.setVisible(true);
	};
	
	public void paint(Graphics g) {
		if (frame1) {
			world.generateParticles(400, 500);
			world.startCalculating(60, W, H);
			frame1 = false;
		}
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, W, H);
		//double t = 0;
		//while (t<1.0/60.0) {
		//	double step = world.calculateSmalestTimestep();
		//	if (step>(1.0/60.0)-t) {
		//		step = (1.0/60.0)-t;
		//	};
		//	world.update(new Vector(0,9.8), step, W, H);
		//	t+=step;
		//} world.render(g);
		world.renderRecorded(g, count);
		count += 1;
		repaint();
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		
		
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		x =  e.getXOnScreen();
		y = e.getYOnScreen();
	}
	
}
