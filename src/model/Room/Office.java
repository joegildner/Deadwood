package model.Room;
import java.util.ArrayList;
import java.util.Arrays;

public class Office extends Room {

	private static final ArrayList<Integer> upgradeMoney = new ArrayList<>(Arrays.asList(4,10,18,28,40));
	private static final ArrayList<Integer> upgradeCr = new ArrayList<>(Arrays.asList(5,10,15,20,25));
	private ArrayList<Upgrade> upgrades;
	
	
	public Office() {
		super("Office");
		this.upgrades = new ArrayList<>();
	}
	
	@Override
	public int getNewRankMoney(int amount) {
		return upgradeMoney.indexOf(amount) + 2;
	}


	@Override
	public int getNewRankCr(int amount) {
		return upgradeCr.indexOf(amount) +2;
	}

	@Override
	public void addUpgrade(Upgrade u) {
		upgrades.add(u);
	}

	@Override
	public ArrayList<Upgrade> getUpgrades() {
		return this.upgrades;
	}
		
	
	@Override
	public void getUpgradeCosts() {
		int rank = 0;
		System.out.println("Pay dollars OR credits to upgrade.");
		System.out.println("RANK\tDOLLARS\tCREDITS");
		System.out.println("----\t-------\t-------");
		for (int i = 0; i < upgradeMoney.size(); i++) {
			rank = i+2;
			System.out.print(rank +"\t");
			System.out.print(upgradeMoney.get(i) + "\t");
			System.out.print(upgradeCr.get(i) + "\t");
			System.out.println();
		}
	}

	@Override
	public int[] getPlayerPos(){
		int[] pArea = Arrays.copyOf(area, area.length);
		pArea[1] += area[2]/5;
		return pArea;
	}
		
	
}
