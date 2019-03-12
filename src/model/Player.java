package model;
import model.Role.Role;
import model.Room.Trailer;
import model.Room.Room;

public class Player {
	
	private String name;
	private String[] imgFiles;
	private int rank;
	private int money;
	private int credits;
	private int tokens;
	private Role curRole;
	private Room curRoom;
	private Trailer trailer;
	
	
	public Player(String name, Trailer trailer, String[] imgFiles) {
		this.name = name;
		this.imgFiles = imgFiles;
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

	public String getImgFile() {
		return imgFiles[rank-1];
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
		if (this.curRole != null) {
			this.tokens++;
			System.out.println("You have " + tokens + " tokens.");
		} else {
			System.out.println("You are not working a role.");
		}
	}
	
	public void commit(String role) {
		if (curRole == null) {
			if (role != null) {
				Role r = curRoom.commit(this, role);
				if (r != null) {
					this.curRole = r;
					System.out.print("Committed to \"" + role + "\" ");

				} else {
					System.out.print("Could not commit to role \"" + role + "\" ");
				}
				System.out.println("in room " + curRoom.getName());
			} else {
				System.out.println("You must enter a role to work");
			}
		} else {
			System.out.println("You cannot commit to a new role until you complete work as " + curRole.getName());
		}
	}

	private void clearRole() {
		this.curRole = null;
	}

	public void upgradeCr(int amount) {
		int newRank = curRoom.getNewRankCr(amount);

		if (newRank < 1) {
			System.out.println("Upgrade not possible in " + this.curRoom.getName());
		} else {
			boolean upgrade = false;
			if (amount <= this.credits) {
				this.credits -= amount;
				upgrade = true;
			} else {
				System.out.println("Not enough credits to perform upgrade");
			}
			if (upgrade) {
				this.rank = newRank;
				System.out.println("New rank: " + this.rank);
			}
		}
	}

	public void upgradeMoney(int amount) {
		int newRank = curRoom.getNewRankMoney(amount);

		if (newRank < 1) {
			System.out.println("Upgrade not possible in " + this.curRoom.getName());
		} else {
			boolean upgrade = false;
			if (amount <= this.money) {
				this.money -= amount;
				upgrade = true;
			} else {
				System.out.println("Not enough money to perform upgrade");
			}
			if (upgrade) {
				this.rank = newRank;
				System.out.println("New rank: " + this.rank);
			}
		}
	}
	
	public void newDay() {
		this.tokens = 0;
		this.curRole = null;
		if (this.trailer.enter(this))
			this.curRoom = this.trailer;
		
	}
	
	public void act(int roll) {
		if (this.curRole == null){
			System.out.println("You are not working a role.");
		} else if (!this.curRoom.isComplete()) {
			if (this.curRoom.take(roll + this.tokens)) {
				this.addReward(this.curRole.getReward());
				System.out.println("Success!");
				if (this.curRoom.isComplete())
					this.clearRole();
			} else {
				this.money += this.curRole.getPay();
				System.out.println("Failure :(");
			}
		} else {
			System.out.println("This scene has already wrapped!");
		}
	}
	
	public boolean move(Room room) {
		boolean ret = false;
		if (curRole != null) {
			System.out.println("You cannot leave " + curRoom.getName() + " until you finish the scene.");
		} else if (this.curRoom.vacate(this, room)) {
			this.curRoom = room;
			ret = this.curRoom.enter(this);
			this.curRole = null;
			this.tokens = 0;
		}
		
		return ret;
	}

	public Role getCurRole() {
		return curRole;
	}

	public Room getCurRoom() {
		return curRoom;
	}
}
