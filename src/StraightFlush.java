
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
        CardGamePlayer temp = super.getPlayer();
        Straight straight = new Straight(temp, temp.getCardsInHand());
        Flush flush = new Flush(temp, temp.getCardsInHand());
        if (straight.isValid() == true && flush.isValid() == true) {
            return true;
        }
        return false;
    }

    public String getType() {
        return "StraightFlush";
    }
}