import java.util.ArrayList;

/**
 * The BigTwo class implements the CardGame interface. It is used to model a Big
 * Two card game.
 */
public class BigTwo implements CardGame {
    /** A deck of cards. */
    private Deck deck = new Deck();
    /** A list of players. */
    private ArrayList<CardGamePlayer> playerList = new ArrayList<CardGamePlayer>();
    /** A list of hands played on table. */
    private ArrayList<Hand> handsOnTable = new ArrayList<Hand>();
    /** An integer specifying the index of the current player. */
    private int currentIdx;
    /**
     * A Big Two table which builds the GUI for the game and handles all user
     * actions
     */
    private BigTwoTable table;
    /** A boolean indicate this turn is the first turn */
    private boolean isFirst = true;
    /** A counter to counting number of player passed */
    private int passCounter = 0;

    /** A constructor for creating a Big Two card game */
    public BigTwo() {
        for (int i = 0; i < 4; i++) {
            CardGamePlayer player = new CardGamePlayer();
            playerList.add(player);
        }
        this.table = new BigTwoTable(this);
    }

    /**
     * A method forgetting the number of players
     * 
     * @return Number of players
     */
    public int getNumOfPlayers() {
        return playerList.size();
    }

    /**
     * A method for retrieving the deck of cards being used.
     * 
     * @return the deck being used.
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * A method for retrieving the list of players.
     * 
     * @return the list of CardGamePlayer.
     */
    public ArrayList<CardGamePlayer> getPlayerList() {
        return playerList;
    }

    /**
     * A method for retrieving the list of hands played on the table.
     * 
     * @return ArrayList of hands played on the table
     */
    public ArrayList<Hand> getHandsOnTable() {
        return handsOnTable;
    }

    /**
     * A method for retrieving the index of the current player.
     * 
     * @return current player index
     */
    public int getCurrentIdx() {
        return currentIdx;
    }

    /**
     * A method for starting/restarting the game with agiven shuffleddeck of cards.
     * 
     * @param deck Deck object of the Deck of cards
     */
    public void start(Deck deck) {
        CardGamePlayer currentPlayer = null;
        Card diamond3 = new Card(0, 2);
        // Clear the GUI
        table.disable();
        table.clearMsgArea();
        // Remove all cards
        handsOnTable.clear();
        for (int i = 0; i < playerList.size(); i++)
            playerList.get(i).removeAllCards();
        // Distribute cards & Find who hold Diamond3
        for (int i = 0; i < deck.size(); i++) {
            currentPlayer = playerList.get(i % 4);
            Card card = deck.getCard(i);
            currentPlayer.addCard(deck.getCard(i));
            if (card.equals(diamond3))
                currentIdx = i % 4;
        }
        // Sort hand
        for (int i = 0; i < playerList.size(); i++)
            playerList.get(i).sortCardsInHand();
        // Refresh GUI
        table.setActivePlayer(currentIdx);
        table.printMsg(" - Big Two Game Start -");
        table.printMsg(" Player " + currentIdx + "'s turn:");
        table.enable();
    }

    /**
     * A method for making a move by a player with the specified playerID using the
     * cards specified by the list of indices.
     * 
     * @param playerID ID of the player who make move
     * @param cardIdx  List of indices of cards
     */
    public void makeMove(int playerID, int[] cardIdx) {
        checkMove(playerID, cardIdx);
    }

    /**
     * A method for checking a move made by a player.
     * 
     * @param playerID ID of the player who make move
     * @param cardIdx  List of indices of cards
     */
    public void checkMove(int playerID, int[] cardIdx) {
        boolean isLegal = true;
        boolean isPassed = false;
        int cardsize = cardIdx.length;
        Hand hand = null;
        CardGamePlayer currentPlayer = null;

        isLegal = (isFirst && cardsize == 0) ? false : true;
        if (!(isFirst) || (isFirst == true && cardsize != 0)) {
            if (cardsize == 0) {
                passCounter = (passCounter + 1) % 4;
                isFirst = (passCounter == 3) ? true : false;
                isPassed = true;
            } else {
                CardList picked = new CardList();
                currentPlayer = playerList.get(currentIdx);
                for (int i : cardIdx)
                    picked.addCard(currentPlayer.getCardsInHand().getCard(i));
                if (isFirst && handsOnTable.isEmpty()) {
                    Card diamond3 = new Card(0, 2);
                    isLegal = picked.contains(diamond3) ? true : false;
                }
                if (isLegal) {
                    hand = composeHand(currentPlayer, picked);
                    Hand lastHand = handsOnTable.isEmpty() ? null : handsOnTable.get(handsOnTable.size() - 1);
                    isLegal = (hand != null) ? true : false;
                    if (isLegal) {
                        if (!isFirst) {
                            boolean isLarger = (lastHand == null) ? true : hand.beats(lastHand);
                            isLegal = isLarger ? true : false;
                        }
                    }
                }
            }
        }
        if (isLegal) {
            if (isPassed) {
                table.printMsg("{Pass}");
            } else {
                currentPlayer.removeCards(hand);
                handsOnTable.add(hand);
                table.printMsg("{" + hand.getType() + "} " + hand.toString());
                isFirst = false;
                passCounter = 0;
            }
            currentIdx = (currentIdx + 1) % 4;
            if (isFirst == true)
                table.printMsg("Player " + currentIdx + " is the First player.");
            table.setActivePlayer(currentIdx);
        } else {
            if (hand == null)
                table.printMsg("Not a legal move!!!");
            else
                table.printMsg("{" + hand.getType() + "} " + hand.toString() + " <== Not a legal move!!!");
        }
    }

    /**
     * A method for checking if the game ends
     * 
     * @return Boolean value of the game end status
     */
    public boolean endOfGame() {
        for (int i = 0; i < 4; i++) {
            if (playerList.get(i).getNumOfCards() == 0) {
                table.setActivePlayer(i);
                return true;
            }
        }
        return false;
    }

    /**
     * A method for returning a valid hand from the specified list of cards of the
     * player.
     * 
     * @param player player who played these cards
     * @param cards  the played cards
     * @return Hand object if it is a vaild hand, null if it is invaild.
     */
    public static Hand composeHand(CardGamePlayer player, CardList cards) {
        Hand h;
        switch (cards.size()) {
        case 1:
            h = new Single(player, cards);
            if (h.isValid() == true)
                return h;
            break;
        case 2:
            h = new Pair(player, cards);
            if (h.isValid() == true)
                return h;
            break;
        case 3:
            h = new Triple(player, cards);
            if (h.isValid() == true)
                return h;
            break;
        case 5:
            h = new StraightFlush(player, cards);
            if (h.isValid() == true)
                return h;
            h = new Quad(player, cards);
            if (h.isValid() == true)
                return h;
            h = new FullHouse(player, cards);
            if (h.isValid() == true)
                return h;
            h = new Flush(player, cards);
            if (h.isValid() == true)
                return h;
            h = new Straight(player, cards);
            if (h.isValid() == true)
                return h;
            break;
        }
        return h = null;
    }

    /**
     * A method for starting a Big Two card game. It should create a Big Two card
     * game, create and shuffle a deck of cards, and start the game with the deck of
     * cards
     */
    public static void main(String[] args) {
        BigTwo bigtwo = new BigTwo();
        BigTwoDeck deck = new BigTwoDeck();
        deck.shuffle();
        bigtwo.start(deck);
    }
}