
/**
 * The BigTwoCard class is a subclass of the Card class, and is used to model a
 * card used in a Big Two card game.
 */
public class BigTwoCard extends Card {
    /**
     * A constructor for building a card with the specified suit and rank.
     */
    public BigTwoCard(int suit, int rank) {
        super(suit, rank);
    }

    /**
     * A method for comparing the order of this card with the specified card.
     * Returns a negative integer, zero, or a positive integer as this card is less
     * than, equal to, or greater than the specified card
     * 
     * @return the result of comparsion, +1 = larger 0 = equal to, -1 = less than
     *         (the specific hand)
     */
    @Override
    public int compareTo(Card card) {
        int rankA = reRank(this.getRank());
        int rankB = reRank(card.getRank());
        int suitA = this.getSuit();
        int suitB = card.getSuit();
        if (rankA > rankB)
            return 1;
        else if (rankA < rankB)
            return -1;
        else {
            if (suitA > suitB)
                return 1;
            else if (suitA < suitB)
                return -1;
            else
                return 0;
        }
    }

    /**
     * Helper method to rearrange the rank order to fit BigTwo game rule.
     * 
     * @param rank the orginal rank
     * @return the correct rank in BigTwo game.
     */
    private int reRank(int rank) {
        if (rank == 0)
            rank = 13;
        if (rank == 1)
            rank = 14;
        return rank;
    }
}