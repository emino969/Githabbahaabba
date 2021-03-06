package Table;

import GameListeners.GameListener;
import Money.Pot;
import Person.Dealer;
import Person.Person;
import Person.PersonState;
import Person.Player;
import java.util.ArrayList;
import java.util.List;

public class Table
{
    static private List<GameListener> ListenerArray = new ArrayList<GameListener>();
    protected ArrayList<Person> players;
    private Pot tablePot;
    protected Dealer dealer = null;

    public Table() {
	this.players = new ArrayList<Person>();
        this.tablePot = new Pot(0);
    }

    static public void addGameListener(GameListener gl)	{
	ListenerArray.add(gl);
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

    public ArrayList<Person> getActivePlayers()	{
	//Returns all the active Players
	ArrayList<Person> persons = new ArrayList<Person>();
	for	(Person person : players)	{
	    if	(!(person.isPersonState(PersonState.INACTIVE) || person.isPersonState(PersonState.LOSER) ||
	    person.isPersonState(PersonState.WINNER))) {
		persons.add(person);
	    }
	}
	return persons;
    }

    public Iterable<Person> getOnlyPlayers()	{//Everyone besides dealer
	ArrayList<Person> tempPlayers = new ArrayList<Person>(players);
	tempPlayers.remove(dealer);
	return tempPlayers;
    }

    public ArrayList<Person> getOnlyActivePlayers()	{
	ArrayList<Person> persons = getActivePlayers();
	persons.remove(dealer);
	return persons;
    }

    public void removePlayer(Person player)	{
	players.remove(player);
    }

    public Iterable<Person> getPlayers()	{
	return new ArrayList<Person>(players);
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
	for(GameListener gl : ListenerArray)	{
	    gl.gameChanged();
	}
    }

}
