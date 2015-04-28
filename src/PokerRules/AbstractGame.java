package PokerRules;

import Person.*;
import Table.Table;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *  AbstractGame is the structure that all games should follow
 */

public abstract class AbstractGame extends Table
{
    protected static final int DELAY = 1000; //8 sekunder
    protected Person currentPlayer;
    protected boolean isOverState;
    protected int currentPlayerIndex;
    protected Timer clockTimer;
private AbstractPokermoves moves = null; //Get's assigned in subclass

    public AbstractGame(Dealer dealer) {
	 setDealer(dealer);
	this.dealer.setGame(this);
	this.currentPlayer = null;
	this.currentPlayerIndex = -1;
	    final ActionListener move = new AbstractAction()
	    {
		@Override public void actionPerformed(ActionEvent e) {
		    runGameForward();
		}
	    };

	this.clockTimer = new Timer(AbstractGame.DELAY, move); //The clockTimer that should be used in the subclasses also
	clockTimer.setCoalesce(true);
	setOptions(getOptions());
    }


    abstract public boolean gameFinished(); //Telling when the game is over

    abstract public void getWinner(); //Assign winners and end the current game round

    abstract public void startGame();

    abstract public void restartGame();

    abstract public void addBots();

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

    protected void getNextPlayer()	{
	setCurrentPlayer(nextPerson());
     }

    protected Person nextPerson()	{
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

    public void setIsOverState(boolean state)	{
	isOverState = state;
    }

    public void setCurrentPlayer(Person player)	{
	this.currentPlayer = player;
	this.currentPlayerIndex = getIndexByPerson(player);
	player.changePersonState(PersonState.TURN);
    }

    public Person getCurrentPlayer()	{
	return currentPlayer;
    }

    public void setOptions(AbstractPokermoves moves)	{
	this.moves = moves;
    }

    public void stopClock()	{
	clockTimer.stop();
    }

    public void restartClock()	{
	clockTimer.restart();
    }
}
