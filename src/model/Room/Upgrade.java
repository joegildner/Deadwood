package model.Room;

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

    public int getLevel() {
        return this.level;
    }

    public String getCurrency() {
        return this.currency;
    }

    public int[] getArea() {
        return this.area;
    }


    public int getAmt() {
        return amt;
    }

}
