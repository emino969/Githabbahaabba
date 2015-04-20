package PokerRules;

import Person.Dealer;
import Person.Person;
import Table.PokerGame;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *  AbstractGame is the structure that all games should follow
 */

public abstract class AbstractGame extends PokerGame
{
    public AbstractGame(Dealer dealer) {
	    setDealer(dealer);
	    this.dealer.setGame(this);
	    final ActionListener move = new AbstractAction()
	    {
		@Override public void actionPerformed(ActionEvent e) {
		    runGameForward();
		}
	    };
	    clockTimer.addActionListener(move);
	    setOptions(getOptions());
    }


    abstract public boolean gameFinished(); //Telling when the game is over

    abstract public void getWinner(); //Assign winners and end the current game round

    abstract public void startGame();

    abstract public void restartGame();

    abstract public AbstractPokermoves getOptions(); //return options for PokerGame

    public void runGameForward()	{
	//The method that runs the game forward

	if(playersTurn() && !gameFinished())	{
	    currentPlayer.turn();
	}

	if(dealersTurn() && !gameFinished()) {
	    dealerMove();
	}	else	{
	    getNextPlayer();
	}

	if	(gameFinished())	{
	    getWinner();
	    notifyListeners();
	    JOptionPane.showMessageDialog(null, "Click OK to continue");
	    restartGame();
	}

	if	(currentPlayer.equals(getPlayer()))	{
	    stopClock();
	}


	notifyListeners();
    }

    private void getNextPlayer()	{
	setCurrentPlayer(nextPerson());
     }

    private Person nextPerson()	{
	if	(getActivePlayers().contains(currentPlayer)) {
	    return getPersonByIndex((currentPlayerIndex + 1) % getActivePlayers().size());
	}	else	{
	    return getPersonByIndex((currentPlayerIndex) % getActivePlayers().size());
	}
    }

    abstract public boolean dealersTurn();

    abstract public boolean playersTurn();

    abstract public void dealerMove();

    public void addPlayer(Person person)	{
	person.setGame(this);
	players.add(person);
    }

    public void nextMove() {
	restartClock();
	runGameForward();
    }
}
