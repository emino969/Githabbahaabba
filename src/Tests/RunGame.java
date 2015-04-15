package Tests;

import GUI.PokerFrame;
import PokerRules.TexasHoldem.*;

public final class RunGame
{
    private RunGame() {}

    public static void main(String[] args) {
	TexasHoldem texas = new TexasHoldem(10);
	PokerFrame frame = new PokerFrame(texas.getGame());
	frame.setVisible(true);
    }
}