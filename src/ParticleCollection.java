import java.awt.Color;
import java.awt.Graphics;

public class ParticleCollection {
	int number_of_particles;
	Particle[] particles;
	int r = 4;
	 Vector[][] positions;
	public ParticleCollection(int n) {
		this.number_of_particles = n;
	}
	
	public void generateParticles(int W,int H) {
		particles = new Particle[this.number_of_particles];
		double px = 10*r;
		double py = 100;
		for (int i = 0;i<this.number_of_particles;i++) {
			particles[i] = new Particle(new Vector(px,py),new Vector((Math.random()-0.5)*1,(Math.random()-0.5)*1),1,r);    //2*r+Math.random()*(W-4*r),2*r+Math.random()*(H-4*r)
			px += 2*r+1;
			if (px>W-r) {
				px = 10*r;
				py+=2*r+1;
			}
		}//int r2 = 50;
		//particles[this.number_of_particles-1] = new Particle(new Vector(600,550),new Vector((Math.random()-0.5)*10,(Math.random()-0.5)*10),100,r2);
	}
	
	public void render(Graphics g) {
		for (int i = 0;i<this.number_of_particles;i++) {
			particles[i].render(g);
		}
	}
	
	public void startCalculating(double time,int W, int H) {
		this.positions = new Vector[(int) (time*60)][this.number_of_particles];
		for (int i = 0; i<time*60;i++) {
			double t = 0;
			while (t<1.0/60.0) {
				double step = this.calculateSmalestTimestep();
				if (step>(1.0/60.0)-t) {
					step = (1.0/60.0)-t;
				};
				this.update(new Vector(0,9.8), step, W, H);
				t+=step;
			}positions[i]=this.addPosition();
			System.out.println(100*i/(time*60));
		}
	}
	
	public Vector[] addPosition() {
		Vector[] posArr = new Vector[this.number_of_particles];
		for (int i = 0;i<this.number_of_particles;i++) {
			posArr[i] = particles[i].pos;
		}return posArr;
	}
	
	public void renderRecorded(Graphics g, int count) {
		Vector[] posArr = this.positions[count%this.positions.length];
		double radius;
		for (int i = 0;i<this.number_of_particles;i++) {
			radius = particles[i].radius;
			g.setColor(Color.BLUE);
			g.fillOval((int)(posArr[i].x-radius), (int)(posArr[i].y-radius), (int)radius*2, (int)radius*2);
		}
	}
	
	public void update(Vector a,double t,int W,int H) {
		for (int i = 0;i<this.number_of_particles;i++) {
			particles[i].accelerate(a,t);
			particles[i].bounceIfOutOfBounds(W, H,t);
			for (int j = 0;j<this.number_of_particles;j++) {
				if (particles[j].getCollided(particles[i])&&!(i==j)){
					Vector difference = particles[i].pos.sub(particles[j].pos);
					double angle = difference.getRadian();
					Vector transformedVelocityJ = particles[j].velocity.rotate_rad(-angle);
					Vector transformedVelocityI = particles[i].velocity.rotate_rad(-angle);
					double Mj = particles[j].mass;
					double Mi = particles[i].mass;
					double Ujx = transformedVelocityJ.x;
					double Uix = transformedVelocityI.x;
					double Vjx = Ujx*(Mj-Mi)/(Mj+Mi)+(2*Uix*Mi)/(Mj+Mi);
					double Vix = Uix*(Mi-Mj)/(Mj+Mi)+(2*Ujx*Mj)/(Mj+Mi);
					Vector transformedNewVelocityJ = new Vector(Vjx,transformedVelocityJ.y);
					Vector transformedNewVelocityI = new Vector(Vix,transformedVelocityI.y);
					particles[i].updateVelocity(transformedNewVelocityI.rotate_rad(angle));
					particles[j].updateVelocity(transformedNewVelocityJ.rotate_rad(angle));
					//particles[i].moveByTimestep(t);
					//particles[j].moveByTimestep(t);
				}
			}
			particles[i].moveByTimestep(t);
		}
	}
	
	public double calculateSmalestTimestep() {
		double greatestSpeed = 0;
		//double smallestRadius = 1000000;
		for (int i = 0;i<this.number_of_particles;i++) {
			if (particles[i].getSpeed()>greatestSpeed) {
				greatestSpeed = particles[i].getSpeed();
			}//if (particles[i].radius/2<smallestRadius) {
			//	smallestRadius = particles[i].getSpeed();
			//}
		}
		//System.out.println(greatestSpeed);
		//System.out.println(2/greatestSpeed);
		return (r/2)/greatestSpeed;
	}
}
