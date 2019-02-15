package Board;
import java.util.Random;


public class Die {

	private int sides;
	private Random rand;
	
	public Die(int sides) {
		this.sides = sides;
		this.rand = new Random(System.currentTimeMillis());
		
	}
	
	public int[] roll(int n) {
		int[] result = new int[n];
		
		for (int i = 0; i < n; i++) {
			result[i] = this.rand.nextInt(sides) + 1;
		}
		
		return result;
	}
	
	public int roll() {
		return this.rand.nextInt(sides) + 1;
	}
}
