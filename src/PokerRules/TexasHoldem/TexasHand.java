package pokerrules.texasholdem;
/**
 *
 */
public enum TexasHand
{
    /**
     *
     */
    HIGH_CARD,
    /**
     *
     */
    PAIR,
    /**
     *
     */
    TWO_PAIR,
    /**
     *
     */
    TRIPLETS,
    /**
     *
     */
    STRAIGHT,
    /**
     *
     */
    FLUSH,
    /**
     *
     */
    FULL_HOUSE,
    /**
     *
     */
    QUADS,
    /**
     *
     */
    STRAIGHT_FLUSH,
    /**
     *
     */
    ROYAL_FLUSH,
    /**
     *
     */
    EMPTY;

    @Override public String toString()	{
	switch(this)	{
	    case HIGH_CARD:
		return "High Card";
	    case PAIR:
		return "One Pair";
	    case TWO_PAIR:
		return "Two Pair";
	    case TRIPLETS:
		return "Triplets";
	    case STRAIGHT:
		return "Straight";
	    case FLUSH:
		return "Flush";
	    case FULL_HOUSE:
		return "Full house";
	    case QUADS:
		return "Quads";
	    case STRAIGHT_FLUSH:
		return "Straight flush";
	    case ROYAL_FLUSH:
		return "Royal flush";
	    case EMPTY:
		return "Nothing";
	    default:
		return "";
	}
    }
}
