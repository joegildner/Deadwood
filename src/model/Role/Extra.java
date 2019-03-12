package model.Role;
public class Extra extends Role {

	public Extra(String name, String line, int rank, int[] area) {
		super(name, line, rank, area);
		this.reward = new int[] {1,1};
		this.pay = 1;
	}
}
