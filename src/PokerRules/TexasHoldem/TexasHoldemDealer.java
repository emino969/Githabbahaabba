package pokerrules.texasholdem;

import Money.Pot;
import person.Dealer;

public class TexasHoldemDealer extends Dealer
{
    private int round;

    public TexasHoldemDealer(Pot pot) {
	super(pot);
	this.round = 0;
    }

    @Override public void turn()	{
	round++;
	if (round == 1) {
	    giveNCardsToPlayer(this, 3);
	} else if (round == 4) {
	    game.setIsOverState(true);
	    round = 0;
	} else {
	    addCard(popCard());
	}
    }
}
