package pokerrules.texasholdem;

import pokerrules.AbstractPokermoves;

public interface HoldemMoves extends AbstractPokermoves
{
    public void raise();
    public void call();
    public void fold();
}
