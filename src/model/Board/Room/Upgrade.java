package model.Board.Room;

/**
 * Created by grappom on 3/7/19.
 */
public class Upgrade {

    private int[] area;
    private int level;
    private int amt;
    private String currency;

    public Upgrade(int[] area, int level, int amt, String currency) {
        this.area = area;
        this.level = level;
        this.amt = amt;
        this.currency = currency;
    }

    public int[] getArea() {
        return this.area;
    }


}
