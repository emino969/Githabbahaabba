package Pictures;

import Cards.CardSuit;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Images
{
    private static BufferedImage imageClub, imageDiamond, imageHeart, imageSpade,
	    imageBackground, imageTable,
	    imageRedPokerChip, imageBlackPokerChip, imageGreenPokerChip;
    private static String filePath = "C:\\Users\\Emil\\IdeaProjects\\CardGame - DemoVer\\src\\Pictures\\pictures\\";
    private static String clubPic = "club.gif";
    private static String diamondPic = "diamond.png";
    private static String heartPic = "heart.gif";
    private static String spadePic = "spade.gif";
    private static String backgroundPic = "PokerBackground.jpg";
    private static String tablePic = "TableBackground.jpg";
    private static String redPokerChip = "redPokerChip.gif";
    private static String greenPokerChip = "greenPokerChip.gif";
    private static String blackPokerChip = "blackPokerChip.gif";
    private static Map<CardSuit, BufferedImage> cardTypesMap;
    private static final int CARD_IMAGE_X = 15 * 3 / 2;
    private static final int CARD_IMAGE_Y = 20 * 3 / 2;

    private Images() {
	getPictures();
	this.cardTypesMap = new HashMap<CardSuit, BufferedImage>();
	cardTypesMap.put(CardSuit.HEARTS, imageHeart);
	cardTypesMap.put(CardSuit.SPADES, imageSpade);
	cardTypesMap.put(CardSuit.DIAMONDS, imageDiamond);
	cardTypesMap.put(CardSuit.CLUBS, imageClub);
    }

    private static class ImageHandlerHolder{
	private static Images imageHandlerInstance = new Images();
    }

    public static Images getInstance()	{
	return ImageHandlerHolder.imageHandlerInstance;
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

    public static BufferedImage getImageBackground() {
	return imageBackground;
    }

    public static BufferedImage getImageRedPokerChip() {
	return imageRedPokerChip;
    }

    public static BufferedImage getImageBlackPokerChip() {
	return imageBlackPokerChip;
    }

    public static BufferedImage getImageGreenPokerChip() {
	return imageGreenPokerChip;
    }

    public static BufferedImage getTable()	{
	return imageTable;
    }

    public static BufferedImage getPicture(CardSuit ct){
	    return cardTypesMap.get(ct);
	}

    public static int getPrefferedX(CardSuit ct)	{
	switch(ct)	{
	    case DIAMONDS:
		return CARD_IMAGE_X * 3 / 2;
	    default:
		return CARD_IMAGE_X;
	}
    }

    public static int getPrefferedY(CardSuit ct)	{
	switch(ct)	{
	    case DIAMONDS:
		return CARD_IMAGE_Y * 3 / 2;
	    default:
		return CARD_IMAGE_Y;
	}
    }

    public static ImageIcon getBackgroundImageIcon()	{
	return new ImageIcon(filePath + backgroundPic);
    }
}
