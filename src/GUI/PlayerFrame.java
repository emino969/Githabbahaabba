package GUI;

import CardGameExceptions.CardGameActionException;
import GameListeners.GameListener;
import Person.PersonState;
import Person.Player;
import PokerRules.AbstractGame;
import PokerRules.AbstractPokermoves;
import PokerRules.CardGameAction;
import Table.Table;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerFrame extends JToolBar
{
    private Player player;
    private AbstractPokermoves pokermoves;
    private JLabel currentPot;
    private JLabel handValue;
    private AbstractGame game;
    private JPanel buttonPanel;

    public PlayerFrame(final AbstractGame game) {
	this.game = game;
	this.player = game.getPlayer();
	this.currentPot = new JLabel("  Current pot:  " + player.getPot().getAmount() + "$  ");
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
		updateLabels();
	    }
	};

	Table.addGameListener(gl);
    }

    public void updateOptions()	{
	for (final CardGameAction cardGameAction : pokermoves.getOptions(player))	{
	    String actionName = cardGameAction.toString();
	    JButton button = new JButton(actionName);
	    button.setMnemonic(actionName.charAt(0));
	    button.addActionListener(new ActionListener()
	    {
		@Override public void actionPerformed(ActionEvent e) {
		    if (game.getPlayer().hasTurn()) {
			try {
			    pokermoves.makeMove(cardGameAction);
			} catch (CardGameActionException e1) {
			    e1.printStackTrace();
			}
			updateLabels();
			if (!game.getPlayer().isPersonState(PersonState.TURN)) game.nextMove();
		    }
		}
	    });
	    buttonPanel.add(button);
    	}
    }

    public void updateLabels()	{
	int handBet = player.getLastBet() + player.getBetHolder();
	currentPot.setText("  Current pot: " + player.getPot() + "$  " +
			   "  HandBet: " + handBet + "$");
	handValue.setText(pokermoves.getHandValue(player));
    }
}
