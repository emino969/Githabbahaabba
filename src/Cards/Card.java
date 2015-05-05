package Cards;

import CardGameExceptions.NoSuchCardException;
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
    private static final int KNIGHT_INT = 11;
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

    public int getCardIntValue(){
	try{
	    return getCardInt();
	}catch(NoSuchCardException e){
	    e.printStackTrace();
	}
	return -1;
    }

    private int getCardInt() throws NoSuchCardException{
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
		return 11;
	    case QUEEN:
		return 12;
	    case KING:
		return 13;
	    case TOP_ACE:
		return 14;
	    case DONT_CARE:
		return 0;
	    default:
		throw new NoSuchCardException("there is no cardtype with this cardint");
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
	}	else if(value == KNIGHT_INT)	{
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

    public void draw(Graphics2D g, int X, int Y, JComponent comp)	{
	final int IMAGE_X = Images.getPrefferedX(cardSuit);
	final int IMAGE_Y = Images.getPrefferedY(cardSuit);
	this.fm = comp.getFontMetrics(new Font("Serif", Font.BOLD, FONT_SIZE));

	if(isVisible) {
	    g.setColor(Color.WHITE);
	    g.fillRoundRect(X, Y, CARD_SIZE_X, CARD_SIZE_Y, 10, 10);

	    g.setColor(Color.BLACK);
	    g.drawRoundRect(X, Y, CARD_SIZE_X, CARD_SIZE_Y, 10, 10);

	    g.setFont(font);
            g.setColor(cardColor);

	    g.drawString(getSymbolFromInt(getCardIntValue()),
			 X + CARD_SIZE_X / 2 - STRING_SPACE_FROM_MIDDLE - FONT_SPACE,
			 Y + CARD_SPACE_TOP);

	    g.drawString(getSymbolFromInt(getCardIntValue()),
			 X + CARD_SIZE_X / 2 + STRING_SPACE_FROM_MIDDLE + FONT_SPACE - getStringWidth(getSymbolFromInt(getCardIntValue())),
			 Y + CARD_SIZE_Y - 5);

	    g.drawImage(Images.getPicture(cardSuit),
			X + CARD_SIZE_X / 2 - IMAGE_X / 2,
			Y + CARD_SIZE_Y / 2 - CARD_SPACE_PIC,
			IMAGE_X,
			IMAGE_Y,
			comp);

	    Font newFont = new Font("Serif", Font.BOLD, FONT_SIZE);
	    g.setFont(newFont);

        } else{
	    g.setColor(Color.BLUE);
	    g.fillRoundRect(X, Y, CARD_SIZE_X, CARD_SIZE_Y, 10, 10);

	    g.setColor(Color.BLACK);
	    g.drawRoundRect(X, Y, CARD_SIZE_X, CARD_SIZE_Y, 10, 10);
            }
	}

    public int getStringWidth(CharSequence string)	{
	int pixelLength = 0;
	for (int i = 0; i < string.length(); i++) {
	    pixelLength += fm.charWidth(string.charAt(i));
	}
	return pixelLength;
    }

    public CardSuit getCardSuit() {
	return cardSuit;
    }
}
