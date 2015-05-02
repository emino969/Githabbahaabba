package table;

import GameListeners.GameListener;
import person.Dealer;
import person.Person;
import person.PersonState;
import person.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Table
{
    static private Collection<GameListener> listenerList = new ArrayList<>();
    protected ArrayList<Person> players;
    protected Dealer dealer = null;

    public Table() {
	this.players = new ArrayList<>();
    }

    static public void addGameListener(GameListener gl)	{
	listenerList.add(gl);
    }

    public int getPlayersSize()	{
	return players.size();
    }

    public Person getPersonByIndex(int index)	{
	return getActivePlayers().get(index);
    }

    public int getIndexByPerson(Person player)	{
	for (int i = 0; i < getActivePlayers().size(); i++) {
	    if	(getActivePlayers().get(i).equals(player))	{
		return i;
	    }
	}
	return -1;
    }

    public List<Person> getActivePlayers()	{
	//Returns all the active Players
	ArrayList<Person> persons = new ArrayList<>();
	for	(Person person : players)	{
	    if	(!(person.isPersonState(PersonState.INACTIVE) || person.isPersonState(PersonState.LOSER) ||
	    person.isPersonState(PersonState.WINNER))) {
		persons.add(person);
	    }
	}
	return persons;
    }

    public Iterable<Person> getOnlyPlayers()	{//Everyone besides dealer
	List<Person> tempPlayers = new ArrayList<>(players);
	tempPlayers.remove(dealer);
	return tempPlayers;
    }

    public Iterable<Person> getOnlyActivePlayers()	{
	List<Person> persons = new ArrayList<>();
	for	(Person person : players)	{
	    if	(!(person.isPersonState(PersonState.INACTIVE) || person.isPersonState(PersonState.LOSER) ||
	    person.isPersonState(PersonState.WINNER))) {
		persons.add(person);
	    }
	}
	persons.remove(dealer);
	return persons;
    }

    public void removePlayer(Person player)	{
	players.remove(player);
    }

    public Iterable<Person> getPlayers()	{
	return new ArrayList<>(players);
    }

    public Dealer getDealer()	{
	return dealer;
    }

    public void activateDealer()	{
	dealer.changePersonState(PersonState.TURN);
    }

    public void activateWaitingDealer()	{dealer.changePersonState(PersonState.WAITING);}

    public void deactivateDealer()	{
	dealer.changePersonState(PersonState.INACTIVE);
    }

    public Player getPlayer()	{//Returns the player that is conrolled by you
	for(Person person : players)	{
	    if	(person.getClass().equals(Player.class))	{
		return (Player) person;
	    }
	}
	return null;
    }

    public void setDealer(Dealer dealer)	{
	this.dealer = dealer;
	players.add(dealer);
    }

    public void notifyListeners()	{
	for(GameListener gl : listenerList)	{
	    gl.gameChanged();
	}
    }

}
