package GUI.Components;

import Person.Dealer;
import Pictures.Images;

import javax.swing.*;
import java.awt.*;

import static java.awt.Color.BLACK;

public class DealerComponent extends JComponent
{
    private Dealer dealer;
    private final static int PERSON_RADIUS = 50;
    private final static int CARD_SPACE_X = 25; //The space between the cards on the table if there is more than 1 card per player
    // --Commented out by Inspection (2015-04-16 16:36):private final static int CARD_SPACE_Y = 15;
    private final static int PERSON_RECTANGLE = 10;
    private final static int RECTANGLE_BAR = 2;
    private final static int FONT_SIZE = 20;
    private int width, height;
    private String name;
    private Images imageHandler;
    private FontMetrics fm;

    public DealerComponent(Dealer dealer, Images imageHandler) {
	this.dealer = dealer;
	this.imageHandler = imageHandler;
	this.fm = getFontMetrics(new Font("Serif", Font.BOLD, FONT_SIZE));
	this.name = dealer.getName();
	this.width = getMinimumWidth() + PERSON_RECTANGLE * 3;
	this.height = fm.getHeight() + PERSON_RECTANGLE * 4;
    }


    public void drawDealer(final Graphics g, int x,  int y) {
        //g.setColor(Color.BLUE);
        drawDeck(g, x, y);
        drawPot(g, x, y);

	//MAKE BOXES WITH INFO
	if	(dealer.hasTurn())	{
	    g.setColor(Color.YELLOW);
	}	else	{
	    g.setColor(Color.BLUE);
	}

	g.setFont(new Font("Serif", Font.BOLD, FONT_SIZE));
	g.fillRoundRect(x - width, y, width, height, 10, 10);

	g.setColor(BLACK);
	g.fillRoundRect(x + RECTANGLE_BAR / 2 - width, y + RECTANGLE_BAR / 2,
			width - RECTANGLE_BAR, height - RECTANGLE_BAR, 10, 10);

	g.setColor(Color.WHITE);
	g.drawString(name, x + PERSON_RECTANGLE - width, y + fm.getHeight());
	g.drawString(String.valueOf(dealer.getPot().getAmount()) + '$', x + PERSON_RECTANGLE - width, y + 2 * fm.getHeight());

        //g.fillOval(x, y, PERSON_RADIUS, PERSON_RADIUS);
        //g.setColor(BLACK);
        //g.drawString(dealer.getName(), x, y);
       // addGolfCap(g ,x ,y); // this is essential to functionality of the dealer

    }
// --Commented out by Inspection START (2015-04-16 16:36):
//    private void addGolfCap(Graphics g, int x, int y){
//        g.setColor(Color.BLUE);
//        g.fillRect(x + PERSON_RADIUS- 10, y, PERSON_RADIUS/4, PERSON_RADIUS);
//    }
// --Commented out by Inspection STOP (2015-04-16 16:36)

    private void drawDeck(Graphics g, final int x, final int y) {
        g.setColor(Color.RED);
	drawDealerCards(g, x, y); //A generalized solution for painting the cards
        //g.fillRect(x + 50, y + 50, 20, 10); // deck is lightly beside Dealer, has size W/H 20,10
    }
    private void drawPot(Graphics g, final int x, final int y){
        g.setColor(BLACK);
        //g.drawString("Pot'o'gold: " + dealer.getTablePot().getAmount() + "$", x + 2* PERSON_RADIUS, y + 2*PERSON_RADIUS);
	drawDealerPot(g, x, y);
    }

    private void drawDealerPot(final Graphics g, final int x, final int y) {
	g.setColor(Color.RED);
	g.drawRect(x + 2* PERSON_RADIUS, y + 2*PERSON_RADIUS, 200, 100);
	drawChips(g, x + 2* PERSON_RADIUS + 5, y + 2*PERSON_RADIUS + 5);
    }

    private void drawChips(final Graphics g, int x, int y) {
	int money = dealer.getPot().getAmount();
	Image image;
	int row = 0;
	int chipRowCount = 0;
	while (0 < money){
	    image = getChipType(row);
	    g.drawImage(image, x + chipRowCount* 10, y + row*30, 30, 30, this);
	    money -= (25+ 25*row);
	    chipRowCount ++ ;
	    if(chipRowCount == 10){
		chipRowCount = 0;
		row ++;
	    }
	}
     }

    private Image getChipType(final int rows) {
	Image image;
	switch (rows){
	    case 0:
		image = imageHandler.getImageBlackPokerChip();
		break;
	    case 1:
		image = imageHandler.getImageRedPokerChip();
		break;
	    case 2:
		image = imageHandler.getImageGreenPokerChip();
		break;
	    default:
		image = imageHandler.getImageBlackPokerChip();
		break;
	}
	return image;
    }


    private void drawDealerCards(final Graphics g, int x, int y)	{
	for (int i = 0; i < dealer.getHand().getSize(); i++) {
	    dealer.getHand().getCardByIndex(i).draw((Graphics2D) g, x + i * CARD_SPACE_X + 50, y, this, imageHandler);
	}
    }

    public int getStringWidth(CharSequence string)	{
	int pixelLength = 0;
	for (int i = 0; i < string.length(); i++) {
	    pixelLength += fm.charWidth(string.charAt(i));
	}
	return pixelLength;
    }

    public int getMinimumWidth()	{
	int nameLength = getStringWidth(dealer.getName());
	int potLength = getStringWidth(String.valueOf(dealer.getPot().getAmount()) + '$');
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
}
