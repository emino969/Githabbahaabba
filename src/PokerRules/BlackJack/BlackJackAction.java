package PokerRules.BlackJack;

import PokerRules.CardGameAction;

public enum BlackJackAction implements CardGameAction
{
    HIT,
    STAND,
    SPLIT,
    DOUBLE,
    BET_25,
    BET_50,
    BET_75,
    DOUBLE_HIT,
    DOUBLE_STAND,
    DOUBLE_SURRENDER,
    SURRENDER,
    SURRENDER_HIT, BET, RESET_BET;

    BlackJackAction() {

    }

    @Override public String toString() {
        String enumString = super.toString();
        String replacement =  String.valueOf(enumString.charAt(0));
        String lowerString = enumString.toLowerCase();
        lowerString = lowerString.replaceFirst(String.valueOf(lowerString.charAt(0)),replacement);
        return lowerString;
    }

}
