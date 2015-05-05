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

    public PokerFrame(AbstractGame game, String playerName) {
	Player player = new Player(playerName, new Pot(1000));
	Images.getInstance();
	game.addBots(); //bots before player for increased graphics swag
	game.addPlayer(player);
	game.startGame();
	setSize(new Dimension(defaultWidth, defaultHeight));
	setContentPane(new JLabel(Images.getBackgroundImageIcon()));
	setLayout(new BorderLayout());

	PlayerFrame playerFrame = new PlayerFrame(game);
	PokerComponent comp = new PokerComponent(game, playerFrame, this);

	add(comp);
	add(playerFrame, BorderLayout.AFTER_LAST_LINE);
    }
}