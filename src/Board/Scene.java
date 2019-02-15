package Board;
import java.util.ArrayList;
import java.util.Collections;

import Board.Role.Role;

public class Scene {
	
	private ArrayList<Role> leads;
	private String name;
	private String desc;
	private int budget;
	
	public Scene(String name, String desc, ArrayList<Role> roles, int budget) {
		this.leads = roles;
	}
	
	public void cashout() {
		Die d = new Die(6);
		int[] roll = d.roll(this.budget);
		Collections.sort(leads);
		int curRole = 0;
		
		for (int i = 0; i < roll.length; i++) {
			if (curRole == leads.size()+1) {
				curRole = 0;
			}
			Player p = leads.get(curRole).getPlayer();
			p.addEarnings(roll[i]);
			curRole++;
		}
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
