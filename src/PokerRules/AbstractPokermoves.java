package PokerRules;

import Person.Person;

import java.util.List;

/**
 * The methods all player needs to make a move
 */

public interface AbstractPokermoves
{
    public List<CardGameMove> getOptions(Person person);
    public void makeMove(CardGameMove cardGameMove);
    public String getHandValue(Person person);
}
