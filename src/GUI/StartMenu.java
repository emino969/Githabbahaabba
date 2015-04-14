package GUI;

import PokerRules.BlackJack.BlackJack;
import Table.PokerGame;
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
    private JLabel labelMessage;
    private PokerFrame frame;

    public StartMenu()	{
	this.blackJack = new JButton("Black Jack");
	this.labelMessage = new JLabel(WELCOME_TEXT);
	this.frame = null;

	blackJack.setFont(new Font("Serif", Font.BOLD, 27));
	labelMessage.setFont(new Font("Serif", Font.BOLD, 30));

	//Add actionListeners here
	blackJack.addActionListener(new ActionListener() {
	    @Override public void actionPerformed(ActionEvent e) {
		runGame(new BlackJack());
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
    }

    private void runGame(PokerGame game)	{
	frame = new PokerFrame(game);
	frame.setVisible(true);
    }
}
