package money;
/**
 *
 */
public class Pot {
    private int amount;

    public Pot(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void addAmount(int n) {
        amount += n;
    }

    public void subtractAmount(int n)   {
        amount -= n;
    }

    public void clearPot(){
        amount = 0;
    }

    @Override public String toString() {
        return String.valueOf(amount);
    }
}
