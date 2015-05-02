package cards;

import java.util.ArrayList;
import java.util.Collections;

public class CardList
{
    public ArrayList<Card> getCardList() {
        return cardList;
    }

    private ArrayList<Card> cardList;
    private static final int ACE_INT = 11;
    private static final int TEN_TO_KING_INT = 10;
    private static final int BLACKJACK_NUMBER = 21;

    public CardList()   {
        this.cardList = new ArrayList<>();
    }

    public int countIntValue(int value)	{
	int i = 0;
	for (Card card : cardList) {
	    if (card.getCardIntValue() == value) {
		i++;
	    }
	}
	return i;
    }

    public int getLegalHandSum()	{
	int numberOfAces = countIntValue(ACE_INT);
	int maxSum = getSumAceOnTop();
	for (int i = 1; i < numberOfAces + 1; i++) {
	    if ((maxSum > BLACKJACK_NUMBER) && (maxSum - i * 10 <= BLACKJACK_NUMBER))	{
		maxSum -= i * 10;
	    }
	}
	return maxSum;
     }

    public int countCardValue(CardValue cv)	{
	int i = 0;
	for (Card card : cardList) {
	    if (card.getValue().equals(cv)) {
		i++;
	    }
	}
	return i;
    }

    public int countIntValue(Card card2)	{
	int i = 0;
	    for (Card card1 : cardList) {
		if (card1.getCardIntValue() == card2.getCardIntValue()) {
		    i++;
		}
	    }
	    return i;
    }

    public CardList getCopy()	{
	CardList copy = new CardList();
	for (int i = 0; i < this.getSize(); i++) {
	    copy.addCard(this.getCardByIndex(i));
	}
	return copy;
    }

    public boolean isEmpty()    {
        return cardList.isEmpty();
    }

    public void removeCard(Card card)    {
        cardList.remove(card);
    }

    public void addCard(Card card)  {
	cardList.add(card);
    }

    public void setCardsVisible()	{
	for	(Card card : cardList)	{
	    card.setVisible();
	}
    }

    @Override public boolean equals(final Object o) {
	if (this == o) return true;
	if (!(o instanceof CardList)) return false;
	if (!super.equals(o)) return false;

	final CardList cards = (CardList) o;

	if (cardList != null ? !cardList.equals(cards.cardList) : cards.cardList != null) return false;

	return true;
    }

    @Override public int hashCode() {
	int result = super.hashCode();
	result = 31 * result + (cardList != null ? cardList.hashCode() : 0);
	return result;
    }

    public void addHiddenCard(Card card)	{
	card.setNonVisible();
	cardList.add(card);
    }

    public boolean isAllCardsVisible()	{
	for	(Card card : cardList)	{
	    if	(!card.isVisible())	{
		return false;
	    }
	}
	return true;
    }

    public void clearCardList() {
        cardList.clear();
    }

    public int getSize()    {
        return cardList.size();
    }

    public Card getCard(int index)	{
	return cardList.get(index);
    }

    public void createOrdinaryDeck(){
	for (CardSuit cardType : CardSuit.values()) {
	    if(cardType != CardSuit.DONT_CARE) {
		for (CardValue cardValue : CardValue.values()) {
		    if (cardValue != CardValue.BOTTOM_ACE && cardValue != CardValue.DONT_CARE) cardList.add(new Card(cardType, cardValue));
		}
	    }
	}
	shuffleDeck();
	shuffleDeck();
	shuffleDeck();
	shuffleDeck();
	shuffleDeck();
    }
    public int countSuit(CardSuit suit){
	int cardCount = 0;
	for (Card card : cardList) {
	    if (card.getCardSuit().equals(suit)) cardCount++;
	}

	return cardCount;
    }

    public void createBlackJackDeck(int nDecks)	{
	for (int i = 0; i < nDecks; i++) {
	    createOrdinaryDeck();
	}
    }

    public void addCardList(CardList cl)	{
	for (int i = 0; i < cl.getSize(); i++) {
	    cardList.add(cl.getCard(i));
	}
    }

    public void shuffleDeck()   {
        Collections.shuffle(cardList);
    }

    public void printDeck() {
        for (Card card : cardList)  {
            System.out.println(card);
        }
    }

    public Card popCard()   {//Named popCard because it also removes the card from the deck
        Card card = cardList.get(0);
        removeCard(card);
        return card;
    }

    public Card getCardByIndex(int index)	{
	return cardList.get(index);
    }



    @Override public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Card card : cardList) {
            stringBuilder.append(card).append(" ");
        }
        return stringBuilder.toString();    }

    public boolean containsCardValue(final CardValue cardValue) {
            for (Card card : cardList) {
                if(card.getValue() == cardValue) return true;
            }
            return false;
        }

    public void addNCards(final Card cardOne, final int n) {
	for (int i = 0; i< n; i++) {
	    addCard(cardOne);
	}
    }
    public int getSumAceOnTop()	{
	int sum = 0;
	for (int i = 0; i < this.getSize(); i++) {
		if	(this.getCardByIndex(i).getValue().equals(CardValue.TOP_ACE))	{
		    /** Ace is worth 11 when counted on top */
		    sum += ACE_INT;
		}	else if (this.getCardByIndex(i).getCardIntValue() > 10)	{
		    /** Accoding to blackjack rules everything excepts Ace is worth 10 */
		    sum += TEN_TO_KING_INT;
		}	else	{
		    sum += this.getCardByIndex(i).getCardIntValue();
		}
	}
	return sum;
    }

    public int getSumAceOnBottom()	{ //For BlackJack when Ace can be both on top and bottom
	int sum = 0;
	for (int i = 0; i < this.getSize(); i++) {
		if	(this.getCardByIndex(i).getValue().equals(CardValue.TOP_ACE))	{
		    /** If Ace is counted on bottom */
		    sum += 1;
		}	else if (this.getCardByIndex(i).getCardIntValue() > 10)	{
		    /** Accoding to blackjack rules everything excepts Ace is worth 10 */
		    sum += 10;
		}	else	{
		    sum += this.getCardByIndex(i).getCardIntValue();
		}
	    }
	return sum;
    }
}

