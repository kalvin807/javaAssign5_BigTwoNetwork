@SuppressWarnings("serial")
/**
 * A subclass of the Hand class, and are used to model a hand of flush in a Big
 * Two card game.
 */
public class Flush extends Hand {

    /** A constructor for building a flush hand */
    public Flush(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

    /**
     * A method for checking if this hand beats a specified hand.
     * 
     * @return True = this hand larger False = this hand smaller
     */
    public boolean beats(Hand hand) {
        if (size() == 5) {
            if (hand.getType() == "Flush")
                return super.beats(hand);
            else if (hand.getType() == "Straight")
                return true;
            else
                return false;
        } else
            return false;
    }

    /**
     * A method for checking if this is a valid hand.
     * 
     * @return True = this hand is a vaild flush. False = this is NOT a vaild flush.
     */
    @Override
    public boolean isValid() {
        if (size() == 5) {
            for (int i = 1; i < size(); i++) {
                if (getCard(i).getSuit() != getCard(0).getSuit())
                    return false;
            }
            return true;
        } else
            return false;
    }

    /**
     * A method for returning a string specifying flush.
     * 
     * @return String "Flush"
     */
    @Override
    public String getType() {
        return "Flush";
    }
}
