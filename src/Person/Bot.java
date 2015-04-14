package Person;

import Money.Pot;
import PokerRules.AbstractPokermoves;

import java.util.Random;

public class Bot extends Person
{
    public Bot(String name, Pot pot)	{
	super(name, pot);
    }

    @Override public void turn()	{
	AbstractPokermoves APm = game.getOptions();
	Random rand = new Random();
	String nextMove = APm.getOptions(this).get(rand.nextInt(APm.getOptions(this).size()));
	APm.makeMove(nextMove);
    }

    private int betAmount() {
        return 100;
    }
}
