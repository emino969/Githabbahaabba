package GUI;

import PokerRules.AbstractGame;
import PokerRules.BlackJack.BlackJack;
import PokerRules.TexasHoldem.TexasHoldem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenu extends JFrame
{
    private static final int closeOperation = EXIT_ON_CLOSE;
    private static final int defaultWidth = 400;
    private static final int defaultHeight = 400;
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

	setDefaultCloseOperation(closeOperation);
	setSize(new Dimension(defaultWidth, defaultHeight));
	setLayout(new GridBagLayout());
	setOptions();
    }

    private void setOptions()	{
	GridBagConstraints c = new GridBagConstraints();

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weighty = 0;
	c.gridy = 0;
	add(labelMessage, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weighty = 1;
	c.gridy = 1;
	add(blackJack, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weighty = 0;
	c.gridy = 2;
	add(texasHoldem, c);
    }

    private void runGame(AbstractGame game) {
	String playerName = "Guest";
	//String inputString = JOptionPane.showInputDialog("Please Enter your name or leave empty and join as Guest");
	//if (inputString != null && inputString != "") playerName = inputString;
	frame = new PokerFrame(game, playerName);
	frame.setVisible(true);
    }
}
