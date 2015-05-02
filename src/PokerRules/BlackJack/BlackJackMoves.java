package pokerrules.blackjack;

import pokerrules.AbstractPokermoves;
/**
 *
 */
public interface BlackJackMoves extends AbstractPokermoves
{
    public void hit();
    public void stand();
    public void doubleDown();
    public void surrender();
    public void split();
}
