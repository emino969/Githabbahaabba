package Tests;

import card.Card;
import card.CardList;
import card.CardSuit;
import card.CardValue;
import PokerRules.TexasHoldem.HoldemHandComparator;

public class testTexasHandComparator
{
    private testTexasHandComparator() {}

    public static void main(String[] args) {
	CardList cardList = new CardList();
	Card card1 = new Card(CardSuit.CLUBS, CardValue.THREE);
	Card card2 = new Card(CardSuit.CLUBS, CardValue.FOUR);
	Card card3 = new Card(CardSuit.CLUBS, CardValue.TEN);
	Card card4 = new Card(CardSuit.DIAMONDS, CardValue.JACK);
	Card card5 = new Card(CardSuit.HEARTS, CardValue.JACK);
	cardList.addCard(card4);
	cardList.addCard(card5);
	HoldemHandComparator handComparator = new HoldemHandComparator();
	int hand = handComparator.cardsToStraight(cardList);//, CardSuit.DONT_CARE);

	System.out.println("cards to quad : " + handComparator.cardsToQuads(cardList));
	System.out.println("is quad : " + handComparator.isQuads(cardList));

	System.out.println("cards to flush" + handComparator.cardsToFlush(cardList));
	System.out.println("is quad : " + handComparator.isFlush(cardList));

	System.out.println("cards to trips" + handComparator.cardsToTriplets(cardList));
	System.out.println("is quad : " + handComparator.isTriplets(cardList));

	System.out.println("cards to twopair" + handComparator.cardsToTwoPair(cardList));
	System.out.println("is quad : " + handComparator.isTwoPair(cardList));

	System.out.println("cards to pair" + handComparator.cardsToPair(cardList));
	System.out.println("is pair : " + handComparator.isOnePair(cardList));

    }
}
