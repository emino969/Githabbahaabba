package Person;

import Cards.Card;
import Cards.CardList;
import GameListeners.GameListener;
import Money.Pot;
import Table.PokerGame;

import java.util.ArrayList;
import java.util.List;

public class Person
{
    protected String name;
    protected Pot pot;
    protected CardList hand;
    protected PokerGame game = null;
    private PersonState state = PersonState.WAITING;
    private Pot defaultPot = new Pot(1000);
    protected ArrayList<CardList> multipleHands;
    protected int betHolder;
    protected int lastBet;
    private GameListener gl;

    public Person(String name, Pot pot)	{
	this.name = name;
	this.pot = pot;
	this.hand = new CardList(); //Primary hand
	this.multipleHands = new ArrayList<>();
	this.lastBet = 0;
	multipleHands.add(hand);
	setGameListener();
    }

    private void setGameListener()	{
	this.gl = new GameListener()	{
	    @Override public void gameChanged()	{
		if	(game.gameFinished())	{
		     setLastBet(0);
		}
	    }
	};

	PokerGame.addGameListener(gl);
    }

    public int getLastBet()	{
	return lastBet;
    }

    public void setLastBet(int amount)	{
	lastBet = amount;
    }

    public boolean bet(int amount)	{
	if	(pot.getAmount() >= amount)	{
	    pot.subtractAmount(amount);
	    game.getDealer().getTablePot().addAmount(amount);
	    lastBet += amount;
	}	else	{
	    throw new IllegalArgumentException("You don't have enough money!");
	}
	return pot.getAmount() >= amount;
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
        this.state = state;
    }

    public PersonState getState() {
        return state;
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

    public List<CardList> getHands()	{
	return multipleHands;
    }

    public CardList getHandByIndex(int index)	{
	//0 is always the primary hand
	return multipleHands.get(index);
    }

    public void addHand()	{
	//Adds a empty CardList
	multipleHands.add(new CardList());
    }

    public void addCard(Card card)	{
	hand.addCard(card);
    }

    public Card popCard()	{
	return hand.popCard();
    }

    public void setGame(PokerGame game)	{
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
	return state.equals(st);
    }

    @Override public String toString() {
        System.out.println("Name: " + name + " -- Pot: " + pot + "");
        System.out.println(hand);
        return "";
    }
    public boolean hasTurn(){
        return state == PersonState.TURN;
    }


    public void addToBet(final int money) {
        betHolder += money;
        pot.subtractAmount(money);
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
}
