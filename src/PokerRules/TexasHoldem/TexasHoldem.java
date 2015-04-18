package PokerRules.TexasHoldem;

import Cards.CardList;
import Money.Pot;
import Person.Person;
import Person.PersonState;
import PokerRules.AbstractGame;
import PokerRules.AbstractPokermoves;
import PokerRules.CardGameMove;

import java.util.ArrayList;

public class TexasHoldem extends AbstractGame
{
    //Test the AbstractGame here
    private AbstractGame game;
    private int minBet, highestBet;
    private HoldemHandComparator handComparator = new HoldemHandComparator();
    private HoldemMoves moves = new HoldemMoves()	{
    	    @Override public void call()	{
    		currentPlayer.bet(getHighestBet() - currentPlayer.getLastBet());
    	    }

    	    @Override public void raise()	{
    		currentPlayer.bet(2 * minBet);
    		setHighestBet(2 * minBet);
    	    }

    	    @Override public void fold()	{
    		currentPlayer.changePersonState(PersonState.LOSER);
    	    }

    	    @Override public ArrayList<CardGameMove> getOptions(Person person)	{
    		ArrayList<CardGameMove> options = new ArrayList<>();
    		if	(!person.isPersonState(PersonState.INACTIVE))	{
    		    options.add(TexasHoldemAction.CALL);
    		    //options.add("Fold");
    		    if	(highestBet != 2 * minBet) {
    			options.add(TexasHoldemAction.RAISE);
    		    }
    		}
    		return options;
    	    }

    	    @Override public void makeMove(CardGameMove cardGameMove)	{
    		switch((TexasHoldemAction) cardGameMove)	{
    		    case CALL:
    			this.call();
       			currentPlayer.changePersonState(PersonState.WAITING);
    			break;
    		    case RAISE:
    			raise();
       			currentPlayer.changePersonState(PersonState.WAITING);
    			break;
    		    case FOLD:
    			fold();
    			break;
    		}
    	    }

    	    @Override public String getHandValue(Person person) {
    		return String.valueOf(handComparator.getTexasHand(person.getHand())); //Need to be assigned correctly
    	    }
    	};

    public TexasHoldem(final int minBet) {
	super(new TexasHoldemDealer(new Pot(1000)));
	this.minBet = minBet;
	this.highestBet = minBet;
	handComparator.setPokerGame(this);
    }

    @Override public boolean gameFinished() {
	return isOverState;
    }

    @Override public void getWinner() {
	showCards();
	List<Person> players = getActivePlayers();
	players.sort(handComparator);
	players.get(0).changePersonState(PersonState.WINNER);
	getDealer().givePot(players.get(0));
	for (int i = 1; i < players.size() ; i++) {
	    players.get(i).changePersonState(PersonState.LOSER);
	}
    }

    @Override public void startGame() {
	dealOutNHiddenCards(2);
	setCurrentPlayer(getPlayer());
	//clockTimer.start();
	handComparator.setPokerGame(this);
    }

    @Override public void dealerMove()	{
	getCurrentPlayer().changePersonState(PersonState.WAITING);
	getDealer().turn();
	clearLastBets();
	setCurrentPlayer(getPlayer());
    }

    @Override public void restartGame() {
	setIsOverState(false);
	clockTimer.setDelay(DELAY);
	collectCards();
	clearLastBets();
	dealer.startNewGame();
	setStartingStates();
	setCurrentPlayer(getPlayer());
	dealOutNHiddenCards(2);
    }

    @Override public boolean dealersTurn() {
	for	(Person person : getOnlyActivePlayers())	{
	    if	(person.getLastBet() != highestBet)	{
		return false;
	    }
	}
	return true;
    }

    @Override public boolean playersTurn() {
	return !dealersTurn();
    }


    @Override public void addPlayer(Person player)	{
	player.setGame(this);
	players.add(player);
    }

    @Override public AbstractPokermoves getOptions()	{
	return moves;
    }

    public void clearLastBets()	{
	for	(Person person : getActivePlayers())	{
	    person.setLastBet(0);
	}
	highestBet = minBet;
    }

    public void showCards()	{
	for	(Person player : getActivePlayers())	{
	    player.setCardsVisible();
	}
    }

    private void setStartingStates()	{
	for	(Person person : getOnlyPlayers())	{
	    person.changePersonState(PersonState.WAITING);
	}
    }

    public int getHighestBet()	{
    	return highestBet;
        }

    public int getMinimumBet()	{
	return minBet;
    }

    public void setMinimumBet(int amount)	{
	minBet = amount;
    }

    public void setHighestBet(int amount)	{
	highestBet = amount;
    }

    public PokerGame getGame()	{
	return game;
    }
}
