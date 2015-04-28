package PokerRules.TexasHoldem;

import Money.Pot;
import Person.BotTypes.TexasHoldEmBot;
import Person.Person;
import Person.PersonState;
import PokerRules.AbstractGame;
import PokerRules.AbstractPokermoves;
import PokerRules.CardGameAction;
import java.util.ArrayList;
import java.util.List;

public class TexasHoldem extends AbstractGame
{
    //Test the AbstractGame here
    private AbstractGame game;
    private int smallBlind, highestBet, bigBlind;
    private final static int CHIP_25 = 25;
    private final static int CHIP_50 = 50;
    private final static int CHIP_75 = 75;
    private HoldemHandComparator handComparator = new HoldemHandComparator();
    private AbstractPokermoves moves = new HoldemMoves()	{
    	    @Override public void call()	{
		int betAmount = getHighestBet() - currentPlayer.getLastBet();
		currentPlayer.addToBet(betAmount); //DO NOT UNDER ANY CIRCUMSTANCES USE PERSON.BET() DIRECTLY
		currentPlayer.bet(currentPlayer.getBet());
		currentPlayer.changePersonState(PersonState.WAITING);
    	    }

    	    @Override public void raise()	{
    		//currentPlayer.bet(2 * minBet);
		int betAmount = currentPlayer.getBet();
		if (betAmount > 0) {
		    currentPlayer.bet(betAmount);
		    setHighestBet(betAmount);
		    currentPlayer.changePersonState(PersonState.WAITING);
		}
    	    }

    	    @Override public void fold()	{
    		currentPlayer.changePersonState(PersonState.LOSER);
    	    }

    	    @Override public ArrayList<CardGameAction> getOptions(Person person)	{
    		ArrayList<CardGameAction> options = new ArrayList<>();
    		if	(!person.isPersonState(PersonState.INACTIVE))	{
    		    options.add(TexasHoldemAction.CALL);
		    options.add(TexasHoldemAction.BET_25);
		    options.add(TexasHoldemAction.BET_50);
		    options.add(TexasHoldemAction.BET_75);
		    options.add(TexasHoldemAction.RAISE);
    		}
    		return options;
    	    }

    	    @Override public void makeMove(CardGameAction cardGameMove)	{
    		switch((TexasHoldemAction) cardGameMove)	{
    		    case CALL:
    			this.call();
			System.out.println("PLAYER " + currentPlayer.getName() + " CALLED");
    			break;
    		    case RAISE:
    			raise();
			System.out.println("PLAYER " + currentPlayer.getName() + " RAISED");
    			break;
    		    case FOLD:
    			fold();
    			break;
		    case BET_25:
			currentPlayer.addToBet(CHIP_25);
			break;
		    case BET_50:
			currentPlayer.addToBet(CHIP_50);
			break;
		    case BET_75:
			currentPlayer.addToBet(CHIP_75);
			break;
    		}
		notifyListeners();
    	    }

    	    @Override public String getHandValue(Person person) {
    		return String.valueOf(handComparator.getTexasHand(person.getHand())); //Need to be assigned correctly
    	    }
    	};

    public TexasHoldem(final int minBet) {
	super(new TexasHoldemDealer(new Pot(1000)));
	this.smallBlind = minBet;
	this.bigBlind = 2 * minBet;
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
	dealer.dealOutNHiddenCards(2);
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
	dealer.collectCards();
	clearLastBets();
	dealer.startNewGame();
	setStartingStates();
	setCurrentPlayer(getPlayer());
	dealer.dealOutNHiddenCards(2);
    }

    @Override public void addBots() {
	TexasHoldEmBot PlayerBot = new TexasHoldEmBot("Bot", new Pot(1000),this);
	TexasHoldEmBot SuperMario = new TexasHoldEmBot("SuperMario", new Pot(1000), this);
	TexasHoldEmBot SuperBot = new TexasHoldEmBot("SuperBot", new Pot(1000), this);
	TexasHoldEmBot SuperPlayer = new TexasHoldEmBot("SuperPlayer", new Pot(1000), this);
	addPlayer(PlayerBot);
	addPlayer(SuperMario);
	addPlayer(SuperPlayer);
	addPlayer(SuperBot);
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
	highestBet = smallBlind;
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

    public void setHighestBet(int amount)	{
	highestBet = amount;
    }

    public AbstractGame getGame()	{
	return game;
    }

    public HoldemHandComparator getHandComparator() {
	return handComparator;
    }
}
