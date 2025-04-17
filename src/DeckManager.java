import javax.swing.*;
import java.nio.file.*;
import java.util.*;

public class DeckManager {
    private final Map<String, List<String>> masterDecks = new HashMap<>();
    private final Map<String, Queue<String>> activeDecks = new HashMap<>();

    public DeckManager() {
        loadAndShuffle("red", "cards/red_cards.txt");
        loadAndShuffle("blue", "cards/blue_cards.txt");
        loadAndShuffle("green", "cards/green_cards.txt");
    }

    public String drawCard(String color) {
        Queue<String> deck = activeDecks.get(color);
        if (deck == null || deck.isEmpty()) {
            List<String> original = masterDecks.getOrDefault(color, new ArrayList<>());
            deck = shuffleDeck(original);
            activeDecks.put(color, deck);
        }
        return deck.poll();
    }

    private void loadAndShuffle(String key, String path) {
        List<String> lines = loadDeck(path);
        masterDecks.put(key, lines);
        activeDecks.put(key, shuffleDeck(lines));
    }

    private Queue<String> shuffleDeck(List<String> base) {
        List<String> shuffled = new ArrayList<>(base);
        Collections.shuffle(shuffled);
        return new LinkedList<>(shuffled);
    }

    private List<String> loadDeck(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading " + path + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
