package GUI;

import Money.Pot;
import Pictures.Images;
import PokerRules.BlackJack.BlackJack;
import Person.*;
import Table.PokerGame;

import javax.swing.*;
import java.awt.*;

public class PokerFrame extends JFrame
{
    private final PokerGame game;
    private static final int closeOperation = EXIT_ON_CLOSE;
    private static final int defaultWidth = 800;
    private static final int defaultHeight = 600;
    private Images imageHandler;

    public PokerFrame(PokerGame game) {
	Player Emil = new Player("Emil", new Pot(1000));
	Bot PlayerBot = new Bot("Bot", new Pot(1000));
	Bot SuperMario = new Bot("SuperMario", new Pot(1000));
	Bot SuperBot = new Bot("SuperBot", new Pot(1000));
	Bot SuperPlayer = new Bot("SuperPlayer", new Pot(1000));
	this.game = game; //Temporary for BlackJacK
	this.imageHandler = new Images();

	game.addPlayer(Emil);
	game.addPlayer(PlayerBot);
	game.addPlayer(SuperMario);
	game.addPlayer(SuperBot);
	game.addPlayer(SuperPlayer);
	game.startGame();
	//Dousn't kill itself when closed -->
	//this.setDefaultCloseOperation(closeOperation);
	setSize(new Dimension(defaultWidth, defaultHeight));
	setContentPane(new JLabel(imageHandler.getBackgroundImageIcon()));
	setLayout(new BorderLayout());

	PokerComponent comp = new PokerComponent(game, this);
	PlayerFrame playerFrame = new PlayerFrame(game);

	add(comp);
	add(playerFrame, BorderLayout.AFTER_LAST_LINE);
    }
}