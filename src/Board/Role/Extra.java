package Board.Role;
public class Extra extends Role {

	public Extra(String name, String line, int rank) {
		super(name, line, rank);
		this.reward = new int[] {1,1};
		this.pay = 1;
	}
}
