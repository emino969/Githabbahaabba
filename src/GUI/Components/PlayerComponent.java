package GUI.Components;

import Cards.CardList;
import Person.Person;
import Pictures.Images;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class PlayerComponent extends JComponent
{
    private Person person;
    private int xPos,yPos;
    private int personRadius = 50;
    private final static int cardSpaceX = 25; //The space between the cards on the table if there is more than 1 card per player
    private final static int cardSpaceY = 15;
    private final static int spaceBetweenHands = 5;
    private final static int PERSON_RECTANGLE = 10;
    private final static int RECTANGLE_BAR = 2;
    private final static int BET_CIRCLE_RADIUS = 50;
    private Images imageHandler;
    private FontMetrics fm;
    private TableSeat ts;
    private int width, height;
    private String name;

    public PlayerComponent(final Person person, Images imageHandler) {
	this.person = person;
	this.imageHandler = imageHandler;
	this.fm = getFontMetrics(new Font("Serif", Font.BOLD, 20));
	this.name = person.getName();
	this.width = getMinimumWidth() + PERSON_RECTANGLE * 3;
	this.height = fm.getHeight() + PERSON_RECTANGLE * 4;
    }

    public void drawPlayer(final Graphics g, int x, int y, TableSeat ts, final int tableX, final int tableY) {
	this.ts = ts;
        y -= personRadius;

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
	g.setFont(new Font("Serif", Font.BOLD, 20));
	g.fillRoundRect(x, y, width, height, 10, 10);

	g.setColor(Color.BLACK);
	g.fillRoundRect(x + RECTANGLE_BAR / 2, y + RECTANGLE_BAR / 2, width - RECTANGLE_BAR, height - RECTANGLE_BAR, 10, 10);

	g.setColor(Color.WHITE);
	g.drawString(name, x + PERSON_RECTANGLE, y + fm.getHeight());
	g.drawString(String.valueOf(person.getPot().getAmount()) + '$', x + PERSON_RECTANGLE, y + 2 * fm.getHeight());

	drawPlayerBetCircle(g, x, y);
	drawPlayerCards(g, x, y); //A generalized solution for painting the cards
    }

    public int getStringWidth(CharSequence string)	{
	int pixelLength = 0;
	for (int i = 0; i < string.length(); i++) {
	    pixelLength += fm.charWidth(string.charAt(i));
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
	CardList currentHand = person.getHands().get(0);
	drawHand(currentHand, g, recommendedX(ts, x), recommendedY(ts, y));
    }

    private int recommendedX(TableSeat ts, int x)	{
	switch(ts)	{
	    case FIRST_SIDE:
		return x;
	    case SECOND_SIDE:
		return x - 40;
	    case THIRD_SIDE:
		return x + 20;
	    default:
		return -1;
	}
    }

    private int recommendedY(TableSeat ts, int y)	{
	switch(ts)	{
	    case FIRST_SIDE:
		return y;
	    case SECOND_SIDE:
		return y - 10;
	    case THIRD_SIDE:
		return y;
	    default:
		return -1;
	}
    }

    private void drawHand(CardList cl, final Graphics g, int x, int y)	{
	for (int i = 0; i < cl.getSize(); i++) {
	    if	(ts.equals(TableSeat.FIRST_SIDE)) {
		cl.getCardByIndex(i).draw((Graphics2D) g, x + i * cardSpaceX, y + 80, this, imageHandler);
	    }	else if(ts.equals(TableSeat.SECOND_SIDE))	{
		cl.getCardByIndex(i).draw((Graphics2D) g, x - i * cardSpaceX - 25, y, this, imageHandler);
	    }	else	{
		cl.getCardByIndex(i).draw((Graphics2D) g, x - i * cardSpaceX, y - 90, this, imageHandler);
	    }
	}
    }
    private void drawPlayerBetCircle(final Graphics g, int x ,int y){
	g.setColor(Color.YELLOW);
	if(ts.equals(TableSeat.FIRST_SIDE )) {
	    g.drawOval(x - 120 + 50,y + 100, BET_CIRCLE_RADIUS, BET_CIRCLE_RADIUS);
	    drawChips(g, x - 120 + 50, y + 100);
	}else if(ts.equals(TableSeat.SECOND_SIDE)){
	    g.drawOval(x - 100 + 20, y - 80, BET_CIRCLE_RADIUS, BET_CIRCLE_RADIUS);
	    drawChips(g, x - 100 + 20, y - 80);
	}else{
	    g.drawOval(x + 20 + 70, y - 100 + 20, BET_CIRCLE_RADIUS, BET_CIRCLE_RADIUS);
	    drawChips(g, x + 20 + 70, y - 100 + 20);
	}
    }

    private void drawChips(Graphics g, int x, int y) {
	int money = person.getLastBet() + person.getBetHolder();
	Image image = imageHandler.getImageBlackPokerChip();
	int i = 0;
	while(0 < money){
	    if(75 <= money){
		image = imageHandler.getImageBlackPokerChip();
		money -= 75;
	    }else if(50 <= money) {
		image = imageHandler.getImageRedPokerChip();
		money -= 50;
	    }else if(25 <= money){
		image = imageHandler.getImageGreenPokerChip();
		money -= 25;
	    }
	    g.drawImage(image, x + 5 + 10 * (i % 4), y + 5 + 10 * (i / 4), 30, 30, this);
	    i++;
	}
    }
}
