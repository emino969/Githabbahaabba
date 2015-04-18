package GUI;

import GUI.Components.DealerComponent;
import GUI.Components.PlayerComponent;
import GUI.Components.TableComponent;
import GameListeners.GameListener;
import Person.Person;
import GUI.Pictures.Images;
import Table.PokerGame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PokerComponent extends JComponent
{
    private final PlayerFrame playerFrame;
    private PokerGame game;
    private PokerFrame frame; //Temporarily changed
    private TableComponent tableComponent;
    private Images imageHandler;
    private static final int X_CONST = 70;
    private static final int Y_CONST = 90;
    private static final int ADJ_CONST = 50;

    public PokerComponent(PokerGame game, final PlayerFrame playerFrame, PokerFrame frame)	{
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
	PokerGame.addGameListener(gl);
    }

    private TableComponent buildTableComponent() {
	ArrayList<PlayerComponent> players = new ArrayList<PlayerComponent>();
	for (Person player : game.getOnlyPlayers()) {
	    players.add(new PlayerComponent(player, imageHandler));
	}
	TableComponent table = new TableComponent(players, this,
						  new DealerComponent(game.getDealer(), imageHandler),
						  frame.getWidth()/4 - X_CONST,
						  frame.getHeight()/3 - Y_CONST,
						  frame.getWidth()/2 + X_CONST * 2,
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
