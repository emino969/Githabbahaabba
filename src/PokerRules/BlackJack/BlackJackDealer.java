package PokerRules.BlackJack;

import Money.Pot;
import Person.Dealer;

public class BlackJackDealer extends Dealer
{
    public BlackJackDealer(final Pot pot) {
	super(pot);
    }

    @Override public void giveStartingCards()	{
	hand.addCard(popCard());
 	hand.addHiddenCard(popCard());
    }

    @Override public void turn() {
	if (hand.isAllCardsVisible()) {
	    if (hand.getSumAceOnTop() < 17) {
		game.getOptions().makeMove("Hit");
	    } else {
		game.getOptions().makeMove("Stand");
		game.setIsOver();
	    }
	}	else if(hand.getSize() == 2)	{
	    hand.getCardByIndex(1).setVisible();
	}
    }
}
