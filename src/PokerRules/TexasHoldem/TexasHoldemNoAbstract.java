package PokerRules.TexasHoldem;

import Cards.CardList;
import Money.Pot;
import Person.*;
import Table.PokerGame;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
public class TexasHoldemNoAbstract extends PokerGame
{
    private HoldemMoves moves;
    private HoldemHandComparator HHM;
    private int highestBet;
    private final int minBet;
    private ActionListener move =  new AbstractAction() {
    	    @Override public void actionPerformed(ActionEvent e)	{
    		runGameForward();
    	    }
    	};

    public TexasHoldemNoAbstract(int minimumBet) {
	setDealer(new TexasHoldemDealer(new Pot(1000))); //Default size of pot
	dealer.setGame(this); //Dealer is created in table
	this.minBet = minimumBet;
	this.highestBet = minBet;
	this.HHM = new HoldemHandComparator();
	//HHM.setPokerGame(this);
	this.moves = new HoldemMoves()	{
	    @Override public void call()	{
		currentPlayer.bet(highestBet - currentPlayer.getLastBet());
	    }

	    @Override public void raise()	{
		currentPlayer.bet(2 * minBet);
		highestBet = 2 * minBet;
	    }

	    @Override public void fold()	{
		currentPlayer.changePersonState(PersonState.LOSER);
	    }

	    @Override public ArrayList<String> getOptions(Person person)	{
		ArrayList<String> options = new ArrayList<String>();
		if	(!person.isPersonState(PersonState.INACTIVE))	{
		    options.add("Call");
		    //options.add("Fold");

		    if	(highestBet != 2 * minBet) {
			options.add("Raise");
		    }
		}
		return options;
	    }

	    @Override public void makeMove(String name)	{
		if	(name.equals("Call"))	{
		    call();
		    currentPlayer.changePersonState(PersonState.WAITING);
		}	else if(name.equals("Raise"))	{
		    raise();
		    currentPlayer.changePersonState(PersonState.WAITING);
		}	else if(name.equals("Fold"))	{
		    fold();
		}
	    }

	    @Override public String getHandValue(Person person) {
		return String.valueOf(HHM.getTexasHand(person.getHand())); //Need to be assigned correctly
	    }
	};
	setOptions(moves);
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

    private void clearLastBets()	{
	for	(Person person : getActivePlayers())	{
	    person.setLastBet(0);
	}
    }

    public int getHighestBet()	{
 	return highestBet;
     }

    public boolean finishedBetting()	{
	for	(Person person : getOnlyActivePlayers())	{
	    if	(person.getLastBet() != getHighestBet())	{
		return false;
	    }
	}
	return true;
    }

    private void runGameForward()	{
	currentPlayer.turn();
	getNextPlayer();

	if	(finishedBetting()) {
	    currentPlayer.changePersonState(PersonState.WAITING);
	    dealer.turn();
	    highestBet = minBet;
	    setCurrentPlayer(getPlayer());
	    clearLastBets();
	}

	if	(currentPlayer.equals(getPlayer()))	{
	    clockTimer.stop();
	}

	if	(isOver())	{
	    getWinner();
	    notifyListeners();
	    restartGame();
	}

	notifyListeners();
    }

    private void getWinner()	{
	showCards();
	ArrayList<CardList> hands = new ArrayList<CardList>();
	ArrayList<Person> players = getActivePlayers();
	for (int i = 0; i < players.size(); i++) {
	    hands.add(players.get(i).getHand());
	}
	hands.sort(HHM);
	players.get(0).changePersonState(PersonState.WINNER);
	dealer.givePot(players.get(0));
    }

    private void makeToLoser(Person player)	{
	player.changePersonState(PersonState.LOSER);
    }

    private void showCards()	{
	for	(Person player : getActivePlayers())	{
	    player.setCardsVisible();
	}
    }

    private void setStartingStates()	{
	for	(Person person : getOnlyPlayers())	{
	    person.changePersonState(PersonState.WAITING);
	}
    }

    private boolean isOver()	{return isOverState;}

    @Override public void startGame()	{
	dealOutNHiddenCards(2);
	//activateWaitingDealer();
	clockTimer.addActionListener(move);
	setCurrentPlayer(getPlayer());
	//clockTimer.start();
    }

    @Override public void dealOutNHiddenCards(int N)	{
	int currentRound = 0;
	while	(N > currentRound)	{
	    for	(Person player : getActivePlayers())	{
		if	(player.equals(getPlayer())) {
		    player.addCard(dealer.popCard());
		}	else	{
		    player.addHiddenCard(dealer.popCard());
		}
	    }
	    currentRound++;
	}
    }

    @Override public void addPlayer(Person player)	{
	player.setGame(this);
	players.add(player);
    }

    @Override public void nextMove()	{
	clockTimer.restart();
	runGameForward();
    }

    @Override public boolean gameFinished()	{
	return isOver();
    }

    @Override public void restartGame()	{
	isOverState = false;
	highestBet = minBet;
	clockTimer.setDelay(DELAY);
	collectCards();
	clearLastBets();
	dealer.startNewGame();
	setStartingStates();
	setCurrentPlayer(getPlayer());
	dealOutNHiddenCards(2);
	notifyListeners();
    }
}
