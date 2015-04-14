package CardGameExceptions;

public class NoSuchPlayer extends Exception
{
    public NoSuchPlayer(final String message) {
	super(message);
    }
}