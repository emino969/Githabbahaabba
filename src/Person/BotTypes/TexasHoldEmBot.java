package Person.BotTypes;

import cards.Card;
import cards.CardList;
import cards.CardSuit;
import cards.CardValue;
import money.Pot;
import Person.Person;
import Person.PersonState;
import pokerrules.AbstractPokermoves;
import pokerrules.texasholdem.HoldemHandComparator;
import pokerrules.texasholdem.TexasHand;
import pokerrules.texasholdem.TexasHoldem;
import pokerrules.texasholdem.TexasHoldemAction;
/**
 * a texasholdembot
 */
public class TexasHoldEmBot extends Person
{
    private HoldemHandComparator handComparator;
    private CardList idealHand = new CardList();
    private TexasHand desiredHand = null;
    private BehaviourState behaviourState = null;
    private final static int MAX_DECK_CARDS = 52;
    private boolean raiseThisRound = false;

    public TexasHoldEmBot(final String name, final Pot pot, TexasHoldem texasHoldem) {
	super(name, pot);
	this.game = texasHoldem;
	this.handComparator = texasHoldem.getHandComparator();
	this.desiredHand = TexasHand.HIGH_CARD;
	this.behaviourState = BehaviourState.NEUTRAL;
	idealHand.addNCards(new Card(CardSuit.DONT_CARE, CardValue.DONT_CARE), 2);
    }

    @Override public void turn() {
	AbstractPokermoves abstractPokermoves = game.getOptions();
	if (this.hasTurn()) {
	    if(raiseThisRound) abstractPokermoves.makeMove(TexasHoldemAction.CALL);
	    if (game.getDealer().getHand().getCopy().getSize() <= 3) {
		abstractPokermoves.makeMove(TexasHoldemAction.CALL);
	    } else {
		TexasHoldemAction texasHoldemAction = getBestMove();
		if(texasHoldemAction == TexasHoldemAction.RAISE) raiseThisRound = true;
		abstractPokermoves.makeMove(getBestMove());
	    }
	}
	changePersonState(PersonState.WAITING);
    }


    public TexasHoldemAction getBestMove() {
	TexasHand myHand = handComparator.getTexasHand(hand);
	int handValue = handComparator.getValue(myHand);
	desiredHand = getDesiredHand();
	//System.out.println(desiredHand);
	updateBehaviour(myHand.ordinal(), desiredHand.ordinal());
	return getActionByConfidence();
    }

    private TexasHand getDesiredHand() {
	int desiredMissingCardCount = 0;
	for (TexasHand texasHand : TexasHand.values()) {
	    int missingcards = missingCardsToHand(texasHand, hand);
	    if (missingcards <= desiredMissingCardCount && texasHand.ordinal() > desiredHand.ordinal()) {
		desiredHand = texasHand;
	    }
	}
	return desiredHand;
    }

    private double chanceTodrawHand(CardList missingCards) {
	if (3 <= hand.getSize()) return 0; // if hand has greater length or equal length to 3 there is no chance of drawing that hand as 2 cards or less are only ever drawn after start.
	int cardsToDraw = game.getDealer().getTableDeck().getCopy().getSize();
	int cardsLeft = MAX_DECK_CARDS - game.getDealer().getThrownCards().getCopy().getSize();
	double totPos = Combinatorics.choose(cardsLeft, cardsToDraw);
	int pos = 1;
	for (Card card : hand.getCardList()) {
	    int cardCount = game.getDealer().getTableDeck().getCopy().countCardValue(card.getValue());
	    pos *= (int) Combinatorics.choose(cardsLeft, 4 - cardCount);
	    cardsLeft--;
	}
	return pos / totPos;

    }

    private int missingCardsToHand(final TexasHand texasHand, CardList myHand) {
	//System.out.println(handComparator.getMissingCards(texasHand, myHand));
	return handComparator.getMissingCards(texasHand, myHand);
    }

    private void updateBehaviour(final int myhandValue, final int desiredHandValue) {
	double average = (myhandValue + desiredHandValue) / 2; // average € [HIGH_CARD.ordinal(), ROYAL_FLUSH.ordinal()}
	//System.out.println("average" + average);
	if (average < 3) {
	    this.behaviourState = BehaviourState.CAREFUL;
	}
	if (average < 6) {
	    this.behaviourState = BehaviourState.NEUTRAL;
	} else {
	    this.behaviourState = BehaviourState.CONFIDENT;
	}
    }

    public TexasHoldemAction getActionByConfidence() {
	switch (behaviourState) {
	    case CAREFUL:
		return TexasHoldemAction.FOLD;
	    case NEUTRAL:
		return TexasHoldemAction.CALL;
	    case CONFIDENT:
		return TexasHoldemAction.RAISE;
	    default:
		return TexasHoldemAction.CALL;
	}
    }
}
