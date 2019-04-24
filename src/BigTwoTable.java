import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

@SuppressWarnings("serial")

/**
 * The BigTwoTableclass implements the CardGameTable interface. It is used to
 * build a GUI for the Big Two card gameand handle all user actions
 */
public class BigTwoTable implements CardGameTable {
    /** A card game associates with this table */
    private CardGame game;
    /** A boolean array indicating which cards are being selected. */
    private boolean[] selected;
    /** An integerspecifying the index of the activeplayer. */
    private int activePlayer;
    /** the main window of the application */
    private JFrame frame;
    /**
     * A panel for showing the cards of each player and the cards played on the
     * table
     */
    private JPanel bigTwoPanel;
    /** A “Play” buttonfor the active player to play the selected cards */
    private JButton playButton;
    /**
     * A “Pass” buttonfor the active player to pass his/her turn to the next player
     */
    private JButton passButton;
    /**
     * A text area for showing the current game status as well as end of game
     * messages.
     */
    private JTextArea msgArea;
    /** A 2D array storing the images for the faces of the cards */
    private Image[][] cardImages;
    /** An image for the backs of the cards */
    private Image cardBackImage;
    /** An array storing the images for the avatars. */
    private Image[] avatars;

    /**
     * A constructor for creating a BigTwoTable.
     * 
     * @param bigtwo BigTwo object of BigTwo
     */
    BigTwoTable(BigTwo bigTwo) {
        this.game = bigTwo;
        initImg();
        layout();
        this.selected = new boolean[13];
    }

    /**
     * A method for setting the index of the active player
     * 
     * @param activePlayer Id of the active player
     */
    public void setActivePlayer(int activePlayer) {
        this.activePlayer = activePlayer;
    }

    /**
     * A method for getting an array of indices of the cards selected
     * 
     * @return integer array of indices of the cards selected
     */
    public int[] getSelected() {
        ArrayList<Integer> tempList = new ArrayList<Integer>();
        for (int i = 0; i < selected.length; i++) {
            if (selected[i] == true)
                tempList.add(i);
        }
        int[] selectedIdx = new int[tempList.size()];
        int c = 0;
        for (Integer i : tempList)
            selectedIdx[c++] = i.intValue();
        return selectedIdx;
    }

    /** A method for resetting the list of selected cards. */
    public void resetSelected() {
        for (int i = 0; i < selected.length; i++)
            selected[i] = false;
    }

    /** A method for repainting the GUI. */
    public void repaint() {
        if (activePlayer > -1) {
            printMsg(game.getPlayerList().get(activePlayer).getName() + "'s turn:");
        }
        resetSelected();
        frame.repaint();
    }

    /**
     * A method for printing the specified string to the messagearea of the GUI.
     * 
     * @param msg String of the message
     */
    public void printMsg(String msg) {
        msgArea.append(msg + "\n");
    }

    /** A method for clearing the messagearea of the GUI */
    public void clearMsgArea() {
        msgArea.setText(null);
    }

    /** A method for resetting the GUI */
    public void reset() {
        resetSelected();
        clearMsgArea();
        repaint();
        enable();
    }

    /** A method for enabling user interactions with the GUI */
    public void enable() {
        playButton.setEnabled(true);
        passButton.setEnabled(true);
        bigTwoPanel.setEnabled(true);
    }

    /** A method for disabling user interactions with the GUI */
    public void disable() {
        playButton.setEnabled(false);
        passButton.setEnabled(false);
        bigTwoPanel.setEnabled(false);
    }

    /**
     * An inner class of Game panel that extends the JPanelclass and implements the
     * MouseListener interface.
     */
    public class BigTwoPanel extends JPanel implements MouseListener {

        @Override
        /** Draw the card game table. */
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension d = this.getSize();
            g.setColor(Color.WHITE);
            for (int id = 0; id < 4; id++) {
                CardList temp = game.getPlayerList().get(id).getCardsInHand();
                g.drawString(game.getPlayerList().get(id).getName(), 24, 27 + id * 120);
                Image image = avatars[id];
                g.drawImage(image, 5, 30 + id * 120, 85, 97, null);
                g.drawLine(0, 135 + id * 120, d.width, 135 + id * 120);
                boolean isActive = (id == activePlayer || activePlayer == -1) ? true : false;
                if (isActive) {
                    for (int i = 0; i < game.getPlayerList().get(id).getCardsInHand().size(); i++) {
                        Image cardImg = cardImages[temp.getCard(i).getRank()][temp.getCard(i).getSuit()];
                        int x = (selected[i]) ? 25 : 30;
                        g.drawImage(cardImg, 120 + 25 * i, x + id * 120, null);
                    }
                } else {
                    for (int i = 0; i < game.getPlayerList().get(id).getCardsInHand().size(); i++) {
                        Image cardImg = cardBackImage;
                        g.drawImage(cardImg, 120 + 25 * i, 30 + id * 120, null);
                    }
                }
            }
            if (game.getHandsOnTable().size() != 0) {
                Hand tableHand = game.getHandsOnTable().get(game.getHandsOnTable().size() - 1);
                g.drawString("Played by " + tableHand.getPlayer().getName(), 8, 550);
                for (int i = 0; i < tableHand.size(); i++) {
                    Image cardImg = cardImages[tableHand.getCard(i).getRank()][tableHand.getCard(i).getSuit()];
                    g.drawImage(cardImg, 120 + 25 * i, 500, null);
                }
            } else
                g.drawString("No hand on table", 8, 550);
            // End Game Screen
            if (game.endOfGame() == true) {
                Image firework = new ImageIcon(getClass().getResource("imgs/firework.png")).getImage();
                printMsg(game.getPlayerList().get(activePlayer).getName() + " wins the game!");
                g.drawImage(firework, 100, 100, 200, 200, null);
                g.drawImage(firework, 200, 400, 150, 150, null);
                g.drawImage(firework, 300, 200, 200, 200, null);
                g.setFont(new Font("default", Font.BOLD, 16));
                g.drawString("You win the game!", 250, 80 + activePlayer * 120);
            }
        }

        /** Handle mouse click events */
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            int r1 = 25 + activePlayer * 120;
            int r2 = 30 + 97 + activePlayer * 120;
            int size = game.getPlayerList().get(activePlayer).getNumOfCards();
            int y1;
            printMsg(x + " " + y);
            if (y >= r1 && y <= r2) {
                for (int i = (size - 1); i > -1; i--) {
                    if (x >= 120 + 25 * i && x <= 120 + 25 * i + 72) {
                        y1 = (selected[i] == true) ? r1 : r1 + 5;
                        if (y >= y1 && y <= y1 + 96) {
                            selected[i] = (selected[i] == true) ? false : true;
                            break;
                        }
                    }
                }
            }
            frame.repaint();
        }

        // Empty Function implements from MouseListener
        public void mousePressed(MouseEvent e) {
        }

        // Empty Function implements from MouseListener
        public void mouseEntered(MouseEvent e) {
        }

        // Empty Function implements from MouseListener
        public void mouseReleased(MouseEvent e) {
        }

        // Empty Function implements from MouseListener
        public void mouseExited(MouseEvent e) {
        }
    }

    /**
     * An inner class that implements the ActionListener interface to "Play" Button.
     */
    public class PlayButtonListener implements ActionListener {
        /** Handle button-click eventsfor the “Play” button */
        public void actionPerformed(ActionEvent e) {
            boolean isEmpty = true;
            for (int i = 0; i < selected.length; i++) {
                if (selected[i] == true) {
                    isEmpty = false;
                    break;
                }
            }
            if (isEmpty) {
                printMsg("No card selected! Press Pass if you want to pass");
            } else {
                game.makeMove(activePlayer, getSelected());
                if (game.endOfGame()) {
                    activePlayer = -1;
                    repaint();
                    disable();
                } else
                    repaint();
            }
        }
    }

    /**
     * An inner class that implements the ActionListenerinterface to "Pass" Button.
     */
    public class PassButtonListener implements ActionListener {
        /** Handle button-click eventsfor the “Pass” button */
        public void actionPerformed(ActionEvent e) {
            int[] pass = {};
            game.makeMove(activePlayer, pass);
            repaint();
        }
    }

    /**
     * An inner class that implements the ActionListenerinterface to "Restart" Menu
     * item.
     */
    public class RestartMenuItemListener implements ActionListener {
        /** Handle menu-item-click events for the “Restart” menu item */
        public void actionPerformed(ActionEvent e) {
            disable();
            reset();
            BigTwoDeck deck = new BigTwoDeck();
            deck.shuffle();
            game.start(deck);
        }
    }

    /**
     * An inner class that implements the ActionListenerinterface to "Quit" Menu
     * item
     */

    public class QuitMenuItemListener implements ActionListener {
        /** Handle menu-item-click events for the “Quit” menu item */
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }

    }

    // A method to initiate images
    private void initImg() {
        cardImages = new Image[13][4];
        cardBackImage = new ImageIcon(getClass().getResource("imgs/b.gif")).getImage();
        avatars = new Image[4];
        char[] suits = { 'd', 'c', 'h', 's' };
        char[] rank = { 'a', '2', '3', '4', '5', '6', '7', '8', '9', 't', 'j', 'q', 'k' };
        for (int r = 0; r < 13; r++) {
            for (int s = 0; s < 4; s++)
                cardImages[r][s] = new ImageIcon(getClass().getResource("imgs/cards/" + rank[r] + suits[s] + ".gif"))
                        .getImage();
        }
        for (int i = 0; i < 4; i++)
            avatars[i] = new ImageIcon(getClass().getResource("imgs/p" + i + ".png")).getImage();
    }

    // A method to layout the gui
    private void layout() {
        // Initate frame
        this.frame = new JFrame("Big Two");
        frame.setSize(1280, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Btn
        this.playButton = new JButton("Play");
        this.passButton = new JButton("Pass");
        PlayButtonListener playLtr = new PlayButtonListener();
        playButton.addActionListener(playLtr);
        PassButtonListener passLtr = new PassButtonListener();
        passButton.addActionListener(passLtr);
        // Menu
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        menuBar.add(gameMenu);
        JMenuItem restartMenuItem = new JMenuItem("Restart");
        RestartMenuItemListener reLtr = new RestartMenuItemListener();
        restartMenuItem.addActionListener(reLtr);
        JMenuItem quitMenuItem = new JMenuItem("Quit");
        QuitMenuItemListener quitLtr = new QuitMenuItemListener();
        quitMenuItem.addActionListener(quitLtr);
        gameMenu.add(restartMenuItem);
        gameMenu.add(quitMenuItem);
        // bigTwoPanel
        bigTwoPanel = new BigTwoPanel();
        bigTwoPanel.setLayout(new BoxLayout(bigTwoPanel, BoxLayout.X_AXIS));
        bigTwoPanel.setPreferredSize(new Dimension(600, 600));
        bigTwoPanel.addMouseListener(new BigTwoPanel());
        bigTwoPanel.setBackground(new Color(0, 100, 0));
        // buttonPanel
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(playButton);
        btnPanel.add(passButton);
        // MsgArea
        this.msgArea = new JTextArea(20, 30);
        msgArea.setEditable(false);
        msgArea.setLineWrap(true);
        JScrollPane scrollMsg = new JScrollPane(msgArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // Frame
        frame.add(menuBar, BorderLayout.NORTH);
        frame.add(bigTwoPanel, BorderLayout.CENTER);
        frame.add(scrollMsg, BorderLayout.EAST);
        frame.add(btnPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }

}