package tests;

import GUI.PokerFrame;
import pokerrules.AbstractGame;
import pokerrules.texasholdem.TexasHoldem;

public final class RunGame
{
    private RunGame() {}

    public static void main(String[] args) {
	AbstractGame texasHoldem = new TexasHoldem();
	PokerFrame frame = new PokerFrame(texasHoldem, "Guest");
	frame.setVisible(true);
    }
}