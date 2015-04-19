package Person.BotTypes;

import Cards.CardValue;
import Money.Pot;
import Person.Person;
import PokerRules.AbstractPokermoves;
import PokerRules.BlackJack.BlackJack;
import PokerRules.BlackJack.BlackJackAction;
import Table.PokerGame;

import java.util.Random;

/**
 * this blackjackBot is based on basic strategy 4-8decks fpimd at
 * http://wizardofodds.com/games/blackjack/strategy/4-decks/ 13.14.2015
 */
public class BlackJackBot extends Person
{
    private BlackJack blackJack;
    private Random rnd;
    public BlackJackBot(final String name, final Pot pot, final BlackJack blackJack) {
        super(name, pot);
        this.blackJack = blackJack;
        this.game = blackJack;
        this.rnd = new Random();
    }
    @Override public void turn(){
	if(this.hasTurn()) {
            AbstractPokermoves aca = game.getOptions();
            if (hand.isEmpty()) {
                int betOption = rnd.nextInt(3);
                switch(betOption){
                    case 0:
                        aca.makeMove(BlackJackAction.BET_25);
                        break;
                    case 1:
                        aca.makeMove(BlackJackAction.BET_50);
                        break;
                    case 2:
                        aca.makeMove(BlackJackAction.BET_75);
                        break;
                    default:
                        aca.makeMove(BlackJackAction.BET_25);
                }
                aca.makeMove(BlackJackAction.BET);
	    } else {
		BlackJackAction action = getBestMove();
		game.getOptions().makeMove(action);
	    }
        }
        }


    private BlackJackAction getBestMove() {
        boolean soft = !handIsHard();
        int dealerHandValue = game.getDealer().getHand().getCard(0).getCardInt();
        if(10 < dealerHandValue ) dealerHandValue =10;
        int playerHandvalue = blackJack.getLegalHandSum(this);
	return getAction(dealerHandValue, playerHandvalue, soft);
    }

    private BlackJackAction getAction(final int dealerHandValue, final int playerHandValue, final boolean soft) {
        BlackJackAction[][] optionArray;
        if(soft) optionArray  = buildSoftHandOptionArray();
        else optionArray = buildhardHandOptionArray();
        return optionArray[playerHandValue - 4][dealerHandValue - 2];
    }

    public void printOptionArray(){
        StringBuilder stringBuilder = new StringBuilder();
        BlackJackAction[][] hej = buildhardHandOptionArray();
        int i= 4;
        for (BlackJackAction[] blackJackAction : hej) {
            stringBuilder.append(i+":");
            for (BlackJackAction jackAction : blackJackAction) {
                stringBuilder.append("|" +jackAction +"|");
            }
            stringBuilder.append("\n");
            i++;
        }
        System.out.println(stringBuilder);
    }

    public BlackJackAction[][] buildhardHandOptionArray() {
        BlackJackAction[][] optionArray = new BlackJackAction[18][10];
        int dealerHandValue = 2;
        int playerHandValue = 4;
        for (int c = 0; c < optionArray.length; c++) {
            for (int r = 0; r < optionArray[c].length; r++) {
                BlackJackAction actionBlock = BlackJackAction.STAND;
                if (playerHandValue <= 11) actionBlock = BlackJackAction.HIT;
                if (playerHandValue <= 3) actionBlock = BlackJackAction.HIT;
                if (7 <= dealerHandValue && 12 <= playerHandValue &&  playerHandValue <= 16) actionBlock = BlackJackAction.HIT;
                if(dealerHandValue <= 3 && playerHandValue == 12) actionBlock = BlackJackAction.HIT;
                if(9 <= dealerHandValue && playerHandValue == 16 || playerHandValue == 15 && dealerHandValue == 10) actionBlock = BlackJackAction.SURRENDER_HIT;
                if(playerHandValue == 9 && 3<= dealerHandValue && dealerHandValue <=6) actionBlock = BlackJackAction.DOUBLE_HIT;
                if(playerHandValue == 10 && dealerHandValue <=9) actionBlock = BlackJackAction.DOUBLE_HIT;
                if(playerHandValue  == 11 && dealerHandValue <=10) actionBlock = BlackJackAction.DOUBLE_HIT;

                dealerHandValue++;
                optionArray[c][r] = actionBlock;
            }
            dealerHandValue = 2;
            playerHandValue++;
        }
        return optionArray;
    }
    private BlackJackAction[][] buildSoftHandOptionArray(){
        BlackJackAction[][] optionArray = new BlackJackAction[18][10];
        int dealerHandValue = 2;
        int playerHandValue = 4;
        for (int c = 0; c < optionArray.length; c++) {
            for (int r = 0; r < optionArray[c].length; r++) {
                BlackJackAction actionBlock = BlackJackAction.STAND;
                if (playerHandValue <= 17 || playerHandValue == 18 && 9 <= dealerHandValue) actionBlock = BlackJackAction.HIT;
                if(dealerHandValue <=6) {
                    if (playerHandValue == 18 && 3 <= dealerHandValue && dealerHandValue <= 6) actionBlock = BlackJackAction.DOUBLE_STAND;
                    if (playerHandValue == 17 && 3 <= dealerHandValue) actionBlock = BlackJackAction.DOUBLE_HIT;
                    if((playerHandValue == 15 || playerHandValue == 16) && 4 <= dealerHandValue) actionBlock = BlackJackAction.DOUBLE_HIT;
                    if((playerHandValue == 13 || playerHandValue == 14) && 5 <= dealerHandValue ) actionBlock = BlackJackAction.DOUBLE_HIT;
                }
                dealerHandValue++;
                optionArray[c][r] = actionBlock;
            }
            dealerHandValue = 2;
            playerHandValue++;
        }
        return optionArray;
    }

    private boolean handIsHard() {
        return hand.containsCardValue(CardValue.TOP_ACE);
    }

    @Override public void setGame(final PokerGame game) {
        this.blackJack = (BlackJack) game;
    }
}
