package PokerRules.TexasHoldem;

import Cards.Card;
import Cards.CardList;
import java.util.Comparator;
import java.util.EnumMap;

public class HoldemHandComparator implements Comparator<CardList>
{
    private CardList tableDeck;
    private EnumMap<TexasHand, Integer> hands;

    @Override public int compare(CardList cl1, CardList cl2){
	TexasHand th1 = getTexasHand(cl1);
	TexasHand th2 = getTexasHand(cl2);
	if	(getValue(th1) > getValue(th2))	{
	    return 1;
	}	else if	(getValue(th1) == getValue(th2))	{
	    return 0;
	}	else	{
	    return -1;
	}
    }

    public void setTableDeck(CardList tableDeck)	{
	this.tableDeck = tableDeck;
	this.hands = new EnumMap<TexasHand, Integer>(TexasHand.class);
	initiateMap();
    }

    public TexasHand getTexasHand(CardList hand)	{
	if	(isOnePair(hand))	{
	    return TexasHand.PAIR;
	}	else if(isTwoPair(hand))	{
	    return TexasHand.TWO_PAIR;
	}	else if(isTriplets(hand))	{
	    return TexasHand.TRIPLETS;
	}	else if(isStraight(hand))	{
	    return TexasHand.STRAIGHT;
	}	else if(isFlush(hand))	{
	    return TexasHand.FLUSH;
	}	else if(isFullHouse(hand))	{
	    return TexasHand.FULL_HOUSE;
	}	else if(isQuads(hand))	{
	    return TexasHand.QUADS;
	}	else if(isStraightFlush(hand))	{
	    return TexasHand.STRAIGHT_FLUSH;
	}	else if(isRoyalFlush(hand))	{
	    return TexasHand.ROYAL_FLUSH;
	}	else	{
	    return TexasHand.EMPTY;
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

    private int getValue(TexasHand th)	{
	return hands.get(th);
    }

    public boolean isOnePair(CardList hand) {
	Card c1 = hand.getCardByIndex(0);
	Card c2 = hand.getCardByIndex(1);
	CardList cl = tableDeck.getCopy(tableDeck);
	cl.addCard(c1);
	cl.addCard(c2);
	return cl.countIntValue(c1) == 2 || cl.countIntValue(c2) == 2;
    }

    public boolean isTwoPair(CardList hand)	{
	Card c1 = hand.getCardByIndex(0);
	Card c2 = hand.getCardByIndex(1);
	CardList cl = tableDeck.getCopy(tableDeck);
	cl.addCard(c1);
	cl.addCard(c2);
	return cl.countIntValue(c1) == 2 && cl.countIntValue(c2) == 2;
    }

    public boolean isTriplets(CardList hand)	{
	Card c1 = hand.getCardByIndex(0);
	Card c2 = hand.getCardByIndex(1);
	CardList cl = tableDeck.getCopy(tableDeck);
	cl.addCard(c1);
	cl.addCard(c2);
	return cl.countIntValue(c1) == 3 || cl.countIntValue(c2) == 3;
    }

    public boolean isStraight(CardList hand)	{
	Card c1 = hand.getCardByIndex(0);
	Card c2 = hand.getCardByIndex(1);
	CardList cl = tableDeck.getCopy(tableDeck);
	cl.addCard(c1);
	cl.addCard(c2);
	return false; //Fix this
    }

    public boolean isFlush(CardList hand)	{
	Card c1 = hand.getCardByIndex(0);
	Card c2 = hand.getCardByIndex(1);
	CardList cl = tableDeck.getCopy(tableDeck);
	cl.addCard(c1);
	cl.addCard(c2);
	int sum1 = 0;
	int sum2 = 0;
	for (int i = 0; i < cl.getSize(); i++) {
	    if	(cl.getCardByIndex(i).getCardType().equals(c2.getCardType()))	{
		sum2++;
	    }

	    if	(cl.getCardByIndex(i).getCardType().equals(c1.getCardType()))	{
		sum1++;
	    }
	}

	return sum1 >= 5 || sum2 >= 5;
    }

    public boolean isFullHouse(CardList hand)	{
	return isOnePair(hand) && isTriplets(hand);
    }

    public boolean isQuads(CardList hand)	{
	Card c1 = hand.getCardByIndex(0);
	Card c2 = hand.getCardByIndex(1);
	CardList cl = tableDeck.getCopy(tableDeck);
	cl.addCard(c1);
	cl.addCard(c2);
	return cl.countIntValue(c1) == 4 || cl.countIntValue(c2) == 4;
    }

    public boolean isStraightFlush(CardList hand)	{
	return false; //Fix this
    }

    public boolean isRoyalFlush(CardList hand)	{
	return false; //fix this
    }
}
