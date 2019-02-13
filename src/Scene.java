import java.util.ArrayList;

public class Scene {
	
	private ArrayList<Role> leads;
	private String name;
	private String desc;
	private int budget;
	
	public Scene(String name, String desc, ArrayList<Role> roles, int budget) {
		this.leads = roles;
	}
	
	public void cashout() {
		
	}
	
	public ArrayList<Role> getLeads() {
		return this.leads;
	}
	
	public int getBudget() {
		return this.budget;
	}
	
	public String getDesc() {
		return this.desc;
	}
	
	@Override
	public String toString() {
		return this.name;
	}

}
