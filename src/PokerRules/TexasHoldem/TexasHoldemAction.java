package PokerRules.TexasHoldem;

import PokerRules.CardGameAction;

public enum TexasHoldemAction implements CardGameAction
{
    CALL,
    BET_25,
    BET_50,
    BET_75,
    RAISE,
    FOLD,;

    TexasHoldemAction()	{}

    @Override public String toString() {
	switch(this)	{
	    case CALL:
		return "Call";
	    case BET_25:
		return "25$";
	    case BET_50:
		return "50$";
	    case BET_75:
		return "75$";
	    case RAISE:
		return "Raise";
	    case FOLD:
	    	return "Fold";
	    default:
		return "";
	}
    }
}
