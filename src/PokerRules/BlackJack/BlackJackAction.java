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

    BlackJackAction() {}

    @Override public String toString() {
        String string = super.toString();
        String replacement =  String.valueOf(string.charAt(0));
        String lowerString = string.toLowerCase();
        lowerString = lowerString.replaceFirst(String.valueOf(lowerString.charAt(0)),replacement);
        return lowerString;
    }

}
