import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

// Trieda pre úvodné okno hry
class InitialFrame extends JFrame {
    public InitialFrame() {
        setTitle("Piškvorky - Začiatok"); // Nastavenie titulku okna
        setSize(400, 400); // Zmena veľkosti okna
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Nastavenie akcie pri zatvorení okna
        setLayout(new GridLayout(4, 4)); // Zmena rozloženia na mriežku 4x2
        getContentPane().setBackground(Color.GRAY);

        // Komponenty úvodného okna
        JLabel playerXLabel = new JLabel("Meno hráča X:"); // Návestie pre meno hráča X
        playerXLabel.setForeground(Color.RED); // Nastavenie farby textu na červenú
        JTextField playerXField = new JTextField(); // Textové pole pre meno hráča X
        playerXField.setBackground(Color.LIGHT_GRAY); // Nastavenie pozadia na sivú farbu
        playerXField.setPreferredSize(new Dimension(150, 10)); // Zmena veľkosti textového poľa
        JLabel playerOLabel = new JLabel("Meno hráča O:"); // Návestie pre meno hráča O
        playerOLabel.setForeground(Color.CYAN); // Nastavenie farby textu na červenú
        JTextField playerOField = new JTextField(); // Textové pole pre meno hráča O
        playerOField.setBackground(Color.LIGHT_GRAY); // Nastavenie pozadia na sivú farbu
        playerOField.setPreferredSize(new Dimension(150, 10)); // Zmena veľkosti textového poľa

        // Prepínače pre voľbu režimu hry
        JRadioButton pvpButton = new JRadioButton("1vs1", true); // Predvolený režim 1 vs 1
        pvpButton.setBackground(Color.GRAY);
        JRadioButton pvcButton = new JRadioButton("1vsPočítač"); // Režim 1 vs Počítač
        pvcButton.setBackground(Color.GRAY);
        ButtonGroup gameModeGroup = new ButtonGroup(); // Skupina pre prepínače
        gameModeGroup.add(pvpButton); // Pridanie prepínača 1vs1 do skupiny
        gameModeGroup.add(pvcButton); // Pridanie prepínača 1vsPC do skupiny

        JButton startButton = new JButton("Začať hru"); // Tlačidlo na začatie hry
        startButton.setBackground(Color.green);
        startButton.setForeground(Color.darkGray);
        startButton.setPreferredSize(new Dimension(400,10));

        startButton.addActionListener((ActionEvent e) ->
        { // Akcia pri stlačení tlačidla
            String playerXName = playerXField.getText(); // Získanie mena hráča X z textového poľa
            String playerOName = pvcButton.isSelected() ? "Počítač" : playerOField.getText(); // Získanie mena hráča O podľa voľby režimu
            if (playerXName.isEmpty() || (!pvcButton.isSelected() && playerOName.isEmpty())) { // Kontrola, či sú zadané mená hráčov
                JOptionPane.showMessageDialog(this, "Prosím, zadajte mená hráčov."); // Upozornenie na nezadané mená
            } else {
                new TicTacToeGUI(3, playerXName, playerOName, pvcButton.isSelected()); // Spustenie hry s veľkosťou 3x3 a danými parametrami
                this.dispose(); // Zatvorenie úvodného okna
            }
        });

        // Pridanie komponentov do okna
        add(playerXLabel);
        add(playerXField);
        add(playerOLabel);
        add(playerOField);
        add(pvpButton);
        add(pvcButton);
        add(startButton);

        setVisible(true); // Nastavenie viditeľnosti okna na true
    }
}
