package PokerRules;

import java.util.ArrayList;
import Person.*;
import Table.CardGame;

public interface AbstractPokermoves
{
    public ArrayList<CardGameMove> getOptions(Person person);
    public void makeMove(CardGameMove cardGameMove);
    public String getHandValue(Person person);
}
