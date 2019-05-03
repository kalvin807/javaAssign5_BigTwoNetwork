
/**
 * The Hand class is a subclass of the CardList class, and is used to model a
 * hand of cards. It has a private instance variable for storing the player who
 * plays this hand. It also has methods for getting the player of this hand,
 * checking if it is a valid hand, getting the type of this hand, getting the
 * top card of this hand, and checking if it beats a specified hand.
 */
public abstract class Hand extends CardList {
    /** The player who plays this hand */
    private CardGamePlayer player;
    /**
     * A constructor for building a hand with the specified player and list of
     * cards.
     */

    public Hand(CardGamePlayer player, CardList cards) {
        this.player = player;
        for (int i = 0; i < cards.size(); i++) {
            super.addCard(cards.getCard(i));
        }
        super.sort();
    }

    // Empty constructor to prevent bug.
    public Hand() {
    }

    /**
     * A method for retrieving the player of this hand.
     * 
     * @return the player of this hand
     */
    public CardGamePlayer getPlayer() {
        return player;
    }

    /**
     * A method for retrieving the top card of this hand.
     * 
     * @return the top card of this hand
     */
    public Card getTopCard() {
        return this.getCard(size() - 1);
    }

    /**
     * A method for checking if this hand beats a specified hand.
     * 
     * @param hand The hand that going to be compared.
     * @return the result of the comparsion. True = this hand larger. False = this
     *         hand smaller.
     */
    public boolean beats(Hand hand) {
        if (this.size() == hand.size()) {
            Card play = (BigTwoCard) this.getTopCard();
            Card table = (BigTwoCard) hand.getTopCard();
            if (play.compareTo(table) > 0)
                return true;
        }
        return false;
    }

    /** A method for checking if this is a valid hand */
    public abstract boolean isValid();

    /** A method for checking if this is a valid hand */
    public abstract String getType();
}
