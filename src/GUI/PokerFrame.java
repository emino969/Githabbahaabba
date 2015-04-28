package GUI;

import Money.Pot;
import Person.Player;
import Pictures.Images;
import PokerRules.AbstractGame;

import javax.swing.*;
import java.awt.*;

public class PokerFrame extends JFrame
{
    private static final int closeOperation = EXIT_ON_CLOSE;
    private static final int defaultWidth = 1400;
    private static final int defaultHeight = 800;
    private Images imageHandler;

    public PokerFrame(AbstractGame game, String playerName) {

	Player player = new Player(playerName, new Pot(1000));

	this.imageHandler = new Images();
	game.addPlayer(player);
	game.addBots();
	game.startGame();
	//Dousn't kill itself when closed -->
//	this.setDefaultCloseOperation(closeOperation);
	setSize(new Dimension(defaultWidth, defaultHeight));
	setContentPane(new JLabel(imageHandler.getBackgroundImageIcon()));
	setLayout(new BorderLayout());

	PlayerFrame playerFrame = new PlayerFrame(game);
	PokerComponent comp = new PokerComponent(game, playerFrame, this);

	add(comp);
	add(playerFrame, BorderLayout.AFTER_LAST_LINE);
    }
}