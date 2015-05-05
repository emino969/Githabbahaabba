package PokerRules.BlackJack;

import Money.Pot;
import Person.*;
import Person.BotTypes.BlackJackBot;
import PokerRules.AbstractGame;
import PokerRules.AbstractPokermoves;
import PokerRules.CardGameAction;
import java.util.ArrayList;

public class BlackJack extends AbstractGame
{
    private static final int CHIP_25 = 25;
    private static final int CHIP_50 = 50;
    private static final int CHIP_75 = 75;
    private static final int BLACKJACK_NUMBER = 21;
    private static final int START_SIZE = 2;
    private static final int NUMBER_OF_DECKS = 8;
    private static final int DEALER_POT_AMOUNT = 1000;
    private BlackJackMoves moves = new BlackJackMoves()	{
    	    @Override public String getHandValue(Person person)	{
    		if	(isBlackJack(person)) {
    		    return " Hand:  BLACKJACK  ";
    		}	else {
    		    return "  Hand: " + person.getHand().getLegalHandSum() + "  ";
    		}
    	    }

    	    @Override public ArrayList<CardGameAction> getOptions(Person person)	{
    		ArrayList<CardGameAction> actions = new ArrayList<>();
    		if	(person.getHand().isEmpty()) {
		    actions.add(BlackJackAction.RESET_BET);
    		    actions.add(BlackJackAction.BET_25);
    		    actions.add(BlackJackAction.BET_50);
    		    actions.add(BlackJackAction.BET_75);
		    actions.add(BlackJackAction.BET);
    		}	else if (!person.isPersonState(PersonState.INACTIVE) && !person.isPersonState(PersonState.WINNER))	{
    		    actions.add(BlackJackAction.STAND);
    		    actions.add(BlackJackAction.HIT);

    		    if	(isDoubleable(person)) {
			actions.add(BlackJackAction.DOUBLE);
		    }

		    if (isSurrendable(person))	{
    			actions.add(BlackJackAction.SURRENDER);
    		    }

    		    if	(isSplittable(person))	{
    			actions.add(BlackJackAction.SPLIT);
    		    }
    		}
    		return actions;
    	    }


    	    @Override  public void makeMove(CardGameAction blackJackAction) {
    	 	switch((BlackJackAction) blackJackAction){
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
    	 		split();
    	 		break;
    	 	    case BET_25:
    	 		currentPlayer.addToBet(CHIP_25);
			notifyListeners();
    	 		break;
    	 	    case BET_50:
			currentPlayer.addToBet(CHIP_50);
			notifyListeners();
    	 		break;
    	 	    case BET_75:
			currentPlayer.addToBet(CHIP_75);
			notifyListeners();
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
		    case BET:
			buyCards(currentPlayer, currentPlayer.getBet());
			break;
		    case RESET_BET:
			currentPlayer.resetBet();
			currentPlayer.resetLastBet();
			notifyListeners();
    	 	    default:
    			break;
    	 	}
		updatePlayerState(currentPlayer);
	    }


    	    @Override public void split()	{
    		int currentBet = currentPlayer.getLastBet();
    		makeBet(currentPlayer, currentBet);
    		currentPlayer.addHand();
		int lastHandIndex = currentPlayer.getHands().size() - 1;
		int currentHandIndex = currentPlayer.getHands().indexOf(currentPlayer.getHand());
    		currentPlayer.getHandByIndex(lastHandIndex).addCard(currentPlayer.popCard()); //Gets one of the cards from the first hand
    		currentPlayer.getHandByIndex(currentHandIndex).addCard(dealer.popCard());
    		currentPlayer.getHandByIndex(lastHandIndex).addCard(dealer.popCard());
		currentPlayer.changePersonState(PersonState.WAITING);
		currentPlayer.setHand(currentPlayer.mappedHands.getLastHand());
		notifyListeners();
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
		notifyListeners();
    	    }

    	    @Override public void stand()	{
    		/** Do nothing */
    		currentPlayer.changePersonState(PersonState.INACTIVE);
		notifyListeners();
    	    }

    	    @Override public void hit()	{
    		/** Add one card to player */
    		currentPlayer.addCard(dealer.popCard());
    		currentPlayer.changePersonState(PersonState.WAITING);
		notifyListeners();
    	    }

    	    @Override public void doubleDown()	{
    		/** Double the bet and stand */
		hit();
		stand();
		currentPlayer.addToBet(currentPlayer.getLastBet());
		makeBet(currentPlayer, currentPlayer.getBet());
		notifyListeners();
    	    }
    	};

    public BlackJack() {
	super(new BlackJackDealer(new Pot(DEALER_POT_AMOUNT), NUMBER_OF_DECKS));
    }

    private void updatePlayerState(Person person)	{
	if (!person.equals(dealer) && !person.isPersonState(PersonState.LOSER)) {
	    if (isPersonBusted(person)) {
		person.changePersonState(PersonState.LOSER);
	    } else if (isBlackJack(person)) {
		person.changePersonState(PersonState.WINNER); //Is black jack
	    }
	}
    }

    private boolean isDoubleable(Person person)	{
	return person.getLastBet() <= person.getPot().getAmount() && person.getHand().getSize() == 2;
    }

    private boolean isSurrendable(Person person)	{
	return person.getHand().getSize() == 2;
    }

    private void buyCards(Person person, int amount)	{
	if (currentPlayer.getPot().getAmount() >= 0 || amount == 0) {
	    currentPlayer.changePersonState(PersonState.WAITING);
	    makeBet(person, amount);
	    dealer.giveNCardsToPlayer(person, 2);
	}	else	{
	    currentPlayer.addToPot(amount);
	    currentPlayer.resetBet();
	    notifyListeners();
	    throw new IllegalArgumentException("You can't bet that amount!");
	}
    }

    private void makeBet(Person person, int amount)	{
	person.bet(amount);
    }

    public boolean isSplittable(Person person)	{
	if	(person.getHand().getSize() == START_SIZE) {
	    int firstCard = person.getHand().getCardByIndex(0).getCardIntValue();
	    int secondCard = person.getHand().getCardByIndex(1).getCardIntValue();
	    return firstCard == secondCard;
	}	else	{
	    return false;
	}
    }

    private boolean checkVictoryOverDealer(Person person)	{
	if	(isPersonBusted(person) || person.isPersonState(PersonState.LOSER)) {
	    return false;
	}	else if(isDealerBusted())	{
	    return true;
	}	else	{
	    return (dealer.getHand().getLegalHandSum() <= person.getHand().getLegalHandSum()) && !isPersonBusted(person) ;
	}
    }

    private void makeToWinner(Person person)	{
	int victoryAmount;
	int bet = person.getLastBet();

	if	(isATie(person)) {
	    victoryAmount = 0;
	}	else if(isBlackJack(person))	{
	    victoryAmount = bet / 2;
	}	else	{
	    victoryAmount = bet;
	}

	dealer.getTablePot().subtractAmount(bet);
	dealer.getPot().subtractAmount(victoryAmount); //Dealer pays all profits from his own pocket!
	person.addToPot(victoryAmount + bet);
	person.changePersonState(PersonState.WINNER);
    }

    private boolean isATie(Person person)	{
	if (dealer.getHand().getLegalHandSum() != person.getHand().getLegalHandSum()) {
	    return false;
	}	else if(isBlackJack(dealer) && isBlackJack(person))	{
	    return true;
	}	else	{
	    return (person.getHand().getLegalHandSum() == dealer.getHand().getLegalHandSum()) &&
		   !(isBlackJack(dealer) || isBlackJack(person));
	}
    }

    private boolean isBlackJack(Person person)	{
	return person.getHand().getLegalHandSum() == 21 && person.getHand().getSize() == 2;
    }

    private boolean isPersonBusted(Person person)	{
	return person.getHand().getLegalHandSum() > 21;
    }

    private boolean isDealerBusted()	{
	return dealer.getHand().getSumAceOnTop() > 21 ;
    }

    private void makeToLoser(Person person)	{
	int bet = person.getLastBet();
	person.changePersonState(PersonState.LOSER);
	dealer.giveAmountToPerson(dealer, bet);
    }

    private void setStartingStates()	{
	for	(Person person : getOnlyPlayers())	{
	    person.changePersonState(PersonState.WAITING);
	    person.resetLastBet();
	}
    }

    @Override public boolean gameFinished() {
	return isOverState;
    }

    @Override public void getWinner() {
	isOverState = true;
	for	(Person person : getOnlyPlayers())	{
	    if	(checkVictoryOverDealer(person))	{
		makeToWinner(person);
	    }	else	{
		makeToLoser(person);
	    }
	}
	assert false : getOnlyPlayers();
    }

    @Override public void startGame() {
	setStartingStates();
	setCurrentPlayer(getPlayer());
	dealer.giveStartingCards();
    }

    @Override public void restartGame() {
	isOverState = false;
	setStartingStates();
	dealer.collectCards();
	deactivateDealer();
	setCurrentPlayer(getPlayer());
	dealer.giveStartingCards();
	notifyListeners();
    }

    @Override public void addBots() {
	BlackJackBot PlayerBot = new BlackJackBot("Bot", new Pot(1000),this);
	BlackJackBot SuperMario = new BlackJackBot("SuperMario", new Pot(1000), this);
	BlackJackBot SuperBot = new BlackJackBot("SuperBot", new Pot(1000), this);
	BlackJackBot SuperPlayer = new BlackJackBot("SuperPlayer", new Pot(1000), this);
	addPlayer(PlayerBot);
	addPlayer(SuperMario);
	addPlayer(SuperPlayer);
	addPlayer(SuperBot);
    }

    @Override public AbstractPokermoves getOptions() {
	return moves;
    }

    @Override public boolean isDealersTurn() {
	return getActivePlayers().isEmpty();
    }

    @Override public boolean playersTurn() {
	return !isDealersTurn();
    }

    @Override public void dealerMove() {
	setCurrentPlayer(dealer);
    }

    @Override protected void getNextPlayer()	{
	if (currentPlayer.isTurnLeft())	{
	    currentPlayer.switchHand();
	    currentPlayer.changePersonState(PersonState.TURN);
	}	else	{
	    currentPlayer.switchHand();
	    setCurrentPlayer(nextPerson());
	}
    }
}
