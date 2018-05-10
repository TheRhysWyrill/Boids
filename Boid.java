import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

public class Boid {

	private static double WEIGHT_COHESION = 0.95;
	private static double WEIGHT_ALIGNMENT = 0.95;
	private static double WEIGHT_SEPARATION = 0.05;
	private static double MINIMUM_SEPARATION = 1.0;
	private static double GROUP_DISTANCE = 25.0;
	private static double MIN_SEP_SQR = MINIMUM_SEPARATION * MINIMUM_SEPARATION;
	private static double MIN_X = Main.MIN_COORD;
	private static double MIN_Y = Main.MIN_COORD;
	private static double MAX_X = Main.MAX_COORD;
	private static double MAX_Y = Main.MAX_COORD;

	private Vector2D pos;
	private Vector2D vel;

	private Vector2D oldpos;
	private Vector2D oldvel;

	private int Index;

	private int[] Trust;

	public Boid(int newIndex, Random rnd, int groupSize) {

		Trust = new int[groupSize];

		Index = newIndex;
		
		for (int i = 0; i < groupSize; i++) {

			// The top left of the 'grid'.
			if (Index < (groupSize / 2) && i < (groupSize / 2)) {
				Trust[i] = 9;
			} 
			// The top right of the 'grid'.
			else if (Index < (groupSize / 2) && i >= (groupSize / 2)) {
				Trust[i] = 9;
			} 
			// The bottom left of the 'grid'.
			else if (Index >= (groupSize / 2) && i < (groupSize / 2)) {
				Trust[i] = 0;
			}
			// The bottom right of the 'grid'.
			else if (Index >= (groupSize / 2) && i >= (groupSize / 2)) {
				Trust[i] = 0;
			}
		}

		// For loop 0 < groupSize, Trust = rand
/*		for (int i = 0; i < groupSize; i++) {
			Trust[i] = rnd.nextInt(10);
			System.out.print(Trust[i]+", ");
		}
		System.out.println();*/

		setPos(new Vector2D(rnd.nextDouble() * MAX_X, rnd.nextDouble() * MAX_Y));
		setVel(new Vector2D((rnd.nextDouble() * 2.0) - 1.0, (rnd.nextDouble() * 2.0) - 1.0));
		vel.normalize();

//		System.out.println("Boid " + Index + " [" + pos.x + ", " + pos.y + "] {" + vel.x + ", " + vel.y + "}");
	}

	public void Update(Vector<Boid> allBoids, int Step) {

		Vector2D centre, vel_target;
		Vector2D trustedcohesion, trustedalignment, separation, anticohesion, antialignment;
		int tooClose = 0;
		int trustedneighbors = 0;
		int distrustedneighbors = 0;

		centre = new Vector2D();
		centre.setZero();

		trustedcohesion = new Vector2D();
		trustedcohesion.setZero();

		anticohesion = new Vector2D();
		anticohesion.setZero();

		trustedalignment = new Vector2D();
		trustedalignment.setZero();

		antialignment = new Vector2D();
		antialignment.setZero();

		separation = new Vector2D();
		separation.setZero();

		vel_target = new Vector2D();

		Iterator itr = allBoids.iterator();

		while (itr.hasNext()) {

			Boid aBoid = (Boid) itr.next();
			if (this.getIndex() != aBoid.getIndex()) {
				if (this.getPos().distanceSq(aBoid.getPos()) < GROUP_DISTANCE) {
					if (Trust[aBoid.getIndex()] > 5) {
						trustedneighbors++;
						// Calculates the sum of positions to calculate the centre.
						trustedcohesion.add(aBoid.getPos());

						// Calculate the sum of velocities.
						trustedalignment.add(aBoid.getVel());

						if (this.getPos().distanceSq(aBoid.getPos()) < MIN_SEP_SQR) {
							separation.add(Vector2D.subtract(this.getPos(), aBoid.getPos()));
							tooClose++;
						}
					} else {
						distrustedneighbors++;
						// Calculates the sum of positions to calculate the centre.
						anticohesion.add(aBoid.getPos());

						// Calculate the sum of velocities.
						antialignment.add(aBoid.getVel());

						if (this.getPos().distanceSq(aBoid.getPos()) < MIN_SEP_SQR) {
							separation.add(Vector2D.subtract(this.getPos(), aBoid.getPos()));
							tooClose++;
						}
					}
				}
			}
		}

		if (trustedneighbors > 0) {
			// Calculate trusted cohesion vector as a fraction of vector to centre.
			trustedcohesion.divide(trustedneighbors);
			trustedcohesion.subtract(this.getPos());
			trustedcohesion.normalize();
			trustedcohesion.multiply(WEIGHT_COHESION);

			// Calculate trusted alignment vector
			trustedalignment.divide(trustedneighbors);
			trustedalignment.normalize();
			trustedalignment.multiply(WEIGHT_ALIGNMENT);

			vel_target.add(trustedcohesion);
			vel_target.add(trustedalignment);

		}

		if (distrustedneighbors > 0) {
			// Calculate distrusted cohesion vector.
			anticohesion.divide(distrustedneighbors);
			anticohesion.subtract(this.getPos());
			anticohesion.normalize();
			anticohesion.multiply(WEIGHT_COHESION);
			anticohesion.multiply(-1.0);

			// Calculate distrusted alignment vector.
			antialignment.divide(distrustedneighbors);
			antialignment.normalize();
			antialignment.multiply(WEIGHT_ALIGNMENT);
			antialignment.multiply(-1.0);

			vel_target.add(anticohesion);
			vel_target.add(antialignment);

		}

		// Calculate separation
		// Check to see if aBoid is to close to me.
		// Only calculate if there is someone too close.
		if (tooClose > 0) {
			separation.divide(tooClose);
			separation.normalize();
			separation.multiply(WEIGHT_SEPARATION);

			vel_target.add(separation);

		}

		vel_target.add(getVel());

		this.setVel(vel_target);

		vel.normalize();

		updatePos();

		/*
		 * if (Step==99) System.out.println("Boid " + Index + " [" + pos.x + ", " +
		 * pos.y + "] {" + vel.x + ", " + vel.y + "}");
		 */
	}

	public void updatePos() {

		setPos(Vector2D.add(pos, vel));

		bounceOffWall();

	}
	
	public static void printWeighting() {

		System.out.println("Cohesion Weighting: " + WEIGHT_COHESION);
		System.out.println("Alignment Weighting: " + WEIGHT_ALIGNMENT);
		System.out.println("Separation Weighting: " + WEIGHT_SEPARATION);
		
	}

	public void bounceOffWall() {

		if (pos.x < MIN_X) {
			pos.x = -pos.x;
			vel.x = -vel.x;
		}
		if (pos.x > MAX_X) {
			pos.x = (2.0 * MAX_X) - pos.x;
			vel.x = -vel.x;
		}
		if (pos.y < MIN_Y) {
			pos.y = -pos.y;
			vel.y = -vel.y;

		}
		if (pos.y > MAX_Y) {
			pos.y = (2.0 * MAX_Y) - pos.y;
			vel.y = -vel.y;
		}
	}

	public int getIndex() {
		return Index;
	}

	public Vector2D getPos() {
		return pos;
	}

	public void setPos(Vector2D newpos) {
		oldpos = pos;
		pos = newpos;
	}

	public Vector2D getVel() {
		return vel;
	}

	public void setVel(Vector2D newvel) {
		oldvel = vel;
		vel = newvel;
	}

	public Vector2D getOldpos() {
		return oldpos;
	}

	public Vector2D getOldvel() {
		return oldvel;
	}

}
