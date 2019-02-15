package Board.Role;
public class Lead extends Role {
	
	public Lead(String name, String line, int rank) {
		super(name, line, rank);
		this.reward = new int[] {0,2};
		this.pay = 0;
	}

}
