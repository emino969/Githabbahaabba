package Person.BotTypes;

import Cards.Card;
import Cards.CardList;
import Cards.CardType;
import GameListeners.GameListener;
import Money.Pot;
import Person.Person;
import PokerRules.BlackJack.BlackJackAction;
import Table.PokerGame;

import java.util.Random;

public class HighestCardBot extends Person implements GameListener
{
    private CardList playedCards;
    private final static int GAME_DECK_SIZE = 52;
    private final static double HIGH_BET_LIMIT = 0.2;
    private final static double MEDIUM_BET_LIMIT = 0.4;
    private final static double LOW_BET_LIMIT = 0.5;

    private Random random = new Random();
    public HighestCardBot(final String name, final Pot pot, final PokerGame game) {
        super(name, pot);
	this.game = game;
    }

    @Override public void turn() {
        this.bet(getBetAmount());
        if(this.getHand().getCardByIndex(0).getCardInt() > 10) game.getOptions().makeMove(BlackJackAction.STAND);
	else{
	    //game.getActions().makeMove("Change card");
	}
    }

    public int getBetAmount() {
        Person dealer = game.getDealer();
        int bet =0;
        int higherCards = getHigherCards();
        double odds = (double) higherCards / GAME_DECK_SIZE; // if highercards/52 isnt cast as double, java will give the result 0.0 as it rounds 0.xxx to  0.0
        bet += getBet(odds);
        if(bet > pot.getAmount()) bet = pot.getAmount();
        return bet;
    }
    private int getBet(double odds){
        System.out.println(name + odds);
        if(odds < HIGH_BET_LIMIT) return highBet();
        else if(odds < MEDIUM_BET_LIMIT) return mediumBet();
        else if( odds < LOW_BET_LIMIT) return lowBet();
        else return 0;
    }
    private int getHigherCards(){
        int cardValue = hand.getCardByIndex(0).getCardInt();
        int higherCards = (14 - cardValue) * CardType.values().length ; // You have your own card
        if(playedCards != null) {
            for(Card card: playedCards.getCardList()) {
                if (card.isVisible() && cardValue < card.getCardInt()) higherCards--;
                // if there is a higher card that has been played there is one less in play
            }

        }
        return higherCards;
    }
    private int highBet(){
        int bet = 250;
        int betFactor = random.nextInt(5);
        bet += betFactor * 100;
        return bet;
    }
    private int lowBet(){
        int bet = 0;
        int betFactor = random.nextInt(5);
        bet += betFactor*10;
        return bet;
    }
    private int mediumBet(){
        int bet = 100;
        int betFactor = random.nextInt(5);
        bet += betFactor * 50;
        return bet;
    }

    @Override public void gameChanged() {
       // playedCards = game.getDealer().getThrownCards();
    }
}
