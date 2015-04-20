package Cards;

import CardGameExceptions.NoSuchCardException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardList extends ArrayList<Card>
{
    public Iterable<Card> getCardList() {
        return cardList;
    }

    public boolean contains(Card card)  {
        return cardList.contains(card);
    }
    private List<Card> cardList;
    private static final int ACE_INT = 11;
    private static final int TEN_TO_KING_INT = 10;

    public CardList()   {
        this.cardList = new ArrayList<Card>();
    }

    public boolean containsIntValue(Card card)	{
	for (Card c : cardList) {
	    if (c.getCardInt() == card.getCardInt()) {
		return true;
	    }
	}
	return false;
    }

    public int countCardValue(CardValue cv)	{
	int i = 0;
	for (Card c : cardList) {
	    if (c.getValue() == cv) {
		i++;
	    }
	}
	return i;
    }

    public int countIntValue(Card card)	{
	int i = 0;
	    for (Card c : cardList) {
		if (c.getCardInt() == card.getCardInt()) {
		    i++;
		}
	    }
	    return i;
    }

    public CardList getCopy(CardList cl)	{
	CardList copy = new CardList();
	for (int i = 0; i < cl.getSize(); i++) {
	    copy.addCard(cl.getCardByIndex(i));
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

    public void setCardList(List<Card> cardList){
          this.cardList = cardList;
    }

    public void createOrdinaryDeck(){
	for (CardType cardType : CardType.values()) {
	    for (CardValue cardValue : CardValue.values()) {
		if(cardValue != CardValue.BOTTOM_ACE) cardList.add(new Card(cardType, cardValue));
	    }
	}
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

    public int getSumAceOnTop()	{
	int sum = 0;
	for (int i = 0; i < this.getSize(); i++) {
		if	(this.getCardByIndex(i).getValue().equals(CardValue.TOP_ACE))	{
		    /** Ace is worth 11 when counted on top */
		    sum += ACE_INT;
		}	else if (this.getCardByIndex(i).getCardInt() > 10)	{
		    /** Accoding to blackjack rules everything excepts Ace is worth 10 */
		    sum += TEN_TO_KING_INT;
		}	else	{
		    sum += this.getCardByIndex(i).getCardInt();
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
		}	else if (this.getCardByIndex(i).getCardInt() > 10)	{
		    /** Accoding to blackjack rules everything excepts Ace is worth 10 */
		    sum += 10;
		}	else	{
		    sum += this.getCardByIndex(i).getCardInt();
		}
	    }
	return sum;
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
        }}

