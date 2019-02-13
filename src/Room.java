import java.util.ArrayList;

public abstract class Room {



	protected String name;
	protected ArrayList<Room> connections;
	protected ArrayList<Player> players;	
	
	public Room(String name, ArrayList<Room> connections) {
		this.name = name;
		this.connections = connections;
	}

	public String getName() {
		return this.name;
	}
	
	public ArrayList<Player> getPlayers() {
		return this.players;
	}
	
	public ArrayList<Room> getConnections() {
		return this.connections;
	}
	
	
	public boolean vacate(Player p, Room r) {
		if (this.isAdjacent(r))
			return players.remove(p);
		
		return false;
		
	}
	
	public boolean isAdjacent(Room r) {
		//return connections.contains(r);
		return true;
	}
	
	public boolean enter(Player p) {
		return players.add(p);
	}
	
	public void newDay() {
		//TODO
	}

	public void getUpgradeCosts() {
		return;
	}

	public int getNewRank(String payType, int amount) {
		return -1;
	}
	
	public boolean isComplete() {
		return false;
	}
	
	protected void wrap() {
		return;
	}
	
	public boolean take(int roll) {
		return true;
	}
	
	public Scene getScene() {
		return null;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public ArrayList<Role> getRoles() {
		return null;
	}
	
	public Role commit(Player p, Role r) {
		return null;
	}
	

}
