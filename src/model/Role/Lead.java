package model.Role;
public class Lead extends Role {
	
	public Lead(String name, String line, int rank, int[] area) {
		super(name, line, rank, area);
		this.reward = new int[] {0,2};
		this.pay = 0;
	}

}
