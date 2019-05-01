import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class BigTwoClient implements CardGame, NetworkGame {
    /** A deck of cards. */
    private Deck deck = new Deck();
    /** A list of players. */
    private ArrayList<CardGamePlayer> playerList = new ArrayList<CardGamePlayer>();
    /** A list of hands played on table. */
    private ArrayList<Hand> handsOnTable = new ArrayList<Hand>();
    /** An integer specifying the index of the current player. */
    private int currentIdx;
    /** An integer specifying the number of players. */
    private int numOfPlayers;
    /** An integer specifying the playerID (i.e., index) of the local player. */
    private int playerID;
    /** A string specifying the name of the local player. */
    private String playerName;
    /** A string specifying the IP address of the game server. */
    private String serverIP;
    /** An integer specifying the TCP port of the game server. */
    private int serverPort;
    /** A socket connection to the game server */
    private Socket sock;
    /** An ObjectOutputStream for sending messages to the server */
    private ObjectOutputStream oos;
    /**
     * A Big Two table which builds the GUI for the game and handles all user
     * actions
     */
    private BigTwoTable table;
    /** A boolean indicate this turn is the first turn */
    private boolean isFirst = true;
    /** A counter to counting number of player passed */
    private int passCounter = 0;

    /** A constructor for creating a Big Two Client */
    public BigTwoClient() {
        for (int i = 0; i < 4; i++) {
            CardGamePlayer player = new CardGamePlayer();
            playerList.add(player);
            numOfPlayers++;

        }
        this.table = new BigTwoTable(this);
        makeConnection();
    }

    /**
     * A method forgetting the number of players
     * 
     * @return Number of players
     */
    public int getNumOfPlayers() {
        return numOfPlayers;
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

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public void makeConnection() {
        table.connectPopup();
        table.playerPopup();
        try {
            this.sock = new Socket(serverIP, serverPort);
        } catch (Exception e) {
            e.printStackTrace();
            table.printMsg("Failed to create Socket");
        }
        try {
            oos = new ObjectOutputStream(sock.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            table.printMsg("Failed to create OOS");
        }
        Thread threadedServer = new Thread(new ServerHandler(sock));
        threadedServer.start();
        sendMessage(new CardGameMessage(CardGameMessage.JOIN, -1, getPlayerName()));
        sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
        table.repaint();
    }

    public synchronized void parseMessage(GameMessage message) {
        switch (message.getType()) {
        case CardGameMessage.PLAYER_LIST:
            setPlayerID(message.getPlayerID());
            for (int i = 0; i < ((String[]) message.getData()).length; i++) {
                String name = ((String[]) message.getData())[i] == null ? "Player " + (i + 1)
                        : ((String[]) message.getData())[i];
                playerList.get(i).setName(name);
            }
            table.repaint();
            break;
        case CardGameMessage.JOIN:
            playerList.get(message.getPlayerID()).setName((String) message.getData());
            table.repaint();
            table.printMsg((String) message.getData() + " joined the game.");
            break;
        case CardGameMessage.FULL:
            table.printMsg("Server is currently FULLED");
            break;
        case CardGameMessage.QUIT:
            table.printMsg(playerList.get(message.getPlayerID()).getName() + " left the game.");
            playerList.get(message.getPlayerID()).setName("");
            table.repaint();
            if (!endOfGame()) {
                table.disable();
                table.reset();
                sendMessage(new CardGameMessage(4, -1, null));
            }
            break;
        case CardGameMessage.READY:
            table.printMsg(playerList.get(message.getPlayerID()).getName() + " is ready.");
            break;
        case CardGameMessage.START:
            table.printMsg("All players are ready. Game Start!");
            start((BigTwoDeck) message.getData());
            break;
        case CardGameMessage.MOVE:
            checkMove(message.getPlayerID(), (int[]) message.getData());
            table.repaint();
            break;
        case CardGameMessage.MSG:
            table.printChatMsg((String) message.getData());
            break;
        }
    }

    public void sendMessage(GameMessage message) {
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (Exception e) {
            table.printMsg("Cannot send the message");
            e.printStackTrace();
        }
    }

    class ServerHandler implements Runnable {
        private ObjectInputStream ois;

        ServerHandler(Socket sock) {
            try {
                ois = new ObjectInputStream(sock.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void run() {
            CardGameMessage message;
            try {
                while ((message = (CardGameMessage) ois.readObject()) != null)
                    parseMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        isFirst = true;
        passCounter = 0;
        table.setActivePlayer(currentIdx);
        table.printMsg(" Player " + currentIdx + "'s turn:");
        table.repaint();
    }

    /**
     * A method for making a move by a player with the specified playerID using the
     * cards specified by the list of indices.
     * 
     * @param playerID ID of the player who make move
     * @param cardIdx  List of indices of cards
     */
    public void makeMove(int playerID, int[] cardIdx) {
        CardGameMessage message = new CardGameMessage(CardGameMessage.MOVE, -1, cardIdx);
        sendMessage(message);
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
        table.printMsg(getPlayerList().get(currentIdx).getName() + "'s turn:");
        table.isWin = endOfGame() ? true : false;
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
                table.winPopup();
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
        BigTwoClient bigtwoclient = new BigTwoClient();
    }
}