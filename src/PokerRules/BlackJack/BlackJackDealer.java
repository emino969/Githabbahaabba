package PokerRules.BlackJack;

import Money.Pot;
import Person.Dealer;
import PokerRules.BlackJack.BlackJackAction;

public class BlackJackDealer extends Dealer
{
    private final static int DEALER_LIMIT = 17;
    private boolean hitSoft17 = true;

    public BlackJackDealer(final Pot pot, final int numberOfDecks) {
	super(pot);
	getTableDeck().clearCardList();
	getTableDeck().createBlackJackDeck(numberOfDecks);
    }

    public boolean getDealer17Rule()	{
	return hitSoft17;
    }

    public void setHitOn17(boolean state)	{
	hitSoft17 = state;
    }

    @Override public void giveStartingCards()	{
	hand.addCard(popCard());
 	hand.addHiddenCard(popCard());
    }

    @Override public void turn() {
	if (hand.isAllCardsVisible()) {
	    if (hand.getSumAceOnTop() < DEALER_LIMIT && !hitSoft17 || hand.getLegalHandSum() < 17 && hitSoft17) {
		game.getOptions().makeMove(BlackJackAction.HIT);
	    } else {
		game.setIsOverState(true);
	    }
	}	else if(hand.getSize() == 2)	{
	    hand.getCardByIndex(1).setVisible();
	}
    }
}
