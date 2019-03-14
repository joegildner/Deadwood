package model.Room;
import java.util.ArrayList;

import model.Player;
import model.Scene;
import model.Role.Role;

public class Stage extends Room {
	
	private ArrayList<Role> extras; 
	private int takes;
	private ArrayList<Take> takeArea;
	private Scene scene;
	
	public Stage(String name) {
		super(name);
		this.takes = -1;
		this.scene = null;
		this.takeArea = new ArrayList<>();
		this.extras = new ArrayList<>();
	}

	public Stage(String name, ArrayList<Room> connections, Scene scene, int takes, ArrayList<Role> extras) {
		super(name, connections);
		this.takes = takes;
		this.scene = scene;
		this.extras = extras;
	}
	
	@Override
	public boolean isComplete() {
		return (this.takes == 0);
	}
	
	@Override
	protected void wrap() {
		this.scene.cashout();
		for (Role r : extras) {
			Player p = r.getPlayer();
			if (p != null) {
				p.addEarnings(r.getRank());
			}
		}
	}
	
	@Override
	public boolean take(int roll) {
		if (roll >= this.scene.getBudget()) {
			this.takes--;
			if (this.takes == 0) {
				System.out.println("Scene Wrapped!");
				wrap();
			}
			return true;
		}
		
		return false;
	}
	@Override
	public void addTake(Take t) {
		takeArea.add(t);
	}

	@Override
	public void addRole(Role r) {
		extras.add(r);
	}
	
	@Override
	public Scene getScene() {
		return this.scene;
	}
	
	@Override
	public Scene newScene(Scene scene) {
		Scene temp = this.scene;
		this.scene = scene;
		return temp;
	}
	
	@Override
	public void setTakes(int t) {
		this.takes = t;
	}

	@Override
	public ArrayList<Role> getRoles() {
		ArrayList<Role> allRoles = new ArrayList<>();
		allRoles.addAll(this.extras);
		allRoles.addAll(scene.getLeads());
		return allRoles;
	}
	
	@Override
	public Role commit(Player p, String role) {
		Role chosen = null;
		boolean found = false;

		if (!isComplete()) {
			int i = 0;
			ArrayList<Role> roles = this.getRoles();
			while (i < roles.size() && !found) {
				Role r = roles.get(i);
				if (role.equalsIgnoreCase(r.getName())) {
					chosen = r;
					found = true;
				}
				i++;
			}

			if (found && !chosen.fill(p)) {
				chosen = null;
			}
		}
		return chosen;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.getName());
		if (this.isComplete()) {
			sb.append(" wrapped");
		} else {
			sb.append(" shooting ");
			sb.append(this.scene);
		}
		return sb.toString();
		
	}

	public ArrayList<Take> getTakeArea() {
		return takeArea;
	}

	public int getTakes() {
		return takes;
	}
}
