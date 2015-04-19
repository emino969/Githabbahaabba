package Person.BotTypes;

import Money.Pot;
import Person.Person;
import PokerRules.TexasHoldem.TexasHoldem;
import PokerRules.TexasHoldem.TexasHoldemAction;

import java.util.Random;

public class TexasHoldEmBot extends Person
{
    private TexasHoldem texasHoldem;
    private Random rnd;
    public TexasHoldEmBot(final String name, final Pot pot, TexasHoldem texasHoldem) {
	super(name, pot);
	this.texasHoldem = texasHoldem;
    }

    @Override public void turn() {
        getBestMove();
    }

    public TexasHoldemAction getBestMove() {

        return null;
    }
}
