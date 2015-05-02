package Person;

import card.Card;
import card.CardList;
import GameListeners.GameListener;
import Money.Pot;
import PokerRules.AbstractGame;

import java.util.ArrayList;
import java.util.List;

public class Person
{
    protected String name;
    protected Pot pot;
    protected CardList hand;
    protected AbstractGame game = null;
    private PersonState state = PersonState.WAITING;
    private Pot defaultPot = new Pot(1000);
    protected int betHolder;
    protected int lastBet;
    public HandMap mappedHands;
    private GameListener gl = null;

    public Person(String name, Pot pot)	{
	this.name = name;
	this.pot = pot;
	this.hand = new CardList(); //Primary hand
	this.lastBet = 0;
	this.mappedHands = new HandMap();
	mappedHands.put(hand, state);
	//setGameListener();
    }


    public boolean isTurnLeft()	{
	return mappedHands.turnsLeft(hand);
    }

    public void switchHand()	{
	hand = mappedHands.nextHand(hand);
    }

    public int getLastBet()	{
	return lastBet;
    }

    public void setLastBet(int amount)	{
	lastBet = amount;
    }

    public void bet(int amount)	{
	if	(pot.getAmount() >= 0)	{
	    game.getDealer().getTablePot().addAmount(amount);
	    lastBet += amount;
	}
    }

    public boolean isBroke()	{
	return pot.getAmount() == 0;
    }

    public void addHiddenCard(Card card)	{
	hand.addHiddenCard(card);
    }

    public void setCardsVisible()	{
	hand.setCardsVisible();
    }

    public void quitGame()	{
	game.removePlayer(this);
    }

    public void changePersonState(PersonState state){
	mappedHands.put(hand, state);
    }

    public PersonState getState() {
        return getBestState();
    }

    public String getName() {
	return name;
    }

    public Pot getPot() {
	return pot;
    }

    public CardList getHand()	{
	return hand;
    }

    public void clearAllHands()	{
	mappedHands.clear();
    }

    public List<CardList> getHands()	{
	return mappedHands.getHands();
    }

    public CardList getHandByIndex(int index)	{
	//0 is always the primary hand
	return mappedHands.get(index);
    }

    public void addHand()	{
	//Adds a empty CardList
	CardList cl = new CardList();
	mappedHands.put(cl, PersonState.WAITING);
    }

    public void addCard(Card card)	{
	hand.addCard(card);
    }

    public Card popCard()	{
	return hand.popCard();
    }

    public void setGame(AbstractGame game)	{
	this.game = game;
    }

    public void addToPot(int winnings){
        pot.addAmount(winnings);
    }

    public void throwCards()	{
	while	(!hand.isEmpty()) {
	    game.getDealer().addCardToThrownCards(hand.popCard());
	}
    }

    public void turn()	{
        //does nothing
    }

    public boolean isPersonState(PersonState st)	{
	return getBestState() == st;
    }

    public PersonState getBestState()	{ //Specialized for Black Jack
	ArrayList<PersonState> states = mappedHands.getStates();
	if (states.contains(PersonState.TURN))	{
	    return PersonState.TURN;
	}	else if(states.contains(PersonState.WINNER))	{
	    return PersonState.WINNER;
	}	else if(states.contains(PersonState.WAITING))	{
	    return PersonState.WAITING;
	}	else if(states.contains(PersonState.INACTIVE))	{
	    return PersonState.INACTIVE;
	}	else	{
	    return PersonState.LOSER;
	}
    }

    @Override public String toString() {
        System.out.println("Name: " + name + " -- Pot: " + pot);
        System.out.println(hand);
        return "";
    }
    public boolean hasTurn(){
        return mappedHands.get(hand) == PersonState.TURN;
    }


    public void addToBet(final int money) {
	if (pot.getAmount() >= money) {
	    betHolder += money;
	    pot.subtractAmount(money);
	}
    }

    public int getBet() {
        int bet = betHolder;
        betHolder = 0;
        return bet;
    }

    public int getBetHolder() {
        return betHolder;
    }

    public void resetBet() {
        pot.addAmount(betHolder);
        betHolder = 0;
    }

    public void resetLastBet()	{
	lastBet = 0;
    }

    public void setHand(CardList cl)	{
	this.hand = cl;
    }
}
