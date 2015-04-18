package GUI;

import GameListeners.GameListener;
import Person.PersonState;
import Person.Player;
import PokerRules.AbstractPokermoves;
import PokerRules.CardGameAction;
import Table.PokerGame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerFrame extends JToolBar
{
    private Player player;
    private AbstractPokermoves pokermoves;
    private JLabel currentPot;
    private JLabel handValue;
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

		if(game.gameFinished())	{
		    JOptionPane.showMessageDialog(null, "Click OK to restart");
		}else{
		    updateLabels();
		}
	    }
	};

	PokerGame.addGameListener(gl);
    }

    public void updateOptions()	{
	for (final CardGameAction cardGameAction : pokermoves.getOptions(player))	{
	    JButton button = new JButton(cardGameAction.toString());
	    button.addActionListener(new ActionListener() {
		@Override public void actionPerformed(ActionEvent e) {
		    if	(game.getPlayer().hasTurn()) {
			pokermoves.makeMove(cardGameAction);
			updateLabels();
			if(!game.getPlayer().isPersonState(PersonState.TURN)) game.nextMove();
		    }
		}

	    });
	    buttonPanel.add(button);
    	}
    }

    public void updateLabels()	{
	currentPot.setText("  Current pot: " + player.getPot() + "$  " + "HandBet: " + player.getLastBet() + "$");
	handValue.setText(pokermoves.getHandValue(player));
    }
}
