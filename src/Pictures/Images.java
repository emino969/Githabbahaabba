package Pictures;

import Cards.CardSuit;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Images
{
    private BufferedImage imageClub = null, imageDiamond = null, imageHeart = null, imageSpade = null,
	    imageBackground = null, imageTable = null,
	    imageRedPokerChip = null, imageBlackPokerChip = null, imageGreenPokerChip = null;
    private String filePath = "";
    private String clubPic = "club.gif";
    private String diamondPic = "diamond.png";
    private String heartPic = "heart.gif";
    private String spadePic = "spade.gif";
    private String backgroundPic = "PokerBackground.jpg";
    private String tablePic = "TableBackground.jpg";
    private String redPokerChip = "redPokerChip.gif";
    private String greenPokerChip = "greenPokerChip.gif";
    private String blackPokerChip = "blackPokerChip.gif";
    private Map<CardSuit, BufferedImage> cardTypesMap;
    private static final int CARD_IMAGE_X = 15 * 3 / 2;
    private static final int CARD_IMAGE_Y = 20 * 3 / 2;

    public Images() {
	getPictures();
	this.cardTypesMap = new HashMap<>();
	cardTypesMap.put(CardSuit.HEARTS, imageHeart);
	cardTypesMap.put(CardSuit.SPADES, imageSpade);
	cardTypesMap.put(CardSuit.DIAMONDS, imageDiamond);
	cardTypesMap.put(CardSuit.CLUBS, imageClub);
    }

    private void getPictures()	{
	try {
	    this.imageClub = ImageIO.read(new File(filePath + clubPic));
	    this.imageDiamond = ImageIO.read(new File(filePath + diamondPic));
	    this.imageHeart = ImageIO.read(new File(filePath + heartPic));
	    this.imageSpade = ImageIO.read(new File(filePath + spadePic));
	    this.imageBackground = ImageIO.read(new File(filePath + backgroundPic));
	    this.imageTable = ImageIO.read(new File(filePath + tablePic));
	    this.imageRedPokerChip = ImageIO.read(new File(filePath + redPokerChip));
	    this.imageBlackPokerChip = ImageIO.read(new File(filePath + blackPokerChip));
	    this.imageGreenPokerChip = ImageIO.read(new File(filePath + greenPokerChip));
 	} catch (IOException ex) {
	    System.out.println("CardPicture couldn't be read properly");
	}
    }

    public BufferedImage getImageBackground() {
	return imageBackground;
    }

    public BufferedImage getImageRedPokerChip() {
	return imageRedPokerChip;
    }

    public BufferedImage getImageBlackPokerChip() {
	return imageBlackPokerChip;
    }

    public BufferedImage getImageGreenPokerChip() {
	return imageGreenPokerChip;
    }


    public BufferedImage getTable()	{
	return imageTable;
    }

     public BufferedImage getPicture(CardSuit ct){
	    return cardTypesMap.get(ct);
	}

    static public int getPrefferedX(CardSuit ct)	{
	switch(ct)	{
	    case DIAMONDS:
		return CARD_IMAGE_X * 3 / 2;
	    default:
		return CARD_IMAGE_X;
	}
    }

    static public int getPrefferedY(CardSuit ct)	{
	switch(ct)	{
	    case DIAMONDS:
		return CARD_IMAGE_Y * 3 / 2;
	    default:
		return CARD_IMAGE_Y;
	}
    }

    public ImageIcon getBackgroundImageIcon()	{
	return new ImageIcon(filePath + backgroundPic);
    }
}
