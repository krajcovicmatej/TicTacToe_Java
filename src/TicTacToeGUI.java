import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

// Class:  Main Gui Of TicTacToe
public class TicTacToeGUI extends JFrame implements ActionListener {
    private char currentPlayer = 'X';
    private JButton[][] buttons;
    private JPanel panel = new JPanel();
    private int boardSize;
    private String playerXName;
    private String playerOName;
    private boolean vsComputer;
    private JLabel statusLabel = new JLabel();
    private JLabel timeLabel = new JLabel();
    private int gameTime = 300;
    private Timer timer;

    // Constructor
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

        controlPanel.add(statusLabel); // status label
        controlPanel.add(timeLabel); // Time label
        add(controlPanel, BorderLayout.NORTH);

        updateStatusLabel();
        startTimer();
        setVisible(true);
    }

    //Board Inicialize
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

    // Action after click button
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton buttonClicked = (JButton) e.getSource();
        if (buttonClicked.getText().equals("")) {
            buttonClicked.setText(String.valueOf(currentPlayer));
            buttonClicked.setForeground(currentPlayer == 'X' ? Color.RED : Color.CYAN);  // Color set for X a O
            buttonClicked.setBackground(currentPlayer == 'X' ? Color.DARK_GRAY : Color.DARK_GRAY);    // Color change after click
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

    // Switch Player
    private void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    // PC
    private void computerMove() {
        Random rand = new Random();
        int i, j;
        do {
            i = rand.nextInt(boardSize);
            j = rand.nextInt(boardSize);
        } while (!buttons[i][j].getText().equals(""));

        buttons[i][j].setText(String.valueOf(currentPlayer));
        if (checkWin()) {
            JOptionPane.showMessageDialog(this, "Vyhral hráč :  " + getCurrentPlayerName() );
            endGame();
        } else if (isBoardFull()) {
            JOptionPane.showMessageDialog(this, "Nikto nevyhral, skús znova!");
            endGame();
        } else {
            switchPlayer();
            updateStatusLabel();
        }
    }

    // Check Win
    private boolean checkWin() {
        for (int i = 0; i < boardSize; i++) {
            if (checkRow(i) || checkColumn(i)) {
                return true;
            }
        }
        return checkDiagonals();
    }

    private boolean checkRow(int row) {
        for (int i = 1; i < boardSize; i++) {
            if (!buttons[row][i].getText().equals(buttons[row][0].getText()) || buttons[row][i].getText().equals("")) {
                return false;
            }
        }
        return true;
    }

    private boolean checkColumn(int col) {
        for (int i = 1; i < boardSize; i++) {
            if (!buttons[i][col].getText().equals(buttons[0][col].getText()) || buttons[i][col].getText().equals("")) {
                return false;
            }
        }
        return true;
    }

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

    // check draw
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

    // Reset
    private void resetBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                buttons[i][j].setText("");
            }
        }
        currentPlayer = 'X';
        updateStatusLabel();
    }

    // New game
    private void newGame() {
        this.dispose();
        SwingUtilities.invokeLater(InitialFrame::new);
    }


    private String getCurrentPlayerName() {
        return (currentPlayer == 'X') ? playerXName : playerOName;
    }


    private void updateStatusLabel() {
        statusLabel.setText("Na ťahu je: " + getCurrentPlayerName());
    }


    private void startTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameTime--;
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

    // End Game
    private void endGame() {
        timer.stop();
        timeLabel.setText("Čas: 00:00");
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                buttons[i][j].setEnabled(false); // Deactivate buttons afer game
            }
        }
    }
}
