package gui.components;

import cards.CardList;
import Person.Person;
import pictures.Images;

import javax.swing.*;
import java.awt.*;
/**
 *
 */
public class PlayerComponent extends JComponent
{
    private Person person;
    private static final int PERSON_RADIUS = 50;
    private final static int CARD_SPACE = 25; //The space between the cards on the table if there is more than 1 card per player
    private final static int PERSON_RECTANGLE = 10;
    private final static int FONT_SIZE = 20;
    private final static int RECTANGLE_BAR = 2;
    private final static int BET_CIRCLE_RADIUS = 50;
    private final static int BLACK_CHIP_VALUE = 75;
    private final static int BORDER_THICKNESS = 10;
    private final static int RED_CHIP_VALUE = 50;
    private final static int GREEN_CHIP_VALUE = 25;
    private Images imageHandler;
    private FontMetrics fm;
    private TableSeat ts = null;
    private int width, height;
    private String name;
    private final static int CHIP_RADIUS = 30;

    public PlayerComponent(final Person person, Images imageHandler) {
	this.person = person;
	this.imageHandler = imageHandler;
	this.fm = getFontMetrics(new Font("Serif", Font.BOLD, FONT_SIZE));
	this.name = person.getName();
	this.width = getMinimumWidth() + PERSON_RECTANGLE * 3;
	this.height = fm.getHeight() + PERSON_RECTANGLE * 4;
    }

    public void drawPlayer(final Graphics g, int x, int y, TableSeat ts) {
	this.ts = ts;
        y -= PERSON_RADIUS;

	switch(person.getState()){
	    case LOSER:
		g.setColor(Color.RED);
		break;
	    case TURN:
		g.setColor(Color.YELLOW);
		break;
	    case WINNER:
		g.setColor(Color.GREEN);
		break;
	    default:
		g.setColor(Color.BLUE);
		break;
	}

	//MAKE BOXES WITH INFO
	g.setFont(new Font("Serif", Font.BOLD, FONT_SIZE));
	g.fillRoundRect(x, y, width, height, BORDER_THICKNESS, BORDER_THICKNESS);

	g.setColor(Color.BLACK);
	g.fillRoundRect(x + RECTANGLE_BAR / 2, y + RECTANGLE_BAR / 2, width - RECTANGLE_BAR, height - RECTANGLE_BAR, BORDER_THICKNESS, BORDER_THICKNESS);

	g.setColor(Color.WHITE);
	g.drawString(name, x + PERSON_RECTANGLE, y + fm.getHeight());
	g.drawString(String.valueOf(person.getPot().getAmount()) + '$', x + PERSON_RECTANGLE, y + 2 * fm.getHeight());

	drawPlayerBetCircle(g, x, y);
	drawPlayerCards(g, x, y); //A generalized solution for painting the cards
    }

    public int getStringWidth(CharSequence anyString)	{
	int pixelLength = 0;
	for (int i = 0; i < anyString.length(); i++) {
	    pixelLength += fm.charWidth(anyString.charAt(i));
	}
	return pixelLength;
    }

    public int getMinimumWidth()	{
	int nameLength = getStringWidth(person.getName());
	int potLength = getStringWidth(String.valueOf(person.getPot().getAmount()) + '$');
	if	(nameLength >= potLength)	{
	    return nameLength;
	}	else	{
	    return potLength;
	}
    }

    public int getHeight()	{
	return height;
    }

    public int getWidth()	{
	return width;
    }

    private void drawPlayerCards(final Graphics g, int x, int y)	{
	for (int i = 0; i < person.getHands().size(); i++) {
	    drawHand(person.getHandByIndex(i), g, recommendedX(ts, x), recommendedY(ts, y, i));
	}
    }

    private int recommendedX(TableSeat ts, int x)	{
	switch (ts) {
	    case FIRST_SIDE:
		return x;
	    case SECOND_SIDE:
		return x - PERSON_RADIUS;
	    case THIRD_SIDE:
		return x + CARD_SPACE;
	    default:
		return -1;
	}
    }

    private int recommendedY(TableSeat ts, int y, int indexCardList)	{
	switch (ts) {
	    case FIRST_SIDE:
		return y + 2*PERSON_RADIUS * indexCardList;
	    case SECOND_SIDE:
		return y - 10 - 2*PERSON_RADIUS * indexCardList;
	    case THIRD_SIDE:
		return y - 2*PERSON_RADIUS * indexCardList;
	    default:
		return -1;
	}
    }

    private void drawHand(CardList cl, final Graphics g, int x, int y)	{
	for (int i = 0; i < cl.getSize(); i++) {
	    if	(ts.equals(TableSeat.FIRST_SIDE)) {
		cl.getCardByIndex(i).draw((Graphics2D) g, x + i * CARD_SPACE, y + 2*PERSON_RADIUS, this, imageHandler);
	    }	else if(ts.equals(TableSeat.SECOND_SIDE))	{
		cl.getCardByIndex(i).draw((Graphics2D) g, x - i * CARD_SPACE - CHIP_RADIUS, y, this, imageHandler);
	    }	else	{
		cl.getCardByIndex(i).draw((Graphics2D) g, x - i * CARD_SPACE, y - 2*PERSON_RADIUS, this, imageHandler);
	    }
	}
    }
    private void drawPlayerBetCircle(final Graphics g, int x ,int y){
	g.setColor(Color.YELLOW);
	if(ts.equals(TableSeat.FIRST_SIDE )) {
	    g.drawOval(x - 2*PERSON_RADIUS + PERSON_RADIUS,y + PERSON_RADIUS*2, BET_CIRCLE_RADIUS, BET_CIRCLE_RADIUS);
	    drawChips(g, x - 2*PERSON_RADIUS + PERSON_RADIUS, y + PERSON_RADIUS*2);
	}else if(ts.equals(TableSeat.SECOND_SIDE)){
	    g.drawOval(x - PERSON_RADIUS*2 + CARD_SPACE, y - 2*PERSON_RADIUS, BET_CIRCLE_RADIUS, BET_CIRCLE_RADIUS);
	    drawChips(g, x - PERSON_RADIUS*2 + CARD_SPACE, y - 2*PERSON_RADIUS);
	}else{
	    g.drawOval(x + CARD_SPACE + 70, y - PERSON_RADIUS*2 + CARD_SPACE, BET_CIRCLE_RADIUS, BET_CIRCLE_RADIUS);
	    drawChips(g, x + CARD_SPACE + 70, y - PERSON_RADIUS*2 + CARD_SPACE);
	}
    }

    private void drawChips(Graphics g, int x, int y) {
	int money = person.getLastBet() + person.getBetHolder();
	Image image = imageHandler.getImageBlackPokerChip();
	int i = 0;
	while(0 < money){
	    if(BLACK_CHIP_VALUE <= money){
		image = imageHandler.getImageBlackPokerChip();
		money -= BLACK_CHIP_VALUE;
	    }else if(PERSON_RADIUS <= money) {
		image = imageHandler.getImageRedPokerChip();
		money -= PERSON_RADIUS;
	    }else if(GREEN_CHIP_VALUE <= money){
		image = imageHandler.getImageGreenPokerChip();
		money -= GREEN_CHIP_VALUE;
	    }
	    g.drawImage(image, x + 5 + 10 * (i % 4), y + 5 + 10 * (i / 4), CHIP_RADIUS, CHIP_RADIUS, this);
	    i++;
	}
    }
}
