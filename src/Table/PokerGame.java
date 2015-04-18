package Table;

import GameListeners.GameListener;
import Money.Pot;
import Person.*;
import PokerRules.AbstractPokermoves;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class PokerGame extends Table
{
    protected Person currentPlayer;
    protected Timer clockTimer;
    static private List<GameListener> ListenerArray = new ArrayList<GameListener>();
    private AbstractPokermoves moves = null; //Get's assigned in subclass
    protected static final int DELAY = 1000; //8 sekunder
    protected boolean isOverState;
    protected int currentPlayerIndex;

    public PokerGame()	{
	this.currentPlayer = null; //Get's assign in the subclass
	this.currentPlayerIndex = -1; //Get's assigned and is very important
	this.clockTimer = new Timer(DELAY, null); //The clockTimer that should be used in the subclasses also
	this.isOverState = false;
	clockTimer.setCoalesce(true);
    }

    public void setIsOverState(boolean state)	{
	isOverState = state;
    }

    static public void addGameListener(GameListener gl)	{
	ListenerArray.add(gl);
    }

    public void notifyListeners()	{
	for(GameListener gl : ListenerArray)	{
	    gl.gameChanged();
	}
    }

    public void setCurrentPlayer(Person player)	{
	this.currentPlayer = player;
	this.currentPlayerIndex = getIndexByPerson(player);
	player.changePersonState(PersonState.TURN);
    }

    public Person getCurrentPlayer()	{
	return currentPlayer;
    }

    protected void collectCards()	{
	/** Throw all the players cards to thrownCards  */
	for	(Person player : getPlayers())	{
	    player.throwCards();
	}
    }

    public void dealOutNCards(int N)	{
	int currentRound = 0;
	while	(N > currentRound)	{
	    for	(Person player : getActivePlayers())	{
		player.addCard(dealer.popCard());
	    }
	    currentRound++;
	}
    }

    public void dealOutNHiddenCards(int N)	{
	int currentRound = 0;
	while	(N > currentRound)	{
	    for	(Person player : getActivePlayers())	{
		if	(player.equals(getPlayer()))	{
		    player.addCard(dealer.popCard());
		}	else {
		    player.addHiddenCard(dealer.popCard());
		}
	    }
	    currentRound++;
	}
    }

    public AbstractPokermoves getOptions()	{
	return moves;
    }

    public void stopClock()	{
	clockTimer.stop();
    }

    public void restartClock()	{
	clockTimer.restart();
    }

    public void setOptions(AbstractPokermoves moves)	{
	this.moves = moves;
    }

    /** Allt the methods below is methods that should be overrided in subclasses */

    public void nextMove()	{}

    public boolean gameFinished()	{return false;}

    public void restartGame()	{}
    public void addBot(String name, Pot pot) {}

    public void addPlayer(Person player)	{
    }

    public void startGame()	{}
}
