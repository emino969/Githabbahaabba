package GUI;

import GameListeners.GameListener;
import Person.Player;
import PokerRules.AbstractPokermoves;
import Table.PokerGame;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerFrame extends JToolBar
{
    private Player player;
    private AbstractPokermoves pokermoves;
    private JLabel nextMove, currentPot, handValue;
    //private JTextField betField;
    private PokerGame game;
    private JPanel buttonPanel;

    public PlayerFrame(final PokerGame game) {
	this.game = game;
	this.player = game.getPlayer();
	this.currentPot = new JLabel("  Current pot:  " + player.getPot().getAmount() + "$  ");
	//this.betField = new JTextField();
	//betField.setMaximumSize(new Dimension(50, 100));
	this.pokermoves = game.getOptions();
	this.handValue = new JLabel(pokermoves.getHandValue(player));
	this.buttonPanel = new JPanel();
	this.add(buttonPanel);
	updateOptions();
	buttonPanel.add(currentPot);
	buttonPanel.add(handValue);

	GameListener gl = new GameListener() {
	    @Override public void gameChanged() {
		buttonPanel.removeAll();
		buttonPanel.revalidate();
		updateOptions();
		buttonPanel.add(currentPot);
		buttonPanel.add(handValue);

		if	(game.gameFinished())	{
		    JOptionPane.showMessageDialog(null, "Click OK to restart");
		    game.restartGame();
		}	else	{
		    updateLabels();
		}
	    }
	};

	game.addGameListener(gl);
    }

    public void updateOptions()	{
	for (final String name : pokermoves.getOptions(player))	{
	    JButton button = new JButton(name);
	    button.addActionListener(new ActionListener() {
		@Override public void actionPerformed(ActionEvent e) {
		    if	(game.getPlayer().hasTurn()) {
			pokermoves.makeMove(name);
			game.nextMove();
			game.notifyListeners();
			game.startClock();
		    }
		}
	    });
	    buttonPanel.add(button);
    	}
    }

    public void updateLabels()	{
	currentPot.setText("  Current pot: " + player.getPot() + "$  ");
	handValue.setText(pokermoves.getHandValue(player));
    }
}
