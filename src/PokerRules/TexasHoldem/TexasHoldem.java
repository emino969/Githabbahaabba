package pokerrules.texasholdem;

import Money.Pot;
import person.BotTypes.TexasHoldEmBot;
import person.Person;
import person.PersonState;
import pokerrules.AbstractGame;
import pokerrules.AbstractPokermoves;
import pokerrules.CardGameAction;

import java.util.ArrayList;
import java.util.List;

public class TexasHoldem extends AbstractGame
{
    private AbstractGame game = null;
    private int smallBlind, highestBet, bigBlind;
    private final static int CHIP_25 = 25;
    private final static int CHIP_50 = 50;
    private final static int CHIP_75 = 75;
    private int playersFinished = 0;
    private HoldemHandComparator handComparator = new HoldemHandComparator();
    private AbstractPokermoves moves = new HoldemMoves()	{
    	    @Override public void call()	{
		currentPlayer.resetBet(); //reset the current proposed bet
		int betAmount = getHighestBet() - currentPlayer.getLastBet();
		if(currentPlayer.getPot().getAmount() < betAmount) fold();
		currentPlayer.addToBet(betAmount);
		currentPlayer.bet(currentPlayer.getBet());
		currentPlayer.changePersonState(PersonState.WAITING);
		playersFinished++;
    	    }

    	    @Override public void raise()	{
		int betAmount = currentPlayer.getBet() + highestBet - currentPlayer.getLastBet();
		if(25 < betAmount){
		    currentPlayer.getBet();
		    currentPlayer.bet(betAmount);
		    addToHighestBet(betAmount);
		    currentPlayer.changePersonState(PersonState.WAITING);
		    playersFinished = 0;
		}	else	{
		    call();
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

    	    @Override public void makeMove(CardGameAction cardGameAction)	{
    		switch((TexasHoldemAction) cardGameAction)	{
    		    case CALL:
    			this.call();
    			break;
    		    case RAISE:
    			raise();
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

    public TexasHoldem() {
	super(new TexasHoldemDealer(new Pot(1000)));
	this.smallBlind = 25;
	this.bigBlind = 2 * 25;
	this.highestBet = 25;
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
	dealer.dealOutNCards(2);
	setCurrentPlayer(getPlayer());
	//clockTimer.start();
	handComparator.setPokerGame(this);
    }

    @Override public void dealerMove()	{
	getCurrentPlayer().changePersonState(PersonState.WAITING);
	getDealer().turn();
	setCurrentPlayer(getPlayer());
	playersFinished = 0;
    }

    @Override public void restartGame() {
	setIsOverState(false);
	clockTimer.setDelay(DELAY);
	dealer.collectCards();
	clearLastBets();
	dealer.startNewGame();
	deactivateDealer();
	setStartingStates();
	setCurrentPlayer(getPlayer());
	dealer.dealOutNHiddenCards(2);
    }

    @Override public void addBots() {
	TexasHoldEmBot playerBot = new TexasHoldEmBot("Bot", new Pot(1000),this);
	TexasHoldEmBot superMario = new TexasHoldEmBot("SuperMario", new Pot(1000), this);
	TexasHoldEmBot superBot = new TexasHoldEmBot("SuperBot", new Pot(1000), this);
	TexasHoldEmBot superPlayer = new TexasHoldEmBot("SuperPlayer", new Pot(1000), this);
	addPlayer(playerBot);
	addPlayer(superMario);
	addPlayer(superPlayer);
	addPlayer(superBot);
    }

    @Override public boolean dealersTurn() {
	for	(Person person : getOnlyActivePlayers())	{
	    if	((person.getLastBet() != highestBet) || !(playersFinished >= getActivePlayers().size()))	{
		return false;
	    }
	}
	return true;
    }

    @Override public boolean playersTurn() {
	return !dealersTurn();
    }


    @Override public void addPlayer(Person person)	{
	person.setGame(this);
	players.add(person);
    }

    @Override public AbstractPokermoves getOptions()	{
	return moves;
    }

    public void clearLastBets()	{
	for	(Person person : getActivePlayers())	{
	    person.setLastBet(0);
	}
	highestBet += smallBlind;
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

    public void addToHighestBet(int amount)	{
	highestBet += amount;
    }

    public AbstractGame getGame()	{
	return game;
    }

    public HoldemHandComparator getHandComparator() {
	return handComparator;
    }
}
