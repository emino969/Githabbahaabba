package Person.BotTypes;

import Cards.Card;
import Cards.CardList;
import Cards.CardSuit;
import Cards.CardValue;
import Money.Pot;
import Person.BotTypes.BehaviourState;
import Person.BotTypes.Combinatorics;
import Person.Person;
import Person.PersonState;
import PokerRules.AbstractPokermoves;
import PokerRules.TexasHoldem.HoldemHandComparator;
import PokerRules.TexasHoldem.TexasHand;
import PokerRules.TexasHoldem.TexasHoldem;
import PokerRules.TexasHoldem.TexasHoldemAction;

import java.util.Random;
/**
 *
 */
public class TexasHoldEmBot extends Person
{
    private HoldemHandComparator handComparator;
    private CardList idealHand = new CardList();
    private TexasHoldem texasHoldem;
    private TexasHand desiredHand = null;
    private Random rnd = null;
    private BehaviourState behaviourState = null;
    private final static int MAX_DECK_CARDS = 52;

    public TexasHoldEmBot(final String name, final Pot pot, TexasHoldem texasHoldem) {
	super(name, pot);
	this.rnd = new Random();
	this.game = texasHoldem;
	this.texasHoldem = texasHoldem;
	this.handComparator = texasHoldem.getHandComparator();
	this.desiredHand = TexasHand.HIGH_CARD;
	this.behaviourState = BehaviourState.NEUTRAL;
	idealHand.addNCards(new Card(CardSuit.DONT_CARE, CardValue.DONT_CARE), 2);
    }

    @Override public void turn() {
	AbstractPokermoves abstractPokermoves = game.getOptions();
	if (this.hasTurn()) {
	    if (game.getDealer().getHand().getCopy().getSize() <= 3) {
		abstractPokermoves.makeMove(TexasHoldemAction.CALL);
	    } else {
		abstractPokermoves.makeMove(getBestMove());
	    }
	}
	changePersonState(PersonState.WAITING);
    }

    public void setBehaviourState(final BehaviourState behaviourState) {
	this.behaviourState = behaviourState;
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
	double handWeight = 0;
	int desiredMissingCardCount = 0;
	for (TexasHand texasHand : TexasHand.values()) {
	    CardList missingcards = missingCardsToHand(texasHand, hand);
	    if (missingcards.getSize() <= desiredMissingCardCount && texasHand.ordinal() > desiredHand.ordinal()) {
		desiredHand = texasHand;
	    }
	}
	//System.out.println(desiredHand);
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
	    pos *= Combinatorics.choose(cardsLeft, 4 - cardCount);
	    cardsLeft--;
	}
	return pos / totPos;

    }

    private CardList missingCardsToHand(final TexasHand texasHand, CardList myHand) {
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
