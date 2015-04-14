package Tests;

import GUI.PokerFrame;
import PokerRules.TexasHoldem.TexasHoldem;

public final class RunGame
{
    private RunGame() {}

    public static void main(String[] args) {
	PokerFrame frame = new PokerFrame(new TexasHoldem(10));
	frame.setVisible(true);
    }
}