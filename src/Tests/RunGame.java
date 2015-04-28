package Tests;

import GUI.PokerFrame;
import PokerRules.AbstractGame;
import PokerRules.TexasHoldem.TexasHoldem;

public final class RunGame
{
    private RunGame() {}

    public static void main(String[] args) {
	AbstractGame texasHoldem = new TexasHoldem();
	PokerFrame frame = new PokerFrame(texasHoldem, "Guest");
	frame.setVisible(true);
    }
}