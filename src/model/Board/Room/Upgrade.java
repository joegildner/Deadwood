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

    public int getLevel() {
        return this.level;
    }

    public int getAmount() {
        return this.amt;
    }

    public String getCurrency() {
        return this.currency;
    }

    public int[] getArea() {
        return this.area;
    }

    public String getCurrency() {
        return currency;
    }

<<<<<<< HEAD
    public int getAmt() {
        return amt;
    }
=======


>>>>>>> fc77f3b7a75fa71ad00acd552d588c521cbd87ca
}
