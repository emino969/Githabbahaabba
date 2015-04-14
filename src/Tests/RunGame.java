package Tests;

import GUI.PokerFrame;
import PokerRules.BlackJack.BlackJack;

public final class RunGame
{
    private RunGame() {}

    public static void main(String[] args) {
	PokerFrame frame = new PokerFrame(new BlackJack());
	frame.setVisible(true);
    }
}