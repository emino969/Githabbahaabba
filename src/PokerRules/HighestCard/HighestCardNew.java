package PokerRules.HighestCard;

import Money.Pot;
import Person.Person;
import Person.PersonState;
import PokerRules.BlackJack.BlackJackDealer;
import PokerRules.CardGameAction;
import Table.PokerGame;

import java.util.ArrayList;
import java.util.List;

public class HighestCardNew extends PokerGame
{
    private HighestCardmoves highestCardmoves;
    public HighestCardNew() {
	setDealer(new BlackJackDealer(new Pot(1000), 8)); //Default Pot
	dealer.setGame(this); //Dealer is created in table
	this.highestCardmoves = new HighestCardmoves()
	{
	    @Override public void stand() {
		currentPlayer.changePersonState(PersonState.WAITING);
	    }

	    @Override public void changeCard() {
		currentPlayer.throwCards();
		currentPlayer.addCard(dealer.popCard());

	    }

	    @Override public List<CardGameAction> getOptions(final Person person) {
		List<CardGameAction> actions = new ArrayList<>();
		if (person.hasTurn()) {
		    actions.add(HighestCardAction.BET);
		    actions.add(HighestCardAction.BET_25);
		    actions.add(HighestCardAction.BET_50);
		    actions.add(HighestCardAction.BET_75);
		}
		return actions;
	    }

	    @Override public void makeMove(final CardGameAction cardGameAction) {

	    }

	    @Override public String getHandValue(final Person person) {
		return null;
	    }
	};
    }
}
