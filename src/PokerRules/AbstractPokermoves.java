package pokerrules;

import person.Person;

import java.util.List;

/**
 * The methods all player needs to make a move
 */


public interface AbstractPokermoves
{
    public List<CardGameAction> getOptions(Person person);
    public void makeMove(CardGameAction cardGameAction);
    public String getHandValue(Person person);
}
