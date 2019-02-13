
public class Player {
	
	private String name;
	private int rank;
	private int money;
	private int credits;
	private int tokens;
	private Role curRole;
	private Room curRoom;
	
	
	public Player(String name, Room room) {
		this.name = name;
		this.rank = 1;
		this.money = 0;
		this.credits = 0;
		this.tokens = 0;
		this.curRole = null;
		this.curRoom = room;
		this.curRoom.enter(this);
	}
	
	public void addReward(int[] reward) {
		this.money += reward[0];
		this.credits += reward[1];
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.name);
		sb.append(", rank " + this.rank);
		sb.append(" ($" + this.money + ", " + this.credits + ") ");
		if (this.curRole != null) {
			sb.append("working ");
			sb.append(this.curRole);
		}
		
		return sb.toString();
	}
	
	public String getRoom() {
		return "in " + this.curRoom;
	}
	
	public int getRank() {
		return this.rank;
	}

	public void rehearse() {
		if (this.curRole != null)
			this.tokens++;
	}
	
	public void commit(Role role) {
		this.curRole = curRoom.commit(this, role);
	}
	
	public void upgrade(String payType, int amount) {
		//TODO: check that room is upgrade office
		if (payType.contains("$")) {
			money -= amount;
		} else {
			credits -= amount;
		}
		
		rank = curRoom.getNewRank(payType, amount);
	}
	
	public void newDay() {
		this.tokens = 0;
		this.curRole = null;
		//TODO
	}
	
	public void act(int roll) {
		if (this.curRoom.take(roll + this.tokens)) {
			this.addReward(this.curRole.getReward());
		} else {
			this.money += this.curRole.getPay();
		}
	}
	
	public boolean move(Room room) {
		//TODO
		if (this.curRoom.vacate(this, room)) {
			this.curRoom = room;
			return this.curRoom.enter(this);	
		}
		
		return false;
	}

}
