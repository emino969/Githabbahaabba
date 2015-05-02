package card;

import Pictures.Images;

import javax.swing.*;
import java.awt.*;

public class Card {
    private CardValue cardValue;
    private Color cardColor;
    private static final int CARD_SIZE_X = 50;
    private static final int CARD_SIZE_Y = 75;
    private static final int STRING_SPACE_FROM_MIDDLE = CARD_SIZE_X / 2 - 20;
    private static final int FONT_SPACE = 15;
    private static final int FONT_SIZE = 20;
    private static final int JACK_INT = 11;
    private static final int QUEEN_INT = 12;
    private static final int KING_INT = 13;
    private static final int ACE_INT = 14;
    private static final int CARD_SPACE_TOP = 15;
    private static final int CARD_SPACE_PIC = 13;
    private boolean isVisible;
    private FontMetrics fm;
    private Font font;
    private CardSuit cardSuit;

    public Card(final CardSuit cardSuit, final CardValue cardValue) {
	this.cardSuit = cardSuit;
	this.cardValue = cardValue;
	this.cardColor = getColorByCardType();
	this.isVisible = true;
	this.font = new Font("Serif", Font.BOLD, FONT_SIZE);
	this.fm = null;
    }

    public Color getColorByCardType() {
	if(cardSuit == CardSuit.DONT_CARE) return Color.ORANGE;
	if (cardSuit == CardSuit.CLUBS || cardSuit == CardSuit.SPADES) return Color.BLACK;
	else if (cardSuit == CardSuit.HEARTS || cardSuit == CardSuit.DIAMONDS) return Color.RED;
	throw new IllegalArgumentException("There is no such cardtype as" + cardSuit);
    }


    public CardValue getValue() {
        return cardValue;
    }

    @Override public String toString() {
        return cardSuit + " " + cardValue;
    }

   /* private int getCardIntValue() throws NoSuchCardException {
        int i = 1;
        for (CardValue cardValue : CardValue.values()) {
            if(cardValue == this.cardValue){
                return i;
            }
            i ++;
        }
        throw new NoSuchCardException("There is no such Card");
    }*/

   /* public int getCardInt()	{
	try	{
	    return this.getCardIntValue();
	}	catch(NoSuchCardException e)	{
	    e.printStackTrace();
	    return -1;
	}
    }*/
    public int getCardIntValue() {
	switch (cardValue) {
	    case BOTTOM_ACE:
		return 1;
	    case TWO:
		return 2;
	    case THREE:
		return 3;
	    case FOUR:
		return 4;
	    case FIVE:
		return 5;
	    case SIX:
		return 6;
	    case SEVEN:
		return 7;
	    case EIGHT:
		return 8;
	    case NINE:
		return 9;
	    case TEN:
		return 10;
	    case JACK:
		return JACK_INT;
	    case QUEEN:
		return QUEEN_INT;
	    case KING:
		return KING_INT;
	    case TOP_ACE:
		return ACE_INT;
	    default:
		return -1;


	}

    }

    public void setNonVisible()	{
	isVisible = false;
    }

    public void setVisible()	{
	isVisible = true;
    }

    public boolean isVisible()	{
	return isVisible;
    }

    private String getSymbolFromInt(int value)	{
	if	(value <= 10)	{
	    return String.valueOf(value);
	}	else if(value == JACK_INT)	{
	    return "J";
	}	else if(value == QUEEN_INT)	{
	    return "Q";
	}	else if(value == KING_INT)	{
	    return "K";
	}	else if(value == ACE_INT)	{
	    return "A";
	}	else	{
	    System.out.println(value);
	    return "-1";
	}
    }

    public void draw(Graphics2D g, int x, int y, JComponent comp, Images imageHandler)	{
	final int imageX = imageHandler.getPrefferedX(cardSuit);
	final int imageY = imageHandler.getPrefferedY(cardSuit);
	this.fm = comp.getFontMetrics(new Font("Serif", Font.BOLD, FONT_SIZE));

	if(isVisible) {
	    g.setColor(Color.WHITE);
	    g.fillRoundRect(x, y, CARD_SIZE_X, CARD_SIZE_Y, 10, 10);

	    g.setColor(Color.BLACK);
	    g.drawRoundRect(x, y, CARD_SIZE_X, CARD_SIZE_Y, 10, 10);

	    g.setFont(font);
            g.setColor(cardColor);

	    g.drawString(getSymbolFromInt(getCardIntValue()),
			 x + CARD_SIZE_X / 2 - STRING_SPACE_FROM_MIDDLE - FONT_SPACE,
			 y + CARD_SPACE_TOP);

	    g.drawString(getSymbolFromInt(getCardIntValue()),
			 x + CARD_SIZE_X / 2 + STRING_SPACE_FROM_MIDDLE + FONT_SPACE - getStringWidth(getSymbolFromInt(getCardIntValue())),
			 y + CARD_SIZE_Y - 5);

	    g.drawImage(imageHandler.getPicture(cardSuit),
			x + CARD_SIZE_X / 2 - imageX / 2,
			y + CARD_SIZE_Y / 2 - CARD_SPACE_PIC,
			imageX,
			imageY,
			comp);

	    Font newFont = new Font("Serif", Font.BOLD, FONT_SIZE);
	    g.setFont(newFont);

        } else{
	    g.setColor(Color.BLUE);
	    g.fillRoundRect(x, y, CARD_SIZE_X, CARD_SIZE_Y, 10, 10);

	    g.setColor(Color.BLACK);
	    g.drawRoundRect(x, y, CARD_SIZE_X, CARD_SIZE_Y, 10, 10);
            }
	}

    public int getStringWidth(CharSequence anyString)	{
	int pixelLength = 0;
	for (int i = 0; i < anyString.length(); i++) {
	    pixelLength += fm.charWidth(anyString.charAt(i));
	}
	return pixelLength;
    }

    public CardSuit getCardSuit() {
	return cardSuit;
    }
}
