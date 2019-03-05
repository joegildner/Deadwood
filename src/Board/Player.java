package Board;
import Board.Role.Role;
import Board.Room.Trailer;
import Board.Room.Room;

public class Player {
	
	private String name;
	private int rank;
	private int money;
	private int credits;
	private int tokens;
	private Role curRole;
	private Room curRoom;
	private Trailer trailer;
	
	
	public Player(String name, Trailer trailer) {
		this.name = name;
		this.rank = 1;
		this.money = 0;
		this.credits = 0;
		this.tokens = 0;
		this.trailer = trailer;
		this.curRole = null;
		this.curRoom = trailer;
		this.curRoom.enter(this);
	}
	
	public void addReward(int[] reward) {
		this.money += reward[0];
		this.credits += reward[1];
	}
	
	public void addEarnings(int money) {
		this.money += money;
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
	
	public String getName() {
		return this.name;
	}
	
	public int getRank() {
		return this.rank;
	}
	
	public int getScore() {
		return this.money + this.credits + (this.rank * 5);
	}

	public void rehearse() {
		if (this.curRole != null)
			this.tokens++;
	}
	
	public void commit(String role) {
		 Role r = curRoom.commit(this, role);
		 if (r != null) {
			 this.curRole = r;
		 } else {
			 System.out.print("Could not commit to role \"" + role + "\" ");
			 System.out.println("in room " + curRoom.getName());
		 }
	}
	
	public void upgrade(String payType, int amount) {
		int newRank = curRoom.getNewRank(payType, amount);

		if (newRank < 1) {
			System.out.println("Upgrade not possible in " + this.curRoom.getName());
		} else {

			if (payType.contains("$")) {
				this.money -= amount;
			} else {
				this.credits -= amount;
			}
			this.rank = newRank;
		}
	}
	
	public void newDay() {
		this.tokens = 0;
		this.curRole = null;
		if (this.trailer.enter(this))
			this.curRoom = this.trailer;
		
	}
	
	public void act(int roll) {
		if (this.curRoom.take(roll + this.tokens)) {
			this.addReward(this.curRole.getReward());
		} else {
			this.money += this.curRole.getPay();
		}
	}
	
	public boolean move(Room room) {
		if (this.curRoom.vacate(this, room)) {
			this.curRoom = room;
			return this.curRoom.enter(this);	
		}
		
		this.curRole = null;
		
		return false;
	}

	

}