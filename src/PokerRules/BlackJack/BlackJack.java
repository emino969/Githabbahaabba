package PokerRules.BlackJack;

import CardGameExceptions.NoSuchCardException;
import Money.Pot;
import Table.PokerGame;
import Person.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BlackJack extends PokerGame
{
    private BlackJackMoves moves;
    private Map<Person, BlackJackHand> personMap;
    private static final int FAST_DELAY = 1000;
    private ActionListener move =  new AbstractAction() {
    	    @Override public void actionPerformed(ActionEvent e)	{
    		runGameForward();
    	    }
    	};

    public BlackJack() {
	setDealer(new BlackJackDealer(new Pot(1000))); //Default Pot
	dealer.setGame(this); //Dealer is created in table
	this.moves = new BlackJackMoves()	{
	    @Override public String getHandValue(Person person)	{
		if	(isState(person, BlackJackHand.BLACKJACK)) {
		    return "  BLACKJACK  ";
		}	else {
		    return "  Hand: " + getLegalHandSum(person) + "  ";
		}
	    }

	    @Override public ArrayList<String> getOptions(Person person)	{
		ArrayList<String> names = new ArrayList<String>();
		if	(person.getHand().isEmpty()) {
		    names.add("Bet 25$");
		    names.add("Bet 50$");
		    names.add("Bet 75$");
		    names.add("Bet 100$");
		}	else if (!person.isPersonState(PersonState.INACTIVE))	{
		    names.add("Stand");
		    names.add("Hit");

		    if	(person.getHand().getSize() == 2) {
			names.add("Double");
			names.add("Surrender");
		    }

		    if	(isSplittable(person))	{
			//names.add("Split");
		    }
		}
		return names;
	    }

	    @Override public void makeMove(String name)	{
		if	(name.equals("Stand"))	{
		    stand();
		    currentPlayer.changePersonState(PersonState.INACTIVE);
		}	else if(name.equals("Hit"))	{
		    hit();
		    currentPlayer.changePersonState(PersonState.WAITING);
		}	else if(name.equals("Bet 50$"))	{
		    buyCards(getCurrentPlayer(), 50);
		    currentPlayer.changePersonState(PersonState.WAITING);
		}	else if(name.equals("Bet 100$"))	{
		    buyCards(getCurrentPlayer(), 100);
		    currentPlayer.changePersonState(PersonState.WAITING);
		}	else if(name.equals("Bet 75$"))	{
		    buyCards(getCurrentPlayer(), 75);
		    currentPlayer.changePersonState(PersonState.WAITING);
		}	else if(name.equals("Bet 25$"))	{
		    buyCards(getCurrentPlayer(), 25);
		    currentPlayer.changePersonState(PersonState.WAITING);
		}	else if(name.equals("Double"))	{
		    doubleDown();
		    currentPlayer.changePersonState(PersonState.INACTIVE);
		}	else if(name.equals("Surrender"))	{
		    surrender();
		    currentPlayer.changePersonState(PersonState.LOSER);
		}	else if(name.equals("Split"))	{
		    split();
		    currentPlayer.changePersonState(PersonState.WAITING);
		}
	    }

	    @Override public void split()	{
		//Not completely added yet, WONT DO ANYTHING IF YOU PRESS SPLIT
		int currentBet = currentPlayer.getLastBet();
		makeBet(currentPlayer, currentBet);
		currentPlayer.addHand();
		currentPlayer.getHandByIndex(1).addCard(currentPlayer.popCard()); //Gets one of the cards from the first hand
		currentPlayer.getHandByIndex(0).addCard(dealer.popCard());
		currentPlayer.getHandByIndex(1).addCard(dealer.popCard());
	    }

	    @Override public void surrender()	{
		//Fold and lose half your bet
		int bet = currentPlayer.getLastBet();
		int loss = bet / 2;
		dealer.getTablePot().subtractAmount(bet);
		dealer.addToPot(loss);
		currentPlayer.addToPot(loss);
		currentPlayer.setLastBet(0);
	    }

	    @Override public void stand()	{
		/** Do nothing */
	    }

	    @Override public void hit()	{
		/** Add one card to player */
		getCurrentPlayer().addCard(dealer.popCard());
	    }

	    @Override public void doubleDown()	{
		/** Double the bet and stand */
		int bet = currentPlayer.getLastBet();
		makeBet(getCurrentPlayer(), bet);
		hit();
	    }
	};

	setOptions(moves);
	this.personMap = new HashMap<Person, BlackJackHand>();
    }

    private void buyCards(Person person, int amount)	{
	makeBet(person, amount);
 	dealer.giveNCardsToPlayer(person, 2);
    }

    private void makeBet(Person person, int amount)	{
	if	(!person.bet(amount)) {
	    person.quitGame(); //If you bet more than you can afford, you're out mister!!
	}
    }

    private boolean isSplittable(Person person)	{
	if	(person.getHand().getSize() == 2) {
	    int firstCard = person.getHand().getCardByIndex(0).getCardInt();
	    int secondCard = person.getHand().getCardByIndex(1).getCardInt();
	    return firstCard == secondCard;
	}	else	{
	    return false;
	}
    }

    private boolean checkVictoryOverDealer(Person person)	{
	if	(isState(dealer, BlackJackHand.BUSTED)) {
	    return true;
	}	else	{
	    return (getLegalHandSum(dealer) <= getLegalHandSum(person)) && !isState(person, BlackJackHand.BUSTED) ;
	}
    }

    private void getNextPlayer()	{
	setCurrentPlayer(nextPerson());
	notifyListeners();
    }

    private Person nextPerson()	{
	if	(getActivePlayers().size() != 0) {
	    if	(getActivePlayers().contains(currentPlayer)) {
		return getPersonByIndex((currentPlayerIndex + 1) % getActivePlayers().size());
	    }	else	{
		return getPersonByIndex(currentPlayerIndex % getActivePlayers().size());
	    }
	}	else	{
	    return null;
	}
    }

    public void runGameForward()	{
	/** Give next player the turn */
	currentPlayer.turn();
	updatePlayerState(currentPlayer);
	if	(isOver())	{
	    if	(isOverState)	{
		clockTimer.stop();
		getWinner();
		//Extremely important this comes before getNextPlayer()
		notifyListeners();
		//Otherwhise when the game is finished the getNextPlayer method will be stuck in a loop searching for players
		restartGame();
	    }	else if	(dealerIsInactive())	{
		activateDealer();
		clockTimer.setDelay(FAST_DELAY); //Make the dealer finish faster
		clockTimer.restart();
		getNextPlayer();
	    }
	}	else	{
	    getNextPlayer();
	}

	//If current player is YOU, then stop the clock
	if	(currentPlayer.equals(getPlayer()))	{
	    clockTimer.stop();
	}
    }

    private boolean isEveryOneInactive()	{
	for	(Person person : getActivePlayers()) {
	    if (!person.isPersonState(PersonState.INACTIVE)) {
		return false;
	    }
	}
	return true;
    }

    private boolean isOver()	{
	return (isEveryOneInactive() || isOverState);
    }

    private boolean dealerIsInactive()	{
	return dealer.isPersonState(PersonState.INACTIVE);
    }

    private void updatePlayerState(Person player) {
	int handSum = getLegalHandSum(player);
	if (handSum > 21) {
	    personMap.put(player, BlackJackHand.BUSTED);
	    player.changePersonState(PersonState.LOSER); //Also counts as inactive
	} else if (handSum == 21 && player.getHand().getSize() == 2) {
	    personMap.put(player, BlackJackHand.BLACKJACK);
	} else if (handSum < 21) {
	    personMap.put(player, BlackJackHand.THIN);
	}
    }

    private int getLegalHandSum(Person person)	{
	int minSum = person.getHand().getSumAceOnBottom();
	int maxSum = person.getHand().getSumAceOnTop();
	if	(maxSum <= 21)	{
	    return maxSum;
	}	else	{
	    return minSum;
	}
    }

    public boolean isState(Person person, BlackJackHand st)	{
	return personMap.get(person).equals(st);
    }

    private void getWinner()	{
	isOverState = true;
	for	(Person person : getOnlyPlayers())	{
	    if	(checkVictoryOverDealer(person) && !person.isPersonState(PersonState.LOSER))	{
		makeToWinner(person);
	    }	else	{
		makeToLoser(person);
	    }
	}
    }

    private boolean isATie(Person person)	{
	if	(getLegalHandSum(dealer) == getLegalHandSum(person))	{
	    return isState(dealer, BlackJackHand.BLACKJACK) && isState(person, BlackJackHand.BLACKJACK);
	}	else	{
	    return false;
	}
    }

    private void makeToWinner(Person person)	{
	int victoryAmount;
	int bet = person.getLastBet();

	if	(isATie(person)) {
	    victoryAmount = 0;
	}	else if(isState(person, BlackJackHand.BLACKJACK))	{
	    victoryAmount = bet / 2;
	}	else	{
	    victoryAmount = bet;
	}

	dealer.getTablePot().subtractAmount(bet);
	dealer.getPot().subtractAmount(victoryAmount); //Dealer pays all profits from his own pocket!
	person.addToPot(victoryAmount + bet);
	person.changePersonState(PersonState.WINNER);
    }

    private void makeToLoser(Person person)	{
	int bet = person.getLastBet();
	person.changePersonState(PersonState.LOSER);
	dealer.giveAmountToPerson(dealer, bet);
    }

    private void setStartingStates()	{
	for	(Person person : getOnlyPlayers())	{
	    person.changePersonState(PersonState.WAITING);
	    personMap.put(person, BlackJackHand.THIN);
	}
    }

    @Override public void startGame()	{
	setStartingStates();
	setCurrentPlayer(getPlayer());
	dealer.giveStartingCards();
	clockTimer.addActionListener(move);
	//clockTimer.start();
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
	clockTimer.setDelay(DELAY);
	personMap.clear();
	setStartingStates();
	collectCards();
	deactivateDealer();
	dealer.startNewGame();
	personMap.put(dealer, BlackJackHand.THIN);
	setCurrentPlayer(getPlayer());
	dealer.giveStartingCards();
	notifyListeners();
    }
}
