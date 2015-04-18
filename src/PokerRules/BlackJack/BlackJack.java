package PokerRules.BlackJack;

import Money.Pot;
import Person.Person;
import Person.PersonState;
import PokerRules.CardGameMove;
import Table.PokerGame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The BlackJack game
 */

public class BlackJack extends PokerGame
{
    private BlackJackMoves moves;
    private Map<Person, BlackJackHand> personMap;
    private static final int FAST_DELAY = 1000;
    private static final int CHIP_25 = 25;
    private static final int CHIP_50 = 75;
    private static final int CHIP_75 = 75;
    private static final int BLACKJACK_NUMBER = 21;
    private static final int START_SIZE = 2;
    private static final int NUMBER_OF_DECKS = 8;
    private static final int DEALER_POT_AMOUNT = 1000;
    private ActionListener move =  new AbstractAction() {
    	    @Override public void actionPerformed(ActionEvent e)	{
    		runGameForward();
    	    }
    	};

    public BlackJack() {
	setDealer(new BlackJackDealer(new Pot(DEALER_POT_AMOUNT), NUMBER_OF_DECKS)); //Default Pot
	dealer.setGame(this); //Dealer is created in table
	this.moves = new BlackJackMoves()	{
	    @Override public String getHandValue(Person person)	{
		if	(isState(person, BlackJackHand.BLACKJACK)) {
		    return "  BLACKJACK  ";
		}	else {
		    return "  Hand: " + getLegalHandSum(person) + "  ";
		}
	    }

	    @Override public ArrayList<CardGameMove> getOptions(Person person)	{
		ArrayList<CardGameMove> actions = new ArrayList<>();
		if	(person.getHand().isEmpty()) {
		    actions.add(BlackJackAction.BET_25);
		    actions.add(BlackJackAction.BET_50);
		    actions.add(BlackJackAction.BET_75);
		}	else if (!person.isPersonState(PersonState.INACTIVE))	{
		    actions.add(BlackJackAction.STAND);
		    actions.add(BlackJackAction.HIT);

		    if	(person.getHand().getSize() == 2) {
			actions.add(BlackJackAction.DOUBLE);
			actions.add(BlackJackAction.SURRENDER);
		    }

		    if	(isSplittable(person))	{
			//names.add("Split");
		    }
		}
		return actions;
	    }


	    @Override  public void makeMove(CardGameMove blackJackAction) {
	 		switch((BlackJackAction)blackJackAction){
	 		    case HIT:
	 			hit();
	 			break;
	 		    case STAND:
	 			stand();
	 			break;
	 		    case SURRENDER:
	 			surrender();
	 			break;
	 		    case DOUBLE:
	 			doubleDown();
	 			break;
	 		    case SPLIT:
	 			hit();
	 			break;
	 		    case BET_25:
	 			buyCards(currentPlayer, CHIP_25);
	 			break;
	 		    case BET_50:
	 			buyCards(currentPlayer, CHIP_50);
	 			break;
	 		    case BET_75:
	 			buyCards(currentPlayer, CHIP_75);
	 			break;
	 		    case DOUBLE_HIT:
	 			doubleDown();
	 			break;
	 		    case DOUBLE_STAND:
	 			doubleDown();
	 			break;
	 		    case DOUBLE_SURRENDER:
	 			surrender();
	 			break;
	 		    case SURRENDER_HIT:
	 			surrender();
	 			break;
	 		    default:
				break;
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
		currentPlayer.changePersonState(PersonState.LOSER);
	    }

	    @Override public void stand()	{
		/** Do nothing */
		currentPlayer.changePersonState(PersonState.INACTIVE);
	    }

	    @Override public void hit()	{
		/** Add one card to player */
		getCurrentPlayer().addCard(dealer.popCard());
		currentPlayer.changePersonState(PersonState.WAITING);
	    }

	    @Override public void doubleDown()	{
		/** Double the bet and stand */
		int bet = currentPlayer.getLastBet();
		makeBet(getCurrentPlayer(), bet);
		hit();
		stand();
	    }
	};

	setOptions(moves);
	this.personMap = new HashMap<>();
    }

    private void buyCards(Person person, int amount)	{
	makeBet(person, amount);
 	dealer.giveNCardsToPlayer(person, 2);
	currentPlayer.changePersonState(PersonState.WAITING);
    }

    private void makeBet(Person person, int amount)	{
	if	(!person.bet(amount)) {
	    person.quitGame(); //If you bet more than you can afford, you're out mister!!
	}
    }

    private boolean isSplittable(Person person)	{
	if	(person.getHand().getSize() == START_SIZE) {
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
	if	(!getActivePlayers().isEmpty()) {
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
	if (handSum > BLACKJACK_NUMBER) {
	    personMap.put(player, BlackJackHand.BUSTED);
	    player.changePersonState(PersonState.LOSER); //Also counts as inactive
	} else if (handSum == BLACKJACK_NUMBER && player.getHand().getSize() == START_SIZE) {
	    personMap.put(player, BlackJackHand.BLACKJACK);
	} else if (handSum < BLACKJACK_NUMBER) {
	    personMap.put(player, BlackJackHand.THIN);
	}
    }

    public int getLegalHandSum(Person person)	{
	int minSum = person.getHand().getSumAceOnBottom();
	int maxSum = person.getHand().getSumAceOnTop();
	if	(maxSum <= BLACKJACK_NUMBER)	{
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
	personMap.put(dealer, BlackJackHand.THIN);
	setCurrentPlayer(getPlayer());
	dealer.giveStartingCards();
	notifyListeners();
    }
}
