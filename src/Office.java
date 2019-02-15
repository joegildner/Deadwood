import java.util.ArrayList;
import java.util.Arrays;

public class Office extends Room {

	private static final ArrayList<Integer> upgradeMoney = new ArrayList<Integer>(Arrays.asList(4,10,18,28,40));
	private static final ArrayList<Integer> upgradeCr = new ArrayList<Integer>(Arrays.asList(5,10,15,20,25));
	
	
	public Office(ArrayList<Room> connections) {
		super("Casting Office", connections);
	}
	
	@Override
	public int getNewRank(String payType, int amount) {
		if (payType.contains("$")) {
			return upgradeMoney.indexOf(amount) + 2;
		}
		
		return upgradeCr.indexOf(amount) +2;
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
		
	
}
