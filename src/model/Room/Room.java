package model.Room;
import java.util.ArrayList;
import java.util.Arrays;

import model.Player;
import model.Scene;
import model.Role.Role;

public abstract class Room {
	protected String name;
	protected ArrayList<Room> connections;
	protected ArrayList<Player> players;
	protected int[] area;

	
	public Room(String name) {
		this.name = name;
		this.connections = new ArrayList<>();
		this.players = new ArrayList<>();
	}
	
	public Room(String name, ArrayList<Room> connections) {
		this.name = name;
		this.connections = connections;
		this.players = new ArrayList<Player>();
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
	
	public void addConnection(Room r) {
		if (r != null)
			connections.add(r);
	}
	
	public void addRole(Role r) {
		return;
	}
	
	public void setTakes(int t) {
		return;
	}

	public void addTake(Take t) {
		return;
	}

	public void setArea(int[] area) {
		this.area = area;
	}

	public void addUpgrade(Upgrade u) {
		return;
	}

	public ArrayList<Upgrade> getUpgrades() {
		return null;
	}
	
	
	public boolean vacate(Player p, Room r) {
		if (this.isAdjacent(r))
			return players.remove(p);
		
		return false;
		
	}
	
	public boolean isAdjacent(Room r) {
		return connections.contains(r);
	}
	
	public boolean enter(Player p) {
		return players.add(p);
	}
	
	public void newDay() {
		for (Player p : players) {
			p.newDay();
		}
		
		players.clear();
	}
	
	public Scene newScene(Scene scene) {
		return null;
	}

	public void getUpgradeCosts() {
		return;
	}

	public int getNewRankMoney(int amount) {
		return -1;
	}

	public int getNewRankCr(int amount) {
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
	
	public Role commit(Player p, String role) {
		return null;
	}

	public int[] getArea() {
		return area;
	}

	public int[] getPlayerPos(){
		int[] pArea = Arrays.copyOf(area, area.length);
		pArea[1] += area[2];
		return pArea;
	}
}
