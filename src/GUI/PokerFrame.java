package GUI;

import Money.Pot;
import Person.Player;
import Pictures.Images;
import Table.PokerGame;

import javax.swing.*;
import java.awt.*;
public class PokerFrame extends JFrame
{
    private static final int closeOperation = EXIT_ON_CLOSE;
    private static final int defaultWidth = 1600;
    private static final int defaultHeight = 1200;
    private Images imageHandler;
    private PokerGame game;

    public PokerFrame(PokerGame game) {
	Player Emil = new Player("Emil", new Pot(1000));
	this.game = game;
	this.imageHandler = new Images();
	game.addPlayer(Emil);
	game.addBot("PlayerBot", new Pot(1000));
	game.addBot("SuperMario", new Pot(1000));
	game.addBot("SuperBot", new Pot(1000));
	game.addBot("SuperPlayer", new Pot(1000));
	game.startGame();
	//Dousn't kill itself when closed -->
	//this.setDefaultCloseOperation(closeOperation);
	setSize(new Dimension(defaultWidth, defaultHeight));
	setContentPane(new JLabel(imageHandler.getBackgroundImageIcon()));
	setLayout(new BorderLayout());

	PlayerFrame playerFrame = new PlayerFrame(game);
	PokerComponent comp = new PokerComponent(game,playerFrame, this);

	add(comp);
	add(playerFrame, BorderLayout.AFTER_LAST_LINE);
    }

}