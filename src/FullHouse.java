

/**
 * A subclass of the Hand class, and are used to model a hand of full house in a
 * Big Two card game.
 */

public class FullHouse extends Hand {
    /** A constructor for building a full house hand */
    public FullHouse(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

    /**
     * A method for retrieving the top card of this hand.
     * 
     * @return the top card of this hand
     */

    @Override
    public Card getTopCard() {
        if ((getCard(0).compareTo(getCard(1)) == 0) && (getCard(0).compareTo(getCard(2)) == 0)) {
            return getCard(0);
        }else return getCard(4);
    }

    /**
     * A method for checking if this hand beats a specified hand.
     * 
     * @return True = this hand larger False = this hand smaller
     */

    @Override
    public boolean beats(Hand hand) {
        if (size() == 5) {
            if (hand.getType() == "FullHouse") {
                Card topA = this.getTopCard();
                Card topB = ((FullHouse) hand).getTopCard();
                if (((BigTwoCard) topA).compareTo(topB) > 0)
                    return true;
                else
                    return false;
            } else if ((hand.getType() == "Straight") || (hand.getType() == "Flush"))
                return true;
            return false;
        } else
            return false;
    }

    /**
     * A method for checking if this is a valid hand.
     * 
     * @return True = this hand is a vaild full house. False = this is NOT a vaild
     *         full house.
     */
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
            if (count1 + count2 == 5)
                return true;
        }
        return false;
    }

    /**
     * A method for returning a string specifying full house.
     * 
     * @return String "FullHouse"
     */

    public String getType() {
        return "FullHouse";
    }
}