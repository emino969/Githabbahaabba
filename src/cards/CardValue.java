package cards;
/**
 * cardvalue has 15 values 1 - 14 and a dont care  value used to represent any of these values;
 */
public enum CardValue
{
    /**
     * The ace do in some cases have the value of 1, this is the bottom ace
     */
    BOTTOM_ACE,
    /**
     *
     */
    TWO,
    /**
     *
     */
    THREE,
    /**
     *
     */
    FOUR,
    /**
     *
     */
    FIVE,
    /**
     *
     */
    SIX,
    /**
     *
     */
    SEVEN,
    /**
     *
     */
    EIGHT,
    /**
     *
     */
    NINE,
    /**
     *
     */
    TEN,
    /**
     * king has value 11
     */
    JACK,
    /**
     * king has value 12
     */
    QUEEN,
    /**
     * king has value 13
     */
    KING,
    /**
     * nomrally the ace has the value of 14, this is the top ace.
     */
    TOP_ACE,
    /**
     * any of the above values
     */
    DONT_CARE

}
