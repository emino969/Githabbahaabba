package PokerRules.TexasHoldem;

import PokerRules.AbstractPokermoves;

public interface HoldemMoves extends AbstractPokermoves
{
    public void raise();
    public void call();
    public void fold();
}
