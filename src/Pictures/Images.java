package Pictures;

import Cards.CardType;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Images
{
    private BufferedImage imageClub, imageDiamond, imageHeart, imageSpade, imageBackground, imageTable;
    private String filePath = "";
    private String clubPic = "club.gif";
    private String diamondPic = "diamond.png";
    private String heartPic = "heart.gif";
    private String spadePic = "spade.gif";
    private String backgroundPic = "PokerBackground.jpg";
    private String tablePic = "TableBackground.jpg";
    private String redPokerChip = "RedPokerChip.jpg";
    private String getBlackPokerChip = "GreenPokerChip.jpg";
    private String blackPokerChip = "BlackPokerChip.jpg.jpg";
    private Map<CardType, BufferedImage> cardTypesMap;
    private static final int CARD_IMAGE_X = 15 * 3 / 2;
    private static final int CARD_IMAGE_Y = 20 * 3 / 2;

    public Images() {
	getPictures();
	cardTypesMap = new HashMap<CardType, BufferedImage>();
	cardTypesMap.put(CardType.HEARTS, imageHeart);
	cardTypesMap.put(CardType.SPADES, imageSpade);
	cardTypesMap.put(CardType.DIAMONDS, imageDiamond);
	cardTypesMap.put(CardType.CLUBS, imageClub);
    }

    private void getPictures()	{
	try {
	    this.imageClub = ImageIO.read(new File(filePath + clubPic));
	    this.imageDiamond = ImageIO.read(new File(filePath + diamondPic));
	    this.imageHeart = ImageIO.read(new File(filePath + heartPic));
	    this.imageSpade = ImageIO.read(new File(filePath + spadePic));
	    this.imageBackground = ImageIO.read(new File(filePath + backgroundPic));
	    this.imageTable = ImageIO.read(new File(filePath + tablePic));
 	} catch (IOException ex) {
	    System.out.println("hejsan");
	    //ex.printStackTrace();
	}
    }

    public BufferedImage getBackground()	{
	return imageBackground;
    }

    public BufferedImage getTable()	{
	return imageTable;
    }

    public BufferedImage getPicture(CardType ct)	{
	return cardTypesMap.get(ct);
    }

    public int getPrefferedX(CardType ct)	{
	switch(ct)	{
	    case DIAMONDS:
		return CARD_IMAGE_X * 3 / 2;
	    default:
		return CARD_IMAGE_X;
	}
    }

    public int getPrefferedY(CardType ct)	{
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
