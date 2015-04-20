package PokerRules.BlackJack;

import Money.Pot;
import Person.Dealer;

public class BlackJackDealer extends Dealer
{
    private final static int DEALER_LIMIT = 17;

    public BlackJackDealer(final Pot pot, final int numberOfDecks) {
	super(pot);
	getTableDeck().clearCardList();
	getTableDeck().createBlackJackDeck(numberOfDecks);
    }

    @Override public void giveStartingCards()	{
	hand.addCard(popCard());
 	hand.addHiddenCard(popCard());
    }

    @Override public void turn() {
	if (hand.isAllCardsVisible()) {
	    if (hand.getSumAceOnTop() < DEALER_LIMIT) {
		game.getOptions().makeMove(BlackJackAction.HIT);
	    } else {
		game.setIsOverState(true);
	    }
	}	else if(hand.getSize() == 2)	{
	    hand.getCardByIndex(1).setVisible();
	}
    }
}
