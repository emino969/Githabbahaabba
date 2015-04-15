package PokerRules;

import Person.*;
import Table.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class AbstractGame extends PokerGame
{
    private AbstractPokermoves options;
    private ActionListener move;

    public AbstractGame(Dealer dealer, AbstractPokermoves pokerMoves) {
	setDealer(dealer);
	this.dealer.setGame(this);
	this.options = pokerMoves;
	this.move =  new AbstractAction() {
        	    @Override public void actionPerformed(ActionEvent e)	{
        		runGameForward();
        	    }
        	};
	clockTimer.addActionListener(move);
	setOptions(options);
    }

    abstract public boolean gameFinished(); //Telling when the game is over

    abstract public void getWinner(); //Assign winners and end the current game round

    abstract public void startGame();

    abstract public void restartGame();

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

    abstract public boolean yourTurn();

    abstract public void dealerMove();

    @Override public void addPlayer(Person person)	{
	person.setGame(this);
	players.add(person);
    }

    public void nextMove() {
	restartClock();
	runGameForward();
    }
}
