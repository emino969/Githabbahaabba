package Person;

import Money.Pot;
import PokerRules.AbstractPokermoves;
import PokerRules.CardGameMove;

import java.util.Random;

public class Bot extends Person
{
    public Bot(String name, Pot pot)	{
	super(name, pot);
    }

    @Override public void turn()	{
	AbstractPokermoves APm = game.getOptions();
	Random rand = new Random();
	CardGameMove nextMove = APm.getOptions(this).get(rand.nextInt(APm.getOptions(this).size()));
	APm.makeMove(nextMove);
    }

    private int betAmount() {
        return 100;
    }
}
