import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.nio.file.Files;
import java.nio.file.Path;

public class LifeExeControlPanel extends JFrame {
    private final DeckManager deckManager = new DeckManager();
    private final JTextArea drawnCardsLog = new JTextArea();
    private final Random random = new Random();

    public LifeExeControlPanel() {
        setTitle("Life.exe Promises & Perils Control Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLayout(new BorderLayout(10, 10));

        // --- BIG CARD BUTTONS ---
        JPanel cardPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        cardPanel.setPreferredSize(new Dimension(700, 250));

        JButton redCardButton = createCardButton("Pull a Consequence", Color.RED, () -> drawCard("red"));
        JButton blueCardButton = createCardButton("Take a Trivia Challenge", Color.CYAN, this::showTriviaCard);
        JButton greenCardButton = createCardButton("Reflect + Reset", Color.GREEN, () -> drawCard("green"));

        cardPanel.add(redCardButton);
        cardPanel.add(blueCardButton);
        cardPanel.add(greenCardButton);
        add(cardPanel, BorderLayout.NORTH);

        // --- SMALL CENTERED DICE BUTTON ---
        JButton rollButton = new JButton("Roll Dice");
        rollButton.setPreferredSize(new Dimension(140, 60));
        rollButton.setFont(new Font("Arial", Font.PLAIN, 14));
        rollButton.addActionListener(e -> {
            int roll = random.nextInt(6) + 1;
            JOptionPane.showMessageDialog(this, "You rolled a " + roll);
        });
        JPanel rollPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rollPanel.add(rollButton);
        add(rollPanel, BorderLayout.CENTER);

        // --- CARD LOG SCROLL PANE ---
        drawnCardsLog.setEditable(false);
        drawnCardsLog.setLineWrap(true);
        drawnCardsLog.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(drawnCardsLog);
        scrollPane.setPreferredSize(new Dimension(680, 200));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Drawn Cards Log"));
        add(scrollPane, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JButton createCardButton(String label, Color color, Runnable action) {
        JButton button = new JButton(label);
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setPreferredSize(new Dimension(200, 160));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.addActionListener(e -> action.run());
        return button;
    }

    private void drawCard(String color) {
        String card = deckManager.drawCard(color);
        if (card == null) return;

        drawnCardsLog.append(capitalize(color) + " Card: " + card + "\n");

        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setPreferredSize(new Dimension(300, 150));

        JLabel title = new JLabel(capitalize(color) + " Card", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        cardPanel.add(title, BorderLayout.NORTH);

        JTextArea content = new JTextArea(card);
        content.setWrapStyleWord(true);
        content.setLineWrap(true);
        content.setEditable(false);
        content.setOpaque(false);
        cardPanel.add(content, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(this, cardPanel, "Card Drawn", JOptionPane.PLAIN_MESSAGE);
    }
    private void showTriviaCard() {
        String card = deckManager.drawCard("blue");
        if (card == null) return;

        String[] parts = card.split("\\|");
        if (parts.length != 2) {
            JOptionPane.showMessageDialog(this, "Invalid trivia card format.");
            return;
        }

        String question = parts[0].trim();
        String answer = parts[1].trim();
        drawnCardsLog.append("Blue Card: " + question + "\n");

        // Create the content panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setPreferredSize(new Dimension(350, 150));

        JLabel header = new JLabel("🧠 Trivia Challenge", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(header, BorderLayout.NORTH);

        JTextArea qText = new JTextArea(question);
        qText.setWrapStyleWord(true);
        qText.setLineWrap(true);
        qText.setEditable(false);
        qText.setOpaque(false);
        panel.add(qText, BorderLayout.CENTER);

        // Create the custom dialog
        JDialog dialog = new JDialog(this, "Trivia Card", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().add(panel, BorderLayout.CENTER);

        JButton showAnswer = new JButton("Show Answer");
        showAnswer.addActionListener(e -> {
            JOptionPane.showMessageDialog(dialog, "Answer: " + answer);
            dialog.dispose(); // close the whole dialog after answer is shown
        });
        panel.add(showAnswer, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }



    private String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LifeExeControlPanel::new);
    }
}

