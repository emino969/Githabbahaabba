package Person;

import CardGameExceptions.CardGameActionException;
import Money.Pot;
import PokerRules.AbstractPokermoves;
import PokerRules.CardGameAction;

import java.util.Random;

public class Bot extends Person
{
    public Bot(String name, Pot pot)	{
	super(name, pot);
    }

    @Override public void turn()	{
	AbstractPokermoves APm = game.getOptions();
	Random rand = new Random();
	CardGameAction nextMove = APm.getOptions(this).get(rand.nextInt(APm.getOptions(this).size()));
	try {
	    APm.makeMove(nextMove);
	} catch (CardGameActionException e) {
	    e.printStackTrace();
	}
    }

    private int betAmount() {
        return 100;
    }
}
