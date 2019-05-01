
/**
 * A subclass of the Hand class, and are used to model a hand of straight in a
 * Big Two card game.
 */
public class Straight extends Hand {
    /** A constructor for building a straight hand */
    public Straight(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

    /**
     * A method for retrieving the top card of this hand.
     * 
     * @return the top card of this hand
     */
    @Override
    public Card getTopCard() {
        if (AKQJ10() == true) {
            return getCard(0);
        }
        return getCard(4);
    }

    /**
     * A method for checking if this hand beats a specified hand.
     * 
     * @return True = this hand larger False = this hand smaller
     */

    @Override
    public boolean beats(Hand hand) {
        if (size() == 5) {
            if (hand.getType() == "Straight")
                if (this.getTopCard().compareTo(((Straight) hand).getTopCard()) > 0)
                    return true;
            return false;
        }
        return true;
    }

    /**
     * A method for checking if this is a valid hand.
     * 
     * @return True = this hand is a vaild straight. False = this is NOT a vaild
     *         straight.
     */
    @Override
    public boolean isValid() {
        if (size() == 5) {
            if (AKQJ10() == true) {
                return true;
            }    else {
                for (int i = 0; i < 5 - 1; i++) {
                    if (getCard(i).getRank() - getCard(i + 1).getRank() != -1)
                        return false;
                }
                return true;
            }
        }
        return false;

    }

    /**
     * A method for returning a string specifying straight.
     * 
     * @return String "Straight"
     */

    public String getType() {
        return "Straight";
    }

    // Helper method to identify AKQJ10
    private boolean AKQJ10() {
        if (getCard(0).getRank() == 9 && getCard(1).getRank() == 10 && getCard(2).getRank() == 11
                && getCard(3).getRank() == 12 && getCard(4).getRank() == 0) {
            return true;
        }
        return false;
    }
}