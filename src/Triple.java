
/**
 * A subclass of the Hand class, and are used to model a hand of triple in a
 * Big Two card game.
 */
public class Triple extends Hand {
    /** A constructor for building a triple hand */
    public Triple(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }
    // Empty constuctor 
    public Triple() {
    }
       
    /**
     * A method for checking if this is a valid hand.
     * 
     * @return True = this hand is a vaild triple. False = this is NOT a vaild
     *         triple.
     */
    @Override
    public boolean isValid() {
        if (size() == 3) {
            if (getCard(0).getRank() == getCard(1).getRank() && (getCard(0).getRank() == getCard(2).getRank())) {
                return true;
            }
        }
        return false;
    }

    /**
     * A method for returning a string specifying triple.
     * 
     * @return String "Triple"
     */

    
    @Override
    public String getType() {
        return "Triple";
    }
}