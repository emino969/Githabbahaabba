package Cards;

import CardGameExceptions.NoSuchCardException;
import Pictures.Images;
import javax.swing.*;
import java.awt.*;

public class Card {
    private CardType cardType;
    private CardValue cardValue;
    private Color cardColor;
    private static final int CARD_SIZE_X = 50;
    private static final int CARD_SIZE_Y = 75;
    private static final int STRING_SPACE_FROM_MIDDLE = CARD_SIZE_X / 2 - 20;
    private static final int FONT_SPACE = 15;
    private boolean isVisible;
    private FontMetrics fm;
    private Font font;

    public Card(final CardType cardType, final CardValue cardValue) {
	this.cardType = cardType;
	this.cardValue = cardValue;
	this.cardColor = getColorByCardType();
	this.isVisible = true;
	this.font = new Font("Serif", Font.BOLD, 20);
	this.fm = null;
    }

    public Color getColorByCardType() {
	if(cardType == CardType.CLUBS || cardType == CardType.SPADES) return Color.BLACK;
	else if( cardType == CardType.HEARTS || cardType == CardType.DIAMONDS)return Color.RED;
	throw new IllegalArgumentException("There is no such cardtype as" + cardType);
    }

    public CardType getCardType() {
        return cardType;
    }

    public CardValue getValue() {
        return cardValue;
    }

    @Override public String toString() {
        return cardType + " " + cardValue;
    }

    public int getCardIntValue() throws NoSuchCardException {
        int i = 1;
        for (CardValue cardValue : CardValue.values()) {
            if(cardValue == this.cardValue){
                return i;
            }
            i ++;
        }
        throw new NoSuchCardException("There is no such Card");
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
	}	else if(value == 11)	{
	    return "J";
	}	else if(value == 12)	{
	    return "Q";
	}	else if(value == 13)	{
	    return "K";
	}	else if(value == 14)	{
	    return "A";
	}	else	{
	    return "-1";
	}
    }

    public void draw(Graphics2D g, int X, int Y, JComponent comp, Images imageHandler)	{
	final int IMAGE_X = imageHandler.getPrefferedX(cardType);
	final int IMAGE_Y = imageHandler.getPrefferedY(cardType);
	this.fm = comp.getFontMetrics(new Font("Serif", Font.BOLD, 20));

	if(isVisible) {
	    g.setColor(Color.WHITE);
	    g.fillRoundRect(X, Y, CARD_SIZE_X, CARD_SIZE_Y, 10, 10);

	    g.setColor(Color.BLACK);
	    g.drawRoundRect(X, Y, CARD_SIZE_X, CARD_SIZE_Y, 10, 10);

	    g.setFont(font);
            g.setColor(cardColor);

	    try{
		g.drawString(getSymbolFromInt(getCardIntValue()),
			     X + CARD_SIZE_X / 2 - STRING_SPACE_FROM_MIDDLE - FONT_SPACE,
			     Y + 15);

		g.drawString(getSymbolFromInt(getCardIntValue()),
			     X + CARD_SIZE_X / 2 + STRING_SPACE_FROM_MIDDLE + FONT_SPACE - getStringWidth(getSymbolFromInt(getCardIntValue())),
			     Y + CARD_SIZE_Y - 5);

		g.drawImage(imageHandler.getPicture(cardType),
			    X + CARD_SIZE_X / 2 - IMAGE_X / 2,
			    Y + CARD_SIZE_Y / 2 - 13,
			    IMAGE_X,
			    IMAGE_Y,
			    comp);

            }catch (NoSuchCardException e) {
		System.out.println("There is no such card!");
	    }

	    Font newFont = new Font("Serif", Font.BOLD, 15);
	    g.setFont(newFont);

        } else{
	    g.setColor(Color.BLUE);
	    g.fillRoundRect(X, Y, CARD_SIZE_X, CARD_SIZE_Y, 10, 10);

	    g.setColor(Color.BLACK);
	    g.drawRoundRect(X, Y, CARD_SIZE_X, CARD_SIZE_Y, 10, 10);
            }
	}

    public int getStringWidth(String string)	{
	int pixelLength = 0;
	for (int i = 0; i < string.length(); i++) {
	    pixelLength += fm.charWidth(string.charAt(i));
	}
	return pixelLength;
    }
    }
