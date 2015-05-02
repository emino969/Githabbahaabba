package tests;

import gui.StartMenu;

public final class RunMenu
{
    private RunMenu() {}

    public static void main(String[] args) {
	StartMenu menuFrame = new StartMenu();
	menuFrame.setVisible(true);
    }
}
