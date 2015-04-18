package PokerRules.HighestCard;

import Cards.Card;
import Money.Pot;
import Person.BotTypes.HighestCardBot;
import Person.Dealer;
import Person.Person;
import Person.PersonState;
import PokerRules.CardGameAction;
import Table.PokerGame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HighestCard extends PokerGame
{
    private int rounds;
    private HighestCardmoves moves;

    public HighestCard() {
	this.rounds = 0;
	setDealer(new Dealer(new Pot(1000))); //The dealer's personal play account
 	this.moves = new HighestCardmoves()	{

 	    @Override public List<CardGameAction> getOptions(Person person)	{
 		List<CardGameAction> actions = new ArrayList<>();
 	    	actions.add(HighestCardAction.STAND);
 	    	actions.add(HighestCardAction.CHANGE_CARD);
 	    	return actions;
 	    }

 	    @Override public void makeMove(CardGameAction cardGameAction)	{
		switch ((HighestCardAction)cardGameAction){
		    case STAND:
			stand();
			break;
		    case CHANGE_CARD:
			changeCard();
			break;
		}
 	    }

	    @Override public String getHandValue(final Person person) {
		return "Hand: " + String.valueOf(person.getHand().getSumAceOnTop());
	    }

	    @Override public void stand()	{
		currentPlayer.changePersonState(PersonState.WAITING);
 	    }

 	    @Override public void changeCard()	{
 		Card card = getCurrentPlayer().popCard();
 	      	dealer.addCardToThrownCards(card);
		dealer.giveNCardsToPlayer(currentPlayer, 1);
		currentPlayer.changePersonState(PersonState.WAITING); 	    }
 	};
 	setOptions(moves);
     }


    @Override public void addPlayer(Person player)	{
	player.setGame(this);
	players.add(player);
    }

    @Override public boolean gameFinished() {
	if (rounds == 3){
	    return true;
	} else {
	    return false;
	}
    }


    public void runGameForward()	{
	if (!gameFinished()) {
	    getNextPlayer();
	    currentPlayer.turn();
	}else{
	    clockTimer.stop();
	    getWinner();
    	}
	notifyListeners();
    }
    private Person nextPerson()	{
	if	(!getActivePlayers().isEmpty()) {
	    if	(getActivePlayers().contains(currentPlayer)) {
		return getPersonByIndex((currentPlayerIndex + 1) % getActivePlayers().size());
	    }	else	{
		return getPersonByIndex(currentPlayerIndex % getActivePlayers().size());
	    }
	}	else	{
	    return null;
	}
    }
	// handcomparator 
    private void checkRoundWinner() {
	//Need to clone because shuffles around the players in the table
	List<Person> players = new ArrayList<Person>(getActivePlayers());
	players.sort(new HandComparator());
	dealer.givePot(players.get(getPlayersSize()-1));
	notifyListeners();
    }

    public void startGame()	{
    	dealOutNCards(1);
    	getNextPlayer();
    	ActionListener move =  new AbstractAction() {
    	    @Override public void actionPerformed(ActionEvent e)	{
    		runGameForward();
    	    }
    	};

    	clockTimer.addActionListener(move);
    	clockTimer.start();
        }

    public void getWinner()	{
	//Need to clone because shuffles around the players in the table
	List<Person> players = new ArrayList<Person>(getActivePlayers());
	players.sort(new HandComparator());
	for (int i = 0; i < players.size(); i++) {
	    if(i == getPlayersSize() -1) dealer.givePot(players.get(i));
	    System.out.println(players.size() - i + ". " + players.get(i).getName() + " " + players.get(i).getPot());
	}
    }
    public void addBot(String name, Pot pot){
	this.addPlayer(new HighestCardBot(name, pot, this));
    }

    private boolean finishedRound()	{
	if (getIndexByPerson(currentPlayer) == getPlayersSize() - 1) {
	    checkRoundWinner();
	    rounds++;
	    return true;
    	} else	{
	    return false;
	}
    }

    private void getNextPlayer()	{
	setCurrentPlayer(nextPerson());
     }


    @Override public void nextMove()	{
	clockTimer.restart();
	runGameForward();
    }

    @Override public void restartGame()	{
	collectCards();
	dealer.startNewGame();
	rounds = 0;
	currentPlayer = getPersonByIndex(0);
	dealOutNCards(1);
	clockTimer.start();
	notifyListeners();
    }

    private class HandComparator implements Comparator<Person>
    {
	@Override public int compare(Person p1, Person p2){
	    int truthValue = compareCards(p1.getHand().getCardByIndex(0).getValue(), p2.getHand().getCardByIndex(0).getValue());
	    return truthValue;
	}

	private int compareCards(Comparable c1, Comparable c2){
	    int a = c1.compareTo(c2);
	    if(a == 0) return 0;
	    if(a > 0) return 1;
	    return -1;
	}
    }
}
