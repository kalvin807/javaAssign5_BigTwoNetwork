

/**
 * A subclass of the Hand class, and are used to model a hand of quad in a Big
 * Two card game.
 */
public class Quad extends Hand {
    /** A constructor for building a quad hand */
    public Quad(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

    /**
     * A method for retrieving the top card of this hand.
     * 
     * @return the top card of this hand
     */

    @Override
    public Card getTopCard() {
        if (getCard(0).compareTo(getCard(1)) == 0)
            return getCard(0);
        else
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
            if (hand.getType() == "StraightFlush" || hand.getType() == "Quad") {
                if (hand.getType() == "Quad")
                    if (this.getTopCard().compareTo(((Quad) hand).getTopCard()) > 0)
                        return true;
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * A method for checking if this is a valid hand.
     * 
     * @return True = this hand is a vaild quad. False = this is NOT a vaild quad.
     */

    @Override
    public boolean isValid() {
        if (size() == 5) {
            int count1 = 0;
            int count2 = 0;
            for (int i = 0; i < size(); i++) {
                if (getCard(i).getRank() == getCard(0).getRank())
                    count1++;
                if (getCard(i).getRank() == getCard(4).getRank())
                    count2++;
            }
            if (count1 == 4 || count2 == 4)
                return true;
        }
        return false;
    }

    /**
     * A method for returning a string specifying quad.
     * 
     * @return String "Quad"
     */

    @Override
    public String getType() {
        return "Quad";
    }
}