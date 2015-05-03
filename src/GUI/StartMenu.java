package gui;

import pokerrules.AbstractGame;
import pokerrules.blackjack.BlackJack;
import pokerrules.texasholdem.TexasHoldem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * startmenu handles startmenu and its deriving components, its buttons and the initializing of correct game.
 */
public class StartMenu extends JFrame
{
    private static final int CLOSE_OPERATION = EXIT_ON_CLOSE;
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 400;
    private final static String WELCOME_TEXT = "SELECT A GAME";
    private JButton blackJack;
    private JButton texasHoldem;
    private JLabel labelMessage;
    private PokerFrame frame;

    public StartMenu()	{
	this.blackJack = new JButton("Black Jack");
	this.labelMessage = new JLabel(WELCOME_TEXT);
	this.texasHoldem = new JButton("Texas Holdem Limited");
	this.frame = null;

	texasHoldem.setFont(new Font("Serif", Font.BOLD, 27));
	blackJack.setFont(new Font("Serif", Font.BOLD, 27));
	labelMessage.setFont(new Font("Serif", Font.BOLD, 30));

	//Add actionListeners here
	blackJack.addActionListener(new ActionListener() {
	    @Override public void actionPerformed(ActionEvent e) {
		runGame(new BlackJack());
	    }
	});

	texasHoldem.addActionListener(new ActionListener()	{
	    @Override public void actionPerformed(ActionEvent e)	{
		runGame(new TexasHoldem());
	    }
	});

	setDefaultCloseOperation(CLOSE_OPERATION);
	setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
	setLayout(new GridBagLayout());
	setOptions();
    }

    private void setOptions()	{
	GridBagConstraints constraints = new GridBagConstraints();

	constraints.fill = GridBagConstraints.HORIZONTAL;
	constraints.weighty = 0;
	constraints.gridy = 0;
	add(labelMessage, constraints);

	constraints.fill = GridBagConstraints.HORIZONTAL;
	constraints.weighty = 1;
	constraints.gridy = 1;
	add(blackJack, constraints);

	constraints.fill = GridBagConstraints.HORIZONTAL;
	constraints.weighty = 0;
	constraints.gridy = 2;
	add(texasHoldem, constraints);
    }

    private void runGame(AbstractGame game) {
	String playerName = "Guest";
	//String inputString = JOptionPane.showInputDialog("Please Enter your name or leave empty and join as Guest");
	//if (inputString != null && inputString != "") playerName = inputString;
	frame = new PokerFrame(game, playerName);
	frame.setVisible(true);
    }
}
