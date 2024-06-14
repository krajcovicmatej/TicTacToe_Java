import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

// Trieda reprezentujúca hlavné GUI hry Piškvorky
public class TicTacToeGUI extends JFrame implements ActionListener {
    private char currentPlayer = 'X'; // Aktuálny hráč, začína hráč X
    private JButton[][] buttons; // Pole tlačidiel pre hraciu plochu
    private JPanel panel = new JPanel(); // Panel pre hraciu plochu
    private int boardSize; // Veľkosť hracej plochy (boardSize x boardSize)
    private String playerXName; // Meno hráča X
    private String playerOName; // Meno hráča O
    private boolean vsComputer; // Určuje, či hráč hraje proti PC
    private JLabel statusLabel = new JLabel(); // Label pre stav hry (kto je na ťahu)
    private JLabel timeLabel = new JLabel(); // Label pre časový limit hry
    private int gameTime = 300; // Časový limit hry (v sekundách)
    private Timer timer; // Časovač hry

    // Konštruktor
    public TicTacToeGUI(int size, String playerXName, String playerOName, boolean vsComputer)
    {
        this.boardSize = size;
        this.playerXName = playerXName;
        this.playerOName = playerOName;
        this.vsComputer = vsComputer;
        buttons = new JButton[boardSize][boardSize];

        setTitle("Piškvorky - Hra");
        setSize(600, 650); // Veľkosť okna pre zobrazenie časového limitu
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panel.setLayout(new GridLayout(boardSize, boardSize));
        initializeBoard(); // Inicializácia hracej plochy
        add(panel, BorderLayout.CENTER);

        // Ovládací panel
        JPanel controlPanel = new JPanel();
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetBoard());
        controlPanel.add(resetButton);

        JButton newGameButton = new JButton("Nová hra");
        newGameButton.addActionListener(e -> newGame());
        controlPanel.add(newGameButton);

        controlPanel.add(statusLabel); // Statusový label (kto je na ťahu)
        controlPanel.add(timeLabel); // Časový label
        add(controlPanel, BorderLayout.NORTH);

        updateStatusLabel(); // Aktualizácia statusového labelu
        startTimer(); // Spustenie časovača
        setVisible(true);
    }

    // Inicializácia hracej plochy
    private void initializeBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 100));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(this);
                panel.add(buttons[i][j]);



            }
        }
    }

    // Akcia po stlačení tlačidla
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton buttonClicked = (JButton) e.getSource();
        if (buttonClicked.getText().equals("")) {
            buttonClicked.setText(String.valueOf(currentPlayer));
            buttonClicked.setForeground(currentPlayer == 'X' ? Color.RED : Color.CYAN);  // Nastavenie farby textu pre X a O
            buttonClicked.setBackground(currentPlayer == 'X' ? Color.DARK_GRAY : Color.DARK_GRAY);    // Zmena farby pozadia po kliknutí
            if (checkWin()) {
                JOptionPane.showMessageDialog(this, "Vyhral hráč :  " + getCurrentPlayerName());
                endGame();
            } else if (isBoardFull()) {
                JOptionPane.showMessageDialog(this, "Nikto nevyhral, skúste znova!");
                endGame();
            } else {
                switchPlayer();
                updateStatusLabel();
                if (vsComputer && currentPlayer == 'O') {
                    computerMove();
                }
            }
        }
    }

    // Prepnutie hráča
    private void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    // Ťah počítača
    private void computerMove() {
        Random rand = new Random();
        int i, j;
        do {
            i = rand.nextInt(boardSize);
            j = rand.nextInt(boardSize);
        } while (!buttons[i][j].getText().equals(""));

        buttons[i][j].setText(String.valueOf(currentPlayer));
        if (checkWin()) {
            JOptionPane.showMessageDialog(this, "Player " + getCurrentPlayerName() + " wins!");
            endGame();
        } else if (isBoardFull()) {
            JOptionPane.showMessageDialog(this, "The game is a tie!");
            endGame();
        } else {
            switchPlayer();
            updateStatusLabel();
        }
    }

    // Kontrola výhry
    private boolean checkWin() {
        for (int i = 0; i < boardSize; i++) {
            if (checkRow(i) || checkColumn(i)) {
                return true;
            }
        }
        return checkDiagonals();
    }

    // Kontrola výhry v riadku
    private boolean checkRow(int row) {
        for (int i = 1; i < boardSize; i++) {
            if (!buttons[row][i].getText().equals(buttons[row][0].getText()) || buttons[row][i].getText().equals("")) {
                return false;
            }
        }
        return true;
    }

    // Kontrola výhry v stĺpci
    private boolean checkColumn(int col) {
        for (int i = 1; i < boardSize; i++) {
            if (!buttons[i][col].getText().equals(buttons[0][col].getText()) || buttons[i][col].getText().equals("")) {
                return false;
            }
        }
        return true;
    }

    // Kontrola výhry na diagonálach
    private boolean checkDiagonals() {
        boolean diagonal1 = true;
        boolean diagonal2 = true;

        for (int i = 1; i < boardSize; i++) {
            if (!buttons[i][i].getText().equals(buttons[0][0].getText()) || buttons[i][i].getText().equals("")) {
                diagonal1 = false;
            }
            if (!buttons[i][boardSize - i - 1].getText().equals(buttons[0][boardSize - 1].getText()) || buttons[i][boardSize - i - 1].getText().equals("")) {
                diagonal2 = false;
            }
        }
        return diagonal1 || diagonal2;
    }

    // Kontrola, či je hracia plocha plná (remíza)
    private boolean isBoardFull() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (buttons[i][j].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    // Resetovanie hracej plochy
    private void resetBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                buttons[i][j].setText("");
            }
        }
        currentPlayer = 'X';
        updateStatusLabel();
    }

    // Začatie novej hry
    private void newGame() {
        this.dispose();
        SwingUtilities.invokeLater(InitialFrame::new);
    }

    // Získanie mena aktuálneho hráča
    private String getCurrentPlayerName() {
        return (currentPlayer == 'X') ? playerXName : playerOName;
    }

    // Aktualizácia stavového labelu
    private void updateStatusLabel() {
        statusLabel.setText("Na ťahu je: " + getCurrentPlayerName());
    }

    // Spustenie časovača
    private void startTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameTime--; // Zníženie času o 1 sekundu
                if (gameTime < 0) {
                    timer.stop();
                    JOptionPane.showMessageDialog(TicTacToeGUI.this, "Čas vypršal. Koniec hry!");
                    endGame();
                } else {
                    int minutes = gameTime / 60;
                    int seconds = gameTime % 60;
                    timeLabel.setText("Čas: " + String.format("%02d:%02d", minutes, seconds));
                }
            }
        });
        timer.start();
    }

    // Ukončenie hry
    private void endGame() {
        timer.stop();
        timeLabel.setText("Čas: 00:00"); // Zobrazenie konečného času
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                buttons[i][j].setEnabled(false); // Deaktivácia tlačidiel po skončení hry
            }
        }
    }
}
