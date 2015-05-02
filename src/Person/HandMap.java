package Person;

import cards.CardList;
import java.util.ArrayList;
import java.util.List;
/**
 *
 */

public class HandMap
{
    private ArrayList<CardList> hands;
    private ArrayList<PersonState> states;

    public HandMap() {
	this.hands = new ArrayList<>();
	this.states = new ArrayList<>();
    }

    public PersonState get(CardList hand)	{
	return states.get(hands.indexOf(hand));
    }

    public CardList get(int index)	{
	return hands.get(index);
    }

    public void clear()	{
	hands.clear();
	states.clear();
    }

    public ArrayList<PersonState> getStates()	{
	return states;
    }

    public void put(CardList hand, PersonState state)	{
	if (hands.contains(hand))	{
	    states.set(hands.indexOf(hand), state);
	    hands.set(hands.indexOf(hand), hand);
	}	else	{
	    hands.add(hand);
	    states.add(state);
	}
    }

    public void remove(CardList hand)	{
	states.remove(get(hand));
	hands.remove(hand);
    }

    public List<CardList> getHands()	{
	return hands;
    }

    public boolean turnsLeft(CardList hand)	{
	for (int i = hands.indexOf(hand) + 1; i < hands.size(); i++) {
	    if (states.get(i) == PersonState.WAITING) return true;
	}
	return false;
    }

    public CardList nextHand(CardList hand)	{
	for (int i = hands.indexOf(hand) + 1; i < hands.size() + hands.indexOf(hand) + 1; i++) {
	    if (states.get(i % hands.size()) == PersonState.WAITING) return hands.get(i % hands.size());
	}
	return getBestHand();
    }

    public CardList getBestHand()	{
	CardList theBest = new CardList();
	for (CardList hand : hands)	{
	    if (getLegalHandSum(hand) <= 21 && getLegalHandSum(hand) > getLegalHandSum(theBest))	{
		theBest = hand;
	    }
	}
	return theBest;
    }

    public int getLegalHandSum(CardList hand)	{
	int numberOfAces = hand.countIntValue(14);
	int maxSum = hand.getSumAceOnTop();
	for (int i = 1; i < numberOfAces + 1; i++) {
	    if ((maxSum > 21) && (maxSum - i * 10 <= 21))	{
		maxSum -= i * 10;
	    }
	}
	return maxSum;
     }

    public CardList getLastHand()	{
	for (int i = hands.size() - 1; i >= 0; i--)	{
	    if (states.get(i) == PersonState.WAITING) return hands.get(i);
	}
	return null;
    }
}
