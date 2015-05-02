package Person;

import cards.Card;
import cards.CardList;
import money.Pot;

public class Dealer extends Person
{
    private CardList thrownCards, gameCards;
    private Pot tablePot;

    public Dealer(Pot pot) {
	super("Dealer", pot);
	this.thrownCards = new CardList();
	this.gameCards = new CardList();
	this.tablePot = new Pot(0);
	gameCards.createOrdinaryDeck();
	gameCards.shuffleDeck();
	changePersonState(PersonState.INACTIVE); //Default for Dealer
    }

    public CardList getTableDeck()	{
	return gameCards;
    }

    public void givePot(Person person){
        person.addToPot(tablePot.getAmount());
        tablePot.clearPot();
    }

    public void giveAmountToPerson(Person person, int amount)	{//In case of multiple winners or a tie
	person.addToPot(amount);
	tablePot.subtractAmount(amount);
    }

    public void addCardToThrownCards(Card card)	{
	thrownCards.addCard(card);
    }

    public CardList getThrownCards()	{
	return thrownCards;
    }

    public void startNewGame()	{
	/** add all the thrownCards to hand and shuffle */
	gameCards.addCardList(thrownCards);
	thrownCards.clearCardList();
	gameCards.shuffleDeck();
    }

    public Pot getTablePot()	{
	return tablePot;
    }

    public void giveNCardsToPlayer(Person person, int n)	{
	for (int i = 0; i < n; i++) {
	    person.addCard(popCard());
	}
    }
    public void dealOutNCards(int n)	{
	int currentRound = 0;
	while	(n > currentRound)	{
	    for	(Person player : game.getActivePlayers())	{
		player.addCard(this.popCard());
	    }
	    currentRound++;
	}
    }

    public void dealOutNHiddenCards(int n)	{
	int currentRound = 0;
	while	(n > currentRound)	{
	    for	(Person player : game.getActivePlayers())	{
		if	(player.equals(game.getPlayer()))	{
		    player.addCard(this.popCard());
		}	else {
		    player.addHiddenCard(this.popCard());
		}
	    }
	    currentRound++;
	}
    }


    @Override public Card popCard()	{
	if (gameCards.getSize() == 0) {
	    //add the thrown cards and shuffle
	    startNewGame();
	}
	return gameCards.popCard();
    }
    public void collectCards()	{
	/** Throw all the players cards to thrownCards  */
	for	(Person player : game.getPlayers())	{
	    for (int i = 0; i < player.getHands().size(); i++)	{
		throwCards(player.mappedHands.get(i));
	    }
	    player.clearAllHands();
	    player.addHand();
	    player.setHand(player.mappedHands.get(0));
	}
    }


    private void throwCards(CardList hand)	{
	while	(!hand.isEmpty()) {
	    addCardToThrownCards(hand.popCard());
	}
    }

    public void giveStartingCards()	{}
}
