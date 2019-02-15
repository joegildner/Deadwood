package Board.Room;
import java.util.ArrayList;

import Board.Player;
import Board.Scene;
import Board.Role.Role;

public class Stage extends Room {
	
	private ArrayList<Role> extras; 
	private int takes;
	private Scene scene;

	public Stage(String name, ArrayList<Room> connections, Scene scene, int takes, ArrayList<Role> extras) {
		super(name, connections);
		this.takes = takes;
		this.scene = scene;
		this.extras = extras;
		this.players = new ArrayList<>();
	}
	
	@Override
	public boolean isComplete() {
		return (this.takes == 0);
	}
	
	@Override
	protected void wrap() {
		//TODO
		this.scene.cashout();
		for (Role r : extras) {
			Player p = r.getPlayer();
			p.addEarnings(r.getRank());
		}
	}
	
	@Override
	public boolean take(int roll) {
		if (roll >= this.scene.getBudget()) {
			this.takes--;
			if (this.takes == 0) {
				wrap();
			}
			return true;
		}
		
		return false;
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
	public ArrayList<Role> getRoles() {
		ArrayList<Role> allRoles = new ArrayList<>();
		allRoles.addAll(this.extras);
		allRoles.addAll(scene.getLeads());
		return allRoles;
	}
	
	@Override
	public Role commit(Player p, Role r) {
		if (this.getRoles().contains(r)) {
			return (r.fill(p)) ? r : null;
		}
		return null;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.getName());
		sb.append("shooting ");
		sb.append(this.scene);
		return sb.toString();
		
	}

}
