package gui.components;

import gamelisteners.GameListener;
import gui.PlayerFrame;
import gui.PokerFrame;
import Person.Person;
import pictures.Images;
import pokerrules.AbstractGame;
import table.Table;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
/**
 *
 */
public class PokerComponent extends JComponent
{
    private final PlayerFrame playerFrame;
    private AbstractGame game;
    private PokerFrame frame; //Temporarily changed
    private TableComponent tableComponent;
    private Images imageHandler;
    private static final int X_CONST = 140;
    private static final int Y_CONST = 90;
    private static final int ADJ_CONST = 50;

    public PokerComponent(AbstractGame game, final PlayerFrame playerFrame, PokerFrame frame)	{
	this.game = game;
	this.frame = frame;
	this.playerFrame = playerFrame;
	this.imageHandler = new Images();
	this.tableComponent = buildTableComponent();
	GameListener gl = new GameListener()	{
	    @Override public void gameChanged()	{
		repaint();
	    }
	};
	Table.addGameListener(gl);
    }

    private TableComponent buildTableComponent() {
	ArrayList<PlayerComponent> players = new ArrayList<>();
	for (Person player : game.getOnlyPlayers()) {
	    players.add(new PlayerComponent(player, imageHandler));
	}
	TableComponent table = new TableComponent(players, this,
						  new DealerComponent(game.getDealer(), imageHandler),
						  frame.getWidth()/4 - X_CONST,
						  frame.getHeight()/3 - Y_CONST,
						  frame.getWidth()/2+ X_CONST * 2,
						  frame.getHeight()/3 + ADJ_CONST * 2,
						  imageHandler);
	return table;
    }

    @Override protected void paintComponent(Graphics g){
	super.paintComponent(g);
	playerFrame.updateLabels();
	tableComponent.drawTable(g);
    }
}
