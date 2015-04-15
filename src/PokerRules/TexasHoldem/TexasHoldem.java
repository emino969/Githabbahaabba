package PokerRules.TexasHoldem;

import Cards.CardList;
import Money.Pot;
import Person.Person;
import Person.PersonState;
import PokerRules.AbstractGame;
import Table.PokerGame;

import java.util.ArrayList;

public class TexasHoldem extends PokerGame
{
    //Test the AbstractGame here
    private AbstractGame game;
    private int minBet, highestBet;
    private HoldemHandComparator HHM = new HoldemHandComparator();
    private HoldemMoves moves;

    public TexasHoldem(final int minBet) {
	this.minBet = getMinimumBet();
	this.highestBet = minBet;
	this.moves = new HoldemMoves()	{
	    @Override public void call()	{
		game.getCurrentPlayer().bet(getHighestBet() - game.getCurrentPlayer().getLastBet());
	    }

	    @Override public void raise()	{
		game.getCurrentPlayer().bet(2 * getMinimumBet());
		setHighestBet(2 * getMinimumBet());
	    }

	    @Override public void fold()	{
		game.getCurrentPlayer().changePersonState(PersonState.LOSER);
	    }

	    @Override public ArrayList<String> getOptions(Person person)	{
		ArrayList<String> options = new ArrayList<String>();
		if	(!person.isPersonState(PersonState.INACTIVE))	{
		    options.add("Call");
		    //options.add("Fold");
		    if	(getHighestBet() != 2 * getMinimumBet()) {
			options.add("Raise");
		    }
		}
		return options;
	    }

	    @Override public void makeMove(String name)	{
		if	(name.equals("Call"))	{
		    this.call();
		    game.getCurrentPlayer().changePersonState(PersonState.WAITING);
		}	else if(name.equals("Raise"))	{
		    raise();
		    game.getCurrentPlayer().changePersonState(PersonState.WAITING);
		}	else if(name.equals("Fold"))	{
		    fold();
		}
	    }

	    @Override public String getHandValue(Person person) {
		return String.valueOf(HHM.getTexasHand(person.getHand())); //Need to be assigned correctly
	    }
	};

	this.game = new AbstractGame(new TexasHoldemDealer(new Pot(1000)), moves)	{
	    private HoldemHandComparator HHM = new HoldemHandComparator();

	    @Override public boolean gameFinished() {
		return isOverState;
	    }

	    @Override public void getWinner() {
		showCards();
		ArrayList<Person> players = getActivePlayers();
		players.sort(HHM);
		players.get(0).changePersonState(PersonState.WINNER);
		getDealer().givePot(players.get(0));
		for (int i = 1; i < players.size() ; i++) {
		    players.get(i).changePersonState(PersonState.LOSER);
		}
	    }

	    @Override public void startGame() {
		dealOutNHiddenCards(2);
		//activateWaitingDealer();
		setCurrentPlayer(getPlayer());
		//clockTimer.start();
		HHM.setPokerGame(this);
		setMinimumBet(10);
		setHighestBet(getMinimumBet());
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
		    if	(person.getLastBet() != getHighestBet())	{
			return false;
		    }
		}
		return true;
	    }

	    @Override public boolean playersTurn() {
		return !dealersTurn();
	    }


	    @Override public boolean yourTurn() {
		return false;
	    }

	    @Override public void addPlayer(Person player)	{
		player.setGame(this);
		players.add(player);
	    }

	    public void clearLastBets()	{
		for	(Person person : getActivePlayers())	{
		    person.setLastBet(0);
		}
		setHighestBet(getMinimumBet());
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
	};
	HHM.setPokerGame(game);
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
