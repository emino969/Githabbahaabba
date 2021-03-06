package PokerRules;

import CardGameExceptions.CardGameActionException;
import Person.Person;
import java.util.List;

/**
 * The methods all player needs to make a move
 */

public interface AbstractPokermoves
{
    public List<CardGameAction> getOptions(Person person);
    public void makeMove(CardGameAction cardGameAction) throws CardGameActionException;
    public String getHandValue(Person person);
}
