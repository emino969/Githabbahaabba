package PokerRules;

import java.util.ArrayList;
import Person.*;

public interface AbstractPokermoves
{
    public ArrayList<String> getOptions(Person person);
    public void makeMove(String name);
    public String getHandValue(Person person);
}
