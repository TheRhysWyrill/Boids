import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

public class Main {

	static Vector<Boid> boidList;
	public static int MIN_COORD = 0;
	public static int RUN_LENGTH = 500;
	public static int BOIDS_COUNT = 100;
	public static int MAX_COORD = 40;
	public static int ExpNo;
	
	public static void main(String[] args) {

		Random rnd = new Random();
		rnd.setSeed(System.currentTimeMillis());

		boidList = new Vector<Boid>();

		int count = 0;

		for (int i = MIN_COORD; i < BOIDS_COUNT; i++) {

			boidList.add(new Boid(boidList.size(), rnd, BOIDS_COUNT));

		}

		while (count < RUN_LENGTH) {
			Iterator itr = boidList.iterator();
			 if(count == 1) {
				System.out.println("Boid Count: " + BOIDS_COUNT);
				Boid.printWeighting();
				System.out.println("Time Step: " + count);
				Draw();
			} else if(count == 50) {
				System.out.println("Time Step: " + count);
				Draw();
			} else if(count == 100) {
				System.out.println("Time Step: " + count);
				Draw();
			} else if(count == 150) {
				System.out.println("Time Step: " + count);
				Draw();
			} else if(count == 200) {
				System.out.println("Time Step: " + count);
				Draw();
			} else if(count == 250) {
				System.out.println("Time Step: " + count);
				Draw();
			} else if(count == 300) {
				System.out.println("Time Step: " + count);
				Draw();
			} else if(count == 350) {
				System.out.println("Time Step: " + count);
				Draw();
			} else if(count == 400) {
				System.out.println("Time Step: " + count);
				Draw();
			} else if(count == 450) {
				System.out.println("Time Step: " + count);
				Draw();
			}

			while (itr.hasNext()) {

				Boid aBoid = (Boid) itr.next();
				aBoid.Update(boidList,count);
			}
			count++;
			
		}
		System.out.println("Time Step: " + count);
		Draw();
	}

	public static void Draw() {

		boolean[][] Grid = new boolean[MAX_COORD][MAX_COORD];
		int tempX, tempY;
		
		for (int i = MIN_COORD; i < MAX_COORD; i++)
			for (int j = MIN_COORD; j < MAX_COORD; j++)
				Grid[i][j] = false;

		Iterator itr = boidList.iterator();
		while (itr.hasNext()) {

			Boid aBoid = (Boid) itr.next();
			tempX = (int) aBoid.getPos().x;
			tempY = MAX_COORD - 1 - (int) aBoid.getPos().y;
			if((tempX < MIN_COORD) || (tempX > MAX_COORD))
				System.out.println("X out of bounds " + tempX);
			else if((tempY < MIN_COORD) || tempY > MAX_COORD)
				System.out.println("Y out of bounds " + tempY);
			Grid[tempX][tempY] = true;
		}
				
		for(int i = MIN_COORD; i < MAX_COORD * 2+ 4; i++)
			System.out.print("=");
		System.out.println("");
				
		for(int j = MIN_COORD; j < MAX_COORD; j++) {
			System.out.print("||");
			for(int i = MIN_COORD; i < MAX_COORD; i++) {
				if(Grid[i][j])
					System.out.print("~*");
			 else 
				System.out.print("  ");
			}
			
			System.out.println("||");
			
		}
		
		for(int i = MIN_COORD; i < MAX_COORD * 2 + 4; i++)
			System.out.print("=");
		System.out.println("");
				
	}

	public static void printOutput() {
		
		
	}
}
