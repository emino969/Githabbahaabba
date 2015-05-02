package GUI;

import GUI.Components.PokerComponent;
import Money.Pot;
import Person.Player;
import Pictures.Images;
import PokerRules.AbstractGame;

import javax.swing.*;
import java.awt.*;

public class PokerFrame extends JFrame
{
    private static final int DEFAULT_WIDTH = 1400;
    private static final int DEFAULT_HEIGHT = 800;
    private Images imageHandler;

    public PokerFrame(AbstractGame game, String playerName) {
	Player player = new Player(playerName, new Pot(1000));

	this.imageHandler = new Images();
	game.addBots(); //bots before player for increased graphics swag
	game.addPlayer(player);
	game.startGame();
	//Dousn't kill itself when closed -->
//	this.setDefaultCloseOperation(closeOperation);
	setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
	setContentPane(new JLabel(imageHandler.getBackgroundImageIcon()));
	setLayout(new BorderLayout());

	PlayerFrame playerFrame = new PlayerFrame(game);
	PokerComponent comp = new PokerComponent(game, playerFrame, this);

	add(comp);
	add(playerFrame, BorderLayout.AFTER_LAST_LINE);
    }
}