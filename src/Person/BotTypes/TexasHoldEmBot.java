package Person.BotTypes;

import Money.Pot;
import Person.Person;
import PokerRules.TexasHoldem.TexasHoldem;

public class TexasHoldEmBot extends Person
{
    private TexasHoldem texasHoldem;
    public TexasHoldEmBot(final String name, final Pot pot, TexasHoldem texasHoldem) {
	super(name, pot);
	this.texasHoldem = texasHoldem;
    }

    @Override public void turn() {
	super.turn();
    }

}
