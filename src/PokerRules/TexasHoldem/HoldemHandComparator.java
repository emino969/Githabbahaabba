package PokerRules.TexasHoldem;

import Cards.Card;
import Cards.CardList;
import Cards.CardSuit;
import Cards.CardValue;
import Person.Person;
import PokerRules.AbstractGame;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;

public class HoldemHandComparator implements Comparator<Person>
{
    private AbstractGame game;
    private EnumMap<TexasHand, Integer> hands;
    private int MAX_HAND_SUM = 60;
    private Comparator<Card> cardComp = new Comparator<Card>()
    	{
    	    @Override public int compare(final Card o1, final Card o2) {
		int i1 = o1.getCardIntValue();
		int i2 = o2.getCardIntValue();
    		return intComp.compare(i1, i2);
    	    }
    	};

    private Comparator<Integer> intComp = new Comparator<Integer>()
    {
	@Override public int compare(final Integer o1, final Integer o2) {
	    if (o1 > o2)	{
		return 1;
	    }	else if(o1 == o2)	{
		return 0;
	    }	else	{
		return -1;
	    }
	}
    };

    @Override public int compare(Person p1, Person p2){
	TexasHand th1 = getTexasHand(p1.getHand());
	TexasHand th2 = getTexasHand(p2.getHand());
	if	(getValue(th1) < getValue(th2) || p1.getHand().getSumAceOnTop() < p2.getHand().getSumAceOnTop())	{
	    return 1;
	}	else if	(getValue(th1) == getValue(th2))	{
	    return compareSameHands(p1.getHand(), p2.getHand());
	}	else	{
	    return -1;
	}
    }

    public void setPokerGame(AbstractGame game)	{
	this.game = game;
	this.hands = new EnumMap<TexasHand, Integer>(TexasHand.class);
	initiateMap();
    }

    private CardList getTableDeck()	{
	CardList deck = game.getDealer().getHand();
	return deck.getCopy();
    }

    public TexasHand getTexasHand(CardList hand)	{
	if	(isRoyalFlush(hand))	{
	    return TexasHand.ROYAL_FLUSH;
	}	else if(isStraightFlush(hand))	{
	    return TexasHand.STRAIGHT_FLUSH;
	}	else if(isQuads(hand))	{
	    return TexasHand.QUADS;
	}	else if(isFullHouse(hand))	{
	    return TexasHand.FULL_HOUSE;
	}	else if(isFlush(hand))	{
	    return TexasHand.FLUSH;
	}	else if(isStraight(hand))	{
	    return TexasHand.STRAIGHT;
	}	else if(isTriplets(hand))	{
	    return TexasHand.TRIPLETS;
	}	else if(isTwoPair(hand))	{
	    return TexasHand.TWO_PAIR;
	}	else if(isOnePair(hand))	{
	    return TexasHand.PAIR;
	}	else	{
	    return TexasHand.HIGH_CARD;
	}
    }

    private void initiateMap()	{
	hands.put(TexasHand.EMPTY, 0);
	hands.put(TexasHand.PAIR, 1);
	hands.put(TexasHand.TWO_PAIR, 2);
	hands.put(TexasHand.TRIPLETS, 3);
	hands.put(TexasHand.STRAIGHT, 4);
	hands.put(TexasHand.FLUSH, 5);
	hands.put(TexasHand.FULL_HOUSE, 6);
	hands.put(TexasHand.QUADS, 7);
	hands.put(TexasHand.STRAIGHT_FLUSH, 8);
	hands.put(TexasHand.ROYAL_FLUSH, 9);
    }

    private int compareSameHands(CardList cl1, CardList cl2)	{
	//Is cl1 bigger than cl2?

	/** The cardlist cl1, cl2 needs to get the complete texashand  */

	int cardInt1 = getHighestCard(cl1).getCardIntValue();
	int cardInt2 = getHighestCard(cl2).getCardIntValue();
	if (cardInt1 < cardInt2)	{
	    return 1;
	}	else if(cardInt1 == cardInt2)	{
	    return 0;
	}	else	{
	    return -1;
	}
    }


    public int getValue(TexasHand th)	{
	//return hands.get(th);
	return th.ordinal();
    }

    private Card getHighestCard(CardList hand)	{
	Card c1 = hand.getCardByIndex(0);
	Card c2 = hand.getCardByIndex(1);
	if	(c1.getCardIntValue() > c2.getCardIntValue())	{
	    return c1;
	}	else	{
	    return c2;
	}
    }
    public int cardsToPair(CardList hand) {
    	return cardsToTriplets(hand) - 1;
        }

    public boolean isOnePair(CardList hand) {
	Card c1 = hand.getCardByIndex(0);
	Card c2 = hand.getCardByIndex(1);
	CardList cl = getTableDeck();
	cl.addCard(c1);
	cl.addCard(c2);
	return (cl.countIntValue(c1) == 2 || cl.countIntValue(c2) == 2);
    }
/*
    public boolean isTwoPair(CardList hand)	{
	Card c1 = hand.getCardByIndex(0);
	Card c2 = hand.getCardByIndex(1);
	CardList cl = getTableDeck();
	cl.addCard(c1);
	cl.addCard(c2);
	return cl.countIntValue(c1) == 2 && cl.countIntValue(c2) == 2 && c1.getCardInt() != c2.getCardInt();
    }
    */
    public int cardsToTwoPair(CardList hand) {
	int fullHandCardSize = 4;
	Card c1 = hand.getCardByIndex(0);
	Card c2 = hand.getCardByIndex(1);
	CardList cl = getTableDeck().getCopy();
	cl.addCard(c1);
	cl.addCard(c2);
	int pairs = 0;
	for (Card card : cl.getCopy().getCardList()) {
	    int cardCount = cl.countIntValue(card);
	    if(cardCount == 2) {
		cl.removeCard(card);
		pairs++;
	    }
	}
	int weight = fullHandCardSize - pairs * 2;
	if (weight > 0 )	{
	    return weight;
	}	else	{
	    return 0;
	}
    }

    public boolean isTwoPair(CardList hand) {
	return cardsToTwoPair(hand) <= 0;
    }


 /*   public boolean isTriplets(CardList hand)	{
	Card c1 = hand.getCardByIndex(0);
	Card c2 = hand.getCardByIndex(1);
	CardList cl = getTableDeck();
	cl.addCard(c1);
	cl.addCard(c2);
	return cl.countIntValue(c1) == 3 || cl.countIntValue(c2) == 3;
    }
    */
 public int cardsToTriplets(CardList hand) {
     int missingCards = cardsToQuads(hand) - 1;
     return missingCards;

 }

 public boolean isTriplets(CardList hand) {
return cardsToTriplets(hand) == 0;
 }


   /* public boolean isStraight(CardList hand)	{
	Card c1 = hand.getCard(0);
	Card c2 = hand.getCard(1);
	CardList cl = getTableDeck();
	cl.addCard(c1);
	cl.addCard(c2);
	return false; //Fix this boolean
    } */
    public boolean isFlush(CardList hand) {
    	return cardsToFlush(hand) == 0;
        }

    public boolean isFullHouse(CardList hand) {
    	return isOnePair(hand) && isTriplets(hand) && !hand.getCard(0).equals(hand.getCard(1));
        }

    public int cardsToStraight(CardList hand, CardSuit cardSuit) {
	CardList straight = new CardList();
	CardList missingCards = new CardList();
	CardList cl = game.getDealer().getHand().getCopy();
	Card c1 = hand.getCardByIndex(0);
	Card c2 = hand.getCardByIndex(1);
	cl.addCard(c1);
	cl.addCard(c2);
	for (int startBound = 2; startBound <= 10; startBound++) {
	    int endBound = startBound + 4;
	    straight.clearCardList();
	    for (int card = startBound; card <= endBound; card++) {
		if (!hand.containsCardValue(CardValue.getValueFromInt(card))) {
		    straight.addCard(new Card(CardSuit.DONT_CARE, CardValue.getValueFromInt(card)));
		}
		//missingcards has no cards to begin with, so for algorithm to work it has to have a starting value och a straighthand
		if (straight.getSize() < missingCards.getSize() && (straight.getSize() == 0) || missingCards.getSize() == 0)
		    missingCards = straight;
	    }
	}
	return missingCards.getSize();
    }


    /*public int cardsToStraight(CardList hand, CardSuit suit)	{
	int foundCards = 0;
	int greatestStair = 0;
	int FULL_STAIR = 5;
	ArrayList<Card> cards = turnHandToInts(hand);
	for (int i = 1; i < cards.size(); i++) {
	    if (foundCards == 5) {
		return FULL_STAIR - foundCards;
	    }	else if (Math.abs(cards.get(i).getCardIntValue() - cards.get(i - 1).getCardIntValue()) == 1 &&
			      (suit == CardSuit.DONT_CARE ||
			       (suit == hand.getCardList().get(i).getCardSuit() && suit == hand.getCardList().get(i-1).getCardSuit())))	{
		foundCards++;
	    }	else	{
		if (greatestStair < foundCards)	{
		    greatestStair = foundCards;
		}
		foundCards = 0;
	    }
	}
	return FULL_STAIR - greatestStair;
    }*/

    private ArrayList<Card> turnHandToInts(CardList hand)	{
	ArrayList<Card> cards = new ArrayList<>();
	CardList cl = game.getDealer().getHand().getCopy();
	cl.addCard(hand.getCardByIndex(0));
	cl.addCard(hand.getCardByIndex(1));
	cl.getCardList().sort(cardComp);
	return cl.getCardList();
    }

    public boolean isStraight(CardList hand) {
	return cardsToStraight(hand, CardSuit.DONT_CARE) == 0;
    }


    /*public boolean isFlush(CardList hand)	{
	Card c1 = hand.getCardByIndex(0);
	Card c2 = hand.getCardByIndex(1);
	CardList cl = getTableDeck();
	cl.addCard(c1);
	cl.addCard(c2);
	int sum1 = 0;
	int sum2 = 0;
	for (int i = 0; i < cl.getSize(); i++) {
	    if	(cl.getCardByIndex(i).getCardType() == c2.getCardType())	{
		sum2++;
	    }

	    if	(cl.getCardByIndex(i).getCardType() == c1.getCardType())	{
		sum1++;
	    }
	}
	return (sum1 >= 5 || sum2 >= 5);
    }*/
    public int cardsToFlush(CardList hand) {
   	Card cardOne = hand.getCardByIndex(0);
   	Card cardTwo = hand.getCardByIndex(1);
   	CardList cardList = getTableDeck();
   	cardList.addCard(cardOne);
   	cardList.addCard(cardTwo);
   	int cardOneSum = 0;
   	int cardTwoSum = 0;
   	for (Card card : cardList.getCardList()) {
   	    if (cardOne.getCardSuit() == card.getCardSuit()) {
   		cardOneSum++;
   	    }
   	    if (cardTwo.getCardSuit() == card.getCardSuit()) {
   		cardTwoSum++;
   	    }
   	}
   	int higher = cardOneSum;
   	if (cardOneSum < cardTwoSum) {
   	    higher = cardTwoSum;
   	    cardOne = cardTwo;
   	}
   	return higher;
       }

    public int cardsToQuads(CardList hand) {
	Card cardOne = hand.getCardByIndex(0);
	Card cardTwo = hand.getCardByIndex(1);
	CardList cl = getTableDeck();
	cl.addCard(cardOne);
	cl.addCard(cardTwo);
	int highestCardCount = 0;
	Card highestCard = new Card(CardSuit.DONT_CARE, CardValue.BOTTOM_ACE);
	for (Card card : cl.getCardList()) {
	    int cardCount = cl.countIntValue(card);
	    if(highestCardCount < cardCount || highestCardCount == cardCount && card.getCardIntValue() < highestCard.getCardIntValue()){
		highestCard = card;
		highestCardCount = cardCount;
	    }
	}
	if(4 < highestCardCount) highestCardCount = 4;
    	return 4 - highestCardCount;
        }

    public boolean isQuads(CardList hand)	{
	return cardsToQuads(hand) == 0;
    }
    public boolean isStraightFlush(CardList hand) {
    	return isStraight(hand) && isFlush(hand);
    }

    public boolean isRoyalFlush(CardList hand) {
    	if (isStraightFlush(hand) && hand.getSumAceOnTop() == MAX_HAND_SUM) return true;
    	return false;
    }

    /**
    public CardList cardsToRoyalFlush(CardList hand) {
    	CardList cardList = cardsToStraightFlush(hand);
    	cardList.addCard(hand.getCard(0));
    	cardList.addCard(hand.getCard(1));

    	CardList suitedCards = new CardList();
    	CardSuit cardSuit = cardList.getCard(0).getCardSuit();

    	suitedCards.addCard(new Card(cardSuit, CardValue.TEN));
    	suitedCards.addCard(new Card(cardSuit, CardValue.JACK));
    	suitedCards.addCard(new Card(cardSuit, CardValue.QUEEN));
    	suitedCards.addCard(new Card(cardSuit, CardValue.KING));
    	suitedCards.addCard(new Card(cardSuit, CardValue.TOP_ACE));
    	for (Card suitedCard : suitedCards.getCardList()){
    	    if(cardList.getCardList().contains(suitedCard)) cardList.removeCard(suitedCard);
    	    else cardList.addCard(suitedCard);
    	}
    	return cardList;
        }*/

    public int cardsToRoyalFlush(CardList hand)	{
	int biggestStair = cardsToStraight(hand, CardSuit.HEARTS);
	if (biggestStair == 0) {
	    hand.getCardList().sort(cardComp);
	    return 10 - hand.getCardList().get(hand.getSize() - 1).getCardIntValue();
	}	else	{
	    return 5;
	}
    }

    private int cardsToStraightFlush(final CardList hand) {
	ArrayList<Integer> list = new ArrayList<>();
	list.add(cardsToStraight(hand, CardSuit.CLUBS));
	list.add(cardsToStraight(hand, CardSuit.DIAMONDS));
	list.add(cardsToStraight(hand, CardSuit.HEARTS));
	list.add(cardsToStraight(hand, CardSuit.SPADES));
	list.sort(intComp);
	return list.get(0);
    }

    public int getMissingCards(final TexasHand texasHand, final CardList hand) {
	switch (texasHand) {
	    case ROYAL_FLUSH:
		return cardsToRoyalFlush(hand);
	    case STRAIGHT_FLUSH:
		return cardsToStraightFlush(hand);
	    case QUADS:
		return cardsToQuads(hand);
	    case FLUSH:
		return cardsToFlush(hand);
	    case STRAIGHT:
		return cardsToStraight(hand, CardSuit.DONT_CARE);
	    case TRIPLETS:
		return cardsToTriplets(hand);
	    case TWO_PAIR:
		return cardsToTwoPair(hand);
	    case PAIR:
		return cardsToPair(hand);
	    case HIGH_CARD:
		return 0;
	    default:
		return -1;
	}
    }

}
