@SuppressWarnings("serial")
/**
 * A subclass of the Hand class, and are used to model a hand of pair in a Big
 * Two card game.
 */
public class Pair extends Hand {
    /** A constructor for building a pair hand */
    public Pair(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

    /**
     * A method for checking if this is a valid hand.
     * 
     * @return True = this hand is a vaild pair. False = this is NOT a vaild pair.
     */
    @Override
    public boolean isValid() {
        if ((size() == 2)) {
            if (getCard(0).getRank() == getCard(1).getRank()) {
                return true;
            }
        }
        return false;
    }

    /**
     * A method for returning a string specifying pair.
     * 
     * @return String "Pair"
     */

    @Override
    public String getType() {
        return "Pair";
    }
}