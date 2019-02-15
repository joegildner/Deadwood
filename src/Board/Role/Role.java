package Board.Role;
import Board.Player;

public abstract class Role implements Comparable<Role>{
	private String name;
	private String line;
	private int rank;
	private Player player;
	protected int[] reward;
	protected int pay;
	
	
	public Role(String name, String line, int rank) {
		this.name = name;
		this.line = line;
		this.rank = rank;
		this.player = null;
	}
	
	public int[] getReward() {
		return this.reward;
	}
	
	public int getPay() {
		return this.pay;
	}
	
	public boolean fill(Player p) {
		if (p.getRank() >= this.rank && this.player == null) {
			this.player = p;
			return true;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return this.name + ", " + this.line;
	}
	
	public int getRank() {
		return this.rank;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	@Override
	public int compareTo(Role r) {
		if (this == null || r == null) {
			return 0;
		}
		
		return this.rank - r.getRank();
	}
}
