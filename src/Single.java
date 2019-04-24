@SuppressWarnings("serial")

/**
 * A subclass of the Hand class, and are used to model a hand of single in a Big
 * Two card game.
 */
public class Single extends Hand {

    /** A constructor for building a single hand */
    public Single(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

    /**
     * A method for checking if this is a valid hand.
     * 
     * @return True = this hand is a vaild single. False = this is NOT a vaild
     *         single.
     */
    @Override
    public boolean isValid() {
        return (super.size() == 1 ? true : false);
    }

    /**
     * A method for returning a string specifying single.
     * 
     * @return String "Single"
     */
    @Override
    public String getType() {
        return "Single";
    }

}