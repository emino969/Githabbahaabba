package PokerRules;

import Person.*;
import Table.PokerGame;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class AbstractGame extends PokerGame
{
    private AbstractPokermoves options;
    private ActionListener move;

    public AbstractGame(Dealer dealer, AbstractPokermoves pokerMoves) {
	this.move =  new AbstractAction() {
        	    @Override public void actionPerformed(ActionEvent e)	{
        		runGameForward();
        	    }
        	};
	setDealer(dealer);
	dealer.setGame(this);
	this.options = pokerMoves;
	setOptions(options);
	clockTimer.addActionListener(move);
    }

    abstract public boolean gameFinished(); //Telling when the game is over

    abstract public boolean getWinner(); //Assign winners and end the current game round

    abstract public void startGame();

    abstract public void restartGame();

    public void runGameForward()	{
	//The method that runs the game forward
	if	(gameFinished())	{
	    getWinner();
	    restartGame();
	}	else if(dealersTurn()) {
	    currentPlayer.changePersonState(PersonState.WAITING);
	    dealer.turn();
	}	else if(playersTurn())	{
	    currentPlayer.turn();
	}

	getNextPlayer();
    }

    private void getNextPlayer()	{
 	setCurrentPlayer(nextPerson());
 	notifyListeners();
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

    @Override public void addPlayer(Person person)	{
	person.setGame(this);
	players.add(person);
    }

    public void nextMove()	{
	clockTimer.restart();
	runGameForward();
    }
}
