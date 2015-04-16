package Tests;

import GUI.PokerFrame;
import PokerRules.BlackJack.BlackJack;
import PokerRules.TexasHoldEm.*;

public final class RunGame
{
    private RunGame() {}

    public static void main(String[] args) {
	BlackJack blackJack = new BlackJack();
	PokerFrame frame = new PokerFrame(blackJack);
	//PokerFrame frame = new PokerFrame(texas.getGame());
	frame.setVisible(true);
    }
}