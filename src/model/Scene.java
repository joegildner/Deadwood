package model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import model.Role.Lead;
import model.Role.Role;

public class Scene {
	
	private ArrayList<Role> leads;
	private String name;
	private String desc;
	private String imgFile;
	private int budget;
	private int number;
	
	public Scene(String name, String desc, int budget, int number, String imgFile) {
		this.leads = new ArrayList<>();
		this.name = name;
		this.desc = desc;
		this.budget = budget;
		this.number = number;
		this.imgFile = imgFile;
	}
	
	public void cashout() {
		Die d = new Die(6);
		int[] roll = d.roll(this.budget);

		Arrays.sort(roll);

		Collections.sort(leads, Collections.reverseOrder());
		int curRole = 0;
		
		for (int i = roll.length - 1; i > -1; i--) {
			if (curRole > leads.size()-1) {
				curRole = 0;
			}
			Player p = leads.get(curRole).getPlayer();
			if (p != null) {
				System.out.println(p + " earns $" + roll[i]);
				p.addEarnings(roll[i]);
			}
			curRole++;
		}
	}
	
	public void addRole(Lead l) {
		leads.add(l);
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
		return this.name + " scene " + this.number;
	}

	public String getImgFile() {
		return imgFile;
	}
}
