package Board.Room;
import java.util.ArrayList;

import Board.Player;
import Board.Scene;
import Board.Role.Role;

public class Stage extends Room {
	
	private ArrayList<Role> extras; 
	private int takes;
	private Scene scene;
	
	public Stage(String name) {
		super(name);
		this.takes = -1;
		this.scene = null;
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
		
		for (Role r : this.getRoles()) {
			if (role.equalsIgnoreCase(r.getName())) {
				chosen = r;
				break;
			}
		}
		
		if (chosen != null) {
			return (chosen.fill(p)) ? chosen : null;
		}
		return null;
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

}
