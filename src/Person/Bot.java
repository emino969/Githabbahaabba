package Person;

import Money.Pot;
import pokerrules.AbstractPokermoves;
import pokerrules.CardGameAction;

import java.util.Random;

public class Bot extends Person
{
    public Bot(String name, Pot pot)	{
	super(name, pot);
    }

    @Override public void turn()	{
	AbstractPokermoves abstractPokermoves = game.getOptions();
	Random rand = new Random();
	CardGameAction nextMove = abstractPokermoves.getOptions(this).get(rand.nextInt(abstractPokermoves.getOptions(this).size()));
	abstractPokermoves.makeMove(nextMove);
    }

    private int betAmount() {
        return 100;
    }
}
