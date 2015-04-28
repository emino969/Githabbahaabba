package Person.BotTypes;

import Cards.CardValue;
import Money.Pot;
import Person.Person;
import PokerRules.AbstractGame;
import PokerRules.AbstractPokermoves;
import PokerRules.BlackJack.BlackJack;
import PokerRules.BlackJack.BlackJackAction;

import java.util.Random;

/**
 * this blackjackBot is based on basic strategy 4-8decks fpimd at
 * http://wizardofodds.com/games/blackjack/strategy/4-decks/ 13.14.2015
 */
public class BlackJackBot extends Person
{
    private BlackJack blackJack;
    private Random rnd;
    private BlackJackAction[][]splitArray;
    private BlackJackAction[][] softHandArray;
    private BlackJackAction[][] hardHandArray;
    private static final int HV_1 = 1;
    private static final int HV_2 = 2;
    private static final int HV_3 = 3;
    private static final int HV_4 = 4;
    private static final int HV_5 = 5;
    private static final int HV_6 = 6;
    private static final int HV_7 = 7;
    private static final int HV_8 = 8;
    private static final int HV_9 = 9;
    private static final int HV_10 = 10;
    private static final int HV_11 = 11;
    private static final int HV_12 = 12;
    private static final int HV_13 = 13;
    private static final int HV_14 = 14;
    private static final int HV_15 = 15;
    private static final int HV_16 = 16;
    private static final int HV_17 = 17;
    private static final int HV_18 = 18;
    private static final int HV_19 = 19;
    private static final int HV_20 = 20;
    private static final int HV_21 = 21;




    public BlackJackBot(final String name, final Pot pot, final BlackJack blackJack) {
        super(name, pot);

        this.blackJack = blackJack;
        this.game = blackJack;
        this.rnd = new Random();
        this.softHandArray = buildSoftHandOptionArray();
        this.hardHandArray = buildhardHandOptionArray();
        this.splitArray = buildSplitActionArray();
    }
    @Override public void turn(){
	if(this.hasTurn()) {
            printOptionArrays();
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
        int dealerHandValue = game.getDealer().getHand().getCard(0).getCardIntValue();
        if(10 < dealerHandValue ) dealerHandValue = 10;
        int playerHandvalue = hand.getLegalHandSum();
	return getAction(dealerHandValue, playerHandvalue, soft);
    }

    private BlackJackAction getAction(final int dealerHandValue, final int playerHandValue, final boolean soft) {
        BlackJackAction[][] optionArray;
        if(blackJack.isSplittable(this)){
            optionArray = splitArray;
            return optionArray[hand.getCard(0).getCardIntValue() - HV_2][dealerHandValue - HV_2];
        }
        else if (soft) optionArray  = softHandArray;
        else optionArray = buildhardHandOptionArray();
        return optionArray[playerHandValue - HV_4][dealerHandValue - HV_2];
    }

    public void printOptionArray(BlackJackAction[][] optionArray){
        StringBuilder stringBuilder = new StringBuilder();
        int i= 2;
        for (BlackJackAction[] blackJackAction : optionArray) {
            stringBuilder.append(i+":");
            for (BlackJackAction jackAction : blackJackAction) {
                stringBuilder.append("|" +jackAction +"|");
            }
            stringBuilder.append("\n");
            i++;
        }
        System.out.println(stringBuilder);
    }
    public void printOptionArrays(){
        printOptionArray(buildhardHandOptionArray());
        System.out.println();
        printOptionArray(buildSoftHandOptionArray());
        System.out.println();
        printOptionArray(buildSplitActionArray());
    }
    private BlackJackAction[][] buildSplitActionArray() {

        BlackJackAction[][] optionArray = new BlackJackAction[12][13];
        int dealerHandValue = HV_2;
        int playerCardValue = HV_2;
        for (int c = 0; c < optionArray.length; c++) {
            for (int r = 0; r < optionArray[c].length; r++) {
                BlackJackAction actionBlock = BlackJackAction.SPLIT;
                if (playerCardValue == HV_4) actionBlock = BlackJackAction.HIT;
                if (dealerHandValue == HV_7 && playerCardValue == HV_6) actionBlock = BlackJackAction.HIT;
                if (playerCardValue <= HV_7 && HV_8 <= dealerHandValue) actionBlock = BlackJackAction.HIT;
                if (playerCardValue == HV_9 && dealerHandValue == HV_7) actionBlock = BlackJackAction.STAND;
                if (playerCardValue == HV_9 && HV_10 <= dealerHandValue) actionBlock = BlackJackAction.STAND;
                if(HV_10 < playerCardValue && playerCardValue != HV_14) actionBlock = BlackJackAction.HIT;
                dealerHandValue++;
                optionArray[c][r] = actionBlock;
            }
            dealerHandValue = HV_2;
            playerCardValue++;
        }
        return optionArray;
    }


    private BlackJackAction[][] buildSoftHandOptionArray(){
        BlackJackAction[][] optionArray = new BlackJackAction[HV_18][HV_10];
        int dealerHandValue = HV_2;
        int playerHandValue = HV_4;
        for (int c = 0; c < optionArray.length; c++) {
            for (int r = 0; r < optionArray[c].length; r++) {
                BlackJackAction actionBlock = BlackJackAction.STAND;
                if (playerHandValue <= HV_17 || playerHandValue == HV_18 && HV_9 <= dealerHandValue) actionBlock = BlackJackAction.HIT;
                if(dealerHandValue <=HV_6) {
                    if (playerHandValue == HV_18 && HV_3 <= dealerHandValue && dealerHandValue <= HV_6) actionBlock = BlackJackAction.DOUBLE_STAND;
                    if (playerHandValue == HV_17 && HV_3 <= dealerHandValue) actionBlock = BlackJackAction.DOUBLE_HIT;
                    if((playerHandValue == HV_15 || playerHandValue == HV_16) && HV_4 <= dealerHandValue) actionBlock = BlackJackAction.DOUBLE_HIT;
                    if((playerHandValue == HV_13 || playerHandValue == HV_14) && HV_5 <= dealerHandValue ) actionBlock = BlackJackAction.DOUBLE_HIT;
                }
                dealerHandValue++;
                optionArray[c][r] = actionBlock;
            }
            dealerHandValue = HV_2;
            playerHandValue++;
        }
        return optionArray;
    }
    public BlackJackAction[][] buildhardHandOptionArray() {
        BlackJackAction[][] optionArray = new BlackJackAction[HV_18][HV_10];
        int dealerHandValue = HV_2;
        int playerHandValue = HV_4;
        for (int c = 0; c < optionArray.length; c++) {
            for (int r = 0; r < optionArray[c].length; r++) {
                BlackJackAction actionBlock = BlackJackAction.STAND;
                if (playerHandValue <= HV_11) actionBlock = BlackJackAction.HIT;
                if (playerHandValue <= HV_3) actionBlock = BlackJackAction.HIT;
                if (HV_7 <= dealerHandValue && HV_12 <= playerHandValue &&  playerHandValue <= HV_16) actionBlock = BlackJackAction.HIT;
                if(dealerHandValue <= HV_3 && playerHandValue == HV_12) actionBlock = BlackJackAction.HIT;
                if(HV_9 <= dealerHandValue && playerHandValue == HV_16 || playerHandValue == HV_15 && dealerHandValue == HV_10) actionBlock = BlackJackAction.SURRENDER_HIT;
                if(playerHandValue == HV_9 && HV_3 <= dealerHandValue && dealerHandValue <= HV_6) actionBlock = BlackJackAction.DOUBLE_HIT;
                if(playerHandValue == HV_10 && dealerHandValue <= HV_9) actionBlock = BlackJackAction.DOUBLE_HIT;
                if(playerHandValue  == HV_11 && dealerHandValue <=HV_10) actionBlock = BlackJackAction.DOUBLE_HIT;

                dealerHandValue++;
                optionArray[c][r] = actionBlock;
            }
            dealerHandValue = HV_2;
            playerHandValue++;
        }
        return optionArray;
    }


    private boolean handIsHard() {
        return hand.containsCardValue(CardValue.TOP_ACE);
    }

    @Override public void setGame(final AbstractGame game) {
        this.blackJack = (BlackJack) game;
    }
}
