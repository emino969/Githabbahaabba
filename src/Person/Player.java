package Person;

import card.CardList;
import Money.Pot;

import javax.swing.*;

public class Player extends Person
{
    public Player(String name, Pot pot) {
	super(name, pot);
    }

    @Override public void quitGame()	{
	JOptionPane.showMessageDialog(null, "YOUR OUT OF MONEY");
	pot = new Pot(1000);
	hand = new CardList();
	clearAllHands();
	mappedHands.put(hand, PersonState.WAITING);
	game.getDealer().getPot().addAmount(getLastBet());
	setLastBet(0);
	getBet();
	changePersonState(PersonState.LOSER);
    }

    public void turn()	{
	/** Välj ett drag */
	System.out.println(this.getBestState());
    }
}
