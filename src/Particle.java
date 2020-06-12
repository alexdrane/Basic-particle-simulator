import java.awt.Color;
import java.awt.Graphics;

public class Particle {
	double mass, radius;
	Vector pos, velocity;	
	public Particle(Vector p, Vector v, double m, double r) {
		this.pos = p;
		this.velocity = v;
		this.mass = m;
		this.radius = r;
	}
	
	public double getSpeed() {
		return this.velocity.getLength();
	}
	
	public boolean getCollided(Particle p) {
		return p.pos.sub(this.pos).getLength()<this.radius+p.radius;
	}
	
	public void moveByTimestep(double t) {
		this.pos = this.pos.add(this.velocity.scaleComponents(t));
	}
	
	public void accelerate(Vector a, double t) {
		this.velocity = this.velocity.add(a.scaleComponents(t));
	}
	
	public int outOfBounds(int x, int y) {
		if (this.pos.x-this.radius<0 || this.pos.x+this.radius>x ){
			return 1;
		} else if (this.pos.y-this.radius<0 || this.pos.y+this.radius>y) {
			return 2;
		}return 0;
	}
	
	public void updateVelocity(Vector newVelocity) {
		this.velocity=newVelocity;
	}
	
	public void bounceIfOutOfBounds(int x, int y, double t) {
		int flag = this.outOfBounds(x, y);
		if (flag==1) {
			this.velocity.x = this.velocity.x*-0.95;
		} else if (flag == 2) {
			this.velocity.y = this.velocity.y*-0.85;
		}
		//this.moveByTimestep(t);
	}
	
	public void render(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillOval((int)(this.pos.x-this.radius), (int)(this.pos.y-this.radius), (int)this.radius*2, (int)this.radius*2);
	}
	
}
