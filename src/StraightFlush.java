
public class StraightFlush extends Hand {
    public StraightFlush(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

    @Override
    public Card getTopCard() {
        CardGamePlayer temp = super.getPlayer();
        Straight straight = new Straight(temp, temp.getCardsInHand());
        return straight.getTopCard();
    }

    public boolean beats(Hand hand) {
        if (size() == 5) {
            if (hand.getType() == "StraightFlush") {
                if (this.getTopCard().compareTo(((StraightFlush) hand).getTopCard()) > 0)
                    return true;
            }
            return true;
        }
        return false;
    }

    public boolean isValid() {
        if (this.size() != 5)
            return false;
        for (int x = 0; x < 4; x++) {
            if ((getCard(x).getRank() - getCard(x + 1).getRank() != -1)
                    && (getCard(x).getRank() - getCard(x + 1).getRank() != 12))
                return false;
        }
        for (int x = 0; x < 4; x++) {
            if (getCard(x).getSuit() != getCard(x + 1).getSuit())
                return false;
        }
        return true;
    }

    public String getType() {
        return "StraightFlush";
    }
}