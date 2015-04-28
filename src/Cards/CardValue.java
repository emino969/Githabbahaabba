package Cards;

public enum CardValue
{
    BOTTOM_ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, TOP_ACE,DONT_CARE;

    public static CardValue getValueFromInt(int n) {
	switch (n) {
	    case 1:
		return BOTTOM_ACE;
	    case 2:
		return TWO;
	    case 3:
		return THREE;
	    case 4:
		return FOUR;
	    case 5:
		return FIVE;
	    case 6:
		return SIX;
	    case 7:
		return SEVEN;
	    case 8:
		return EIGHT;
	    case 9:
		return NINE;
	    case 10:
		return TEN;
	    case 11:
		return JACK;
	    case 12:
		return QUEEN;
	    case 13:
		return KING;
	    case 14:
		return TOP_ACE;
	    default:
		return DONT_CARE;


	}
    }
}
