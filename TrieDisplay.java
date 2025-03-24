
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.TreeMap;
import javax.swing.*;
import java.awt.Image;
import javax.sound.sampled.*;


public class TrieDisplay extends JPanel implements KeyListener, ActionListener {

    private JFrame frame;
    private int size = 30, width = 1000, height = 900;

    private Trie trie;
    private String word, predicted; // word you are trying to spell printed in large font
    private char likelyChar; // used for single most likely character
    private Map<Character, Integer> likelyCharFreqs; // used for storing frequencies
    private boolean wordsLoaded; // use this to make sure words are all loaded before you start typing
    private TreeMap<String, Integer> likelyWords; // next likely words to be typed
    private ArrayList<String> fullText;
    private Image backgroundImage;
    private int textWidth;
    private BackgroundMusic bgMusic;

    //Game Mode vars
    private boolean gameMode = false;  // Toggle for Fill in the Lyrics
    private int score = 0;  // Player's score
    private String currentLine = "";  // Line from lyrics
    private String missingWord = "";  // The word to guess
    private String userGuess = "";  // User's current guess
    private int hintCount = 0;  // Number of hints used

    public TrieDisplay() {
        frame = new JFrame("Rosie Lyrics");
        frame.setSize(width, height);
        frame.add(this);
        frame.addKeyListener(this);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        backgroundImage = new ImageIcon("rosie.jpg").getImage();
        bgMusic = new BackgroundMusic();
        bgMusic.playMusic("drinksorcoffee.wav");

        // default settings
        word = "";
        predicted = "";
        likelyChar = ' '; // used for single most likely character
        likelyCharFreqs = new HashMap<Character, Integer>(); // used for storing frequencies
        likelyWords = new TreeMap<String, Integer>(); // used to store next likely words
        textWidth = 0; //width of the text

        wordsLoaded = false;
        fullText = new ArrayList<String>();

        trie = new Trie();
        readFileToTrie("rosie_lyrics.txt");

        wordsLoaded = true; // set flag to true -> indicates program is ready
        repaint();
    }

    // all graphics handled in method; don't do calculations here
    public void paintComponent(Graphics g) {
        // Background
        super.paintComponent(g); 
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, frame.getWidth(), frame.getHeight());
        g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        // Header
        g2.setFont(new Font("Monospaced", Font.BOLD, 40));
        if (wordsLoaded) {
            g2.setColor(new Color(200,90,90));
            g2.drawString("Start Typing: ", 43, 103);
            g2.setColor(Color.PINK);
            g2.drawString("Start Typing: ", 40, 100);
        } else {
            g2.drawString("Loading... please wait", 40, 100);
        }

        // Writing the line
        g2.setFont(new Font("Impact", Font.PLAIN, 30));
        g2.drawString(String.join(" ", fullText), 38, 160);

        // Writing the word after the rest of the previously typed words
        textWidth = g2.getFontMetrics().stringWidth(String.join(" ", fullText));
        int wordStartX = 48 + textWidth + 10;

        // Colors for word
        if (trie.contains(word)) {
            g2.setColor(Color.GREEN);
        } else {
            if (likelyChar == '_') {
                g2.setColor(Color.RED);
            } else {
                g2.setColor(Color.WHITE);
            }
        }
        g2.drawString(word, wordStartX, 160);

        // Write predicted word in gray (like Google Docs)
        if (!predicted.isEmpty() && !predicted.equals(word)) {
            g2.setColor(Color.GRAY);
            g2.drawString(predicted.substring(word.length()), wordStartX + g2.getFontMetrics().stringWidth(word), 160);
        }

        // Draw String below here for next most likely letter(s)
        g2.setFont(new Font("Monospaced", Font.PLAIN, 20));
        g2.setColor(Color.WHITE);
        if (likelyChar == '_') {
            g2.drawString("Most Likely Next Char -> ", 40, 210);
        } else {
            g2.drawString("Most Likely Next Char -> " + likelyChar, 40, 210);
        }

        // StringBuilder & sorting for likelyCharFreqs
        List<Map.Entry<Character, Integer>> sortedChars = new ArrayList<>(likelyCharFreqs.entrySet());
        sortedChars.sort((a, b) -> Integer.compare(b.getValue(), a.getValue())); // Sort in descending order

        StringBuilder freqSB = new StringBuilder();
        int totalFrequency = 0;
        int count = 0;

        for (int freq : likelyCharFreqs.values()) {
            totalFrequency += freq;
        }

        for (Map.Entry<Character, Integer> entry : sortedChars) { // FIXED: Use sortedChars
            if (count >= 5) {
                break;
            }
            int percent = (int) ((entry.getValue() / (double) totalFrequency) * 100);
            freqSB.append(entry.getKey()).append(" => ").append(percent).append("%, ");
            count++;
        }

        if (!likelyCharFreqs.isEmpty()) {
            freqSB.setLength(freqSB.length() - 2);
        }

        // drawString for likelyCharFreqd
        g2.setFont(new Font("Monospaced", Font.PLAIN, 13));
        g2.drawString("Most Likely Next Char Freqs -> " + freqSB.toString(), 40, 250);

        
        // StringBuilder & sorting for likelyWords
        List<Map.Entry<String, Integer>> sortedWords = new ArrayList<>(likelyWords.entrySet());
        sortedWords.sort((a, b) -> Integer.compare(b.getValue(), a.getValue())); // Sort in descending order

        // Most Likely Next Words printout (percentages)
        StringBuilder wordsSB = new StringBuilder();
        count = 0;
        int totalWordFrequency = 0;

        // Calculate the total frequency of all words
        for (int freq : likelyWords.values()) {
            totalWordFrequency += freq;
        }

        // Append top 5 words with their frequency percentages
        for (Map.Entry<String, Integer> entry : sortedWords) { // FIXED: Use sortedWords
            if (count >= 5) {
                break;
            }
            int percent = (int) ((entry.getValue() / (double) totalWordFrequency) * 100);
            wordsSB.append(entry.getKey()).append(" => ").append(percent).append("%, ");
            count++;
        }

        if (wordsSB.length() > 0) {
            wordsSB.setLength(wordsSB.length() - 2);
        }

        // drawString for likelyWords        
        g2.drawString("Most Likely Next Words -> " + wordsSB, 40, 290);

        // If there are no possible next letters, write "No further possibilities"
        if (likelyCharFreqs.isEmpty()) {
            g2.setColor(Color.RED);
            g2.setFont(new Font("Monospaced", Font.BOLD, 20));
            g2.drawString("No further possibilities", 40, 350);
        }

        g2.setColor(Color.PINK);
        g2.setFont(new Font("Monospaced", Font.PLAIN, 15));
        g2.drawString("Press esc to leave", 800, 50);
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // To return to the homescreen      
        if (keyCode == KeyEvent.VK_ESCAPE){
            bgMusic.stopMusic();
            homescreen(frame);
        }

        // Backspace
        if (keyCode == KeyEvent.VK_BACK_SPACE) {
            if (!word.isEmpty()) {
                word = word.substring(0, word.length() - 1);
            } else if (!fullText.isEmpty()) {
                word = fullText.get(fullText.size() - 1);
                fullText.remove(fullText.size() - 1);
            }
            predicted = "";
        }

        if (textWidth < 600) {  //so it doesnt go over the edge of the frame
            if (keyCode >= KeyEvent.VK_A && keyCode <= KeyEvent.VK_Z) { // Alphabet keys
                word += KeyEvent.getKeyText(keyCode).toLowerCase();
            }

            if (keyCode == KeyEvent.VK_QUOTE) {
                word += "'";
            }

            //confirm word & reset word
            if (keyCode == KeyEvent.VK_SPACE) {
                if (!word.isEmpty()) {
                    if (trie.contains(word)) {
                        fullText.add(word);
                        word = "";
                        predicted = "";;
                    }
                }
            }

            //autocomplete (enter/return)
            if (keyCode == KeyEvent.VK_ENTER) {// && !predicted.isEmpty()) {
                word = predicted;
                fullText.add(word);
                word = "";
                predicted = "";
            }

            // Update predictions
            if (!word.isEmpty()) {
                likelyChar = trie.mostLikelyNextChar(word);
                likelyCharFreqs = trie.likelyNextCharFrequency(word);
                likelyWords = trie.getWords(word);

                if (!likelyWords.isEmpty()) {
                    // Find the word with the highest frequency percentage
                    int totalFrequency = 0;
                    for (int freq : likelyWords.values()) {
                        totalFrequency += freq;
                    }

                    String mostLikelyWord = "";
                    int highestPercentage = 0;

                    for (Map.Entry<String, Integer> entry : likelyWords.entrySet()) {
                        int percent = (int) ((entry.getValue() / (double) totalFrequency) * 100);
                        if (percent > highestPercentage) {
                            highestPercentage = percent;
                            mostLikelyWord = entry.getKey();
                        }
                    }

                    predicted = mostLikelyWord;
                } else {
                    predicted = "";
                }
            } else {
                likelyChar = ' ';
                likelyCharFreqs.clear();
                likelyWords.clear();
                predicted = "";
            }

        }

        repaint();
    }

    private ArrayList<String> lyricsLines = new ArrayList<>();

    public void readFileToTrie(String fileName) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = br.readLine()) != null) {
                // Making everything lowercase and removing other punctuation
                line = line.toLowerCase().trim();
                line = line.replaceAll(",", "");
                line = line.replaceAll("\\?", "");
                line = line.replaceAll("-", "");

                lyricsLines.add(line);
                String[] splitted = line.split(" ");
                for (String word : splitted) {
                    trie.insert(word);
                }

            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * * empty methods needed for interfaces **
     */
    public void keyReleased(KeyEvent e) {

    }

    public void keyTyped(KeyEvent e) {

    }

    public void actionPerformed(ActionEvent e) {

    }

    public static void main(String[] args) {
        TrieDisplay.homescreen(null);
    }

    public static void homescreen(JFrame openFrame){
        if (openFrame != null){ //any open frames will dispose so this one can open
            openFrame.dispose();
        }
        
        JFrame startFrame = new JFrame("Start");
        startFrame.setSize(610, 635);
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setLayout(null);

        JLabel bg = new JLabel(new ImageIcon("rosie.jfif"));
        bg.setBounds(0, 0, 600, 600);

        JLabel title = new JLabel("Welcome to Rosie Lyrics!", SwingConstants.CENTER); 
        title.setFont(new Font("Monospaced", Font.BOLD, 30));
        title.setForeground(Color.PINK);
        title.setBounds(0, 100, 600, 50);

        JLabel intro = new JLabel("Either type lyrics from rosie's album, or play the lyric guessing game", SwingConstants.CENTER);
        intro.setFont(new Font("Monospaced", Font.BOLD, 12));
        intro.setForeground(Color.WHITE);
        intro.setBounds(0, 150, 600, 50);

        // Button to enter the typing part (with autocomplete & prediction percents)
        JButton typingButton = new JButton("Typing");
        typingButton.setFont(new Font("Monospaced", Font.BOLD, 24)); 
        typingButton.setBackground(new Color(207, 0, 24));
        typingButton.setForeground(Color.WHITE);
        typingButton.setFocusPainted(false); 
        typingButton.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));
        typingButton.setBounds(75, 335, 150, 100);

        // Close the homescreen and open the typing frame 
        typingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startFrame.dispose();
                new TrieDisplay();
            }
        });

        // Button to enter the "guess the lyric" game       
        JButton gameButton = new JButton("Game");
        gameButton.setFont(new Font("Monospaced", Font.BOLD, 24)); 
        gameButton.setBackground(new Color(207, 0, 24)); 
        gameButton.setForeground(Color.WHITE); 
        gameButton.setFocusPainted(false);
        gameButton.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));
        gameButton.setBounds(365, 335, 150, 100);

        // Close the homescreen and open the game frame
        gameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startFrame.dispose();
                TrieDisplay2 t = new TrieDisplay2();
            }
        });

        startFrame.add(title);
        startFrame.add(intro);
        startFrame.add(typingButton);
        startFrame.add(gameButton);
        startFrame.add(bg);
        startFrame.setVisible(true);
    }

}

//  to play the background music
class BackgroundMusic {

    private Clip clip;

    public void playMusic(String filepath) {
        try {
            File musicFile = new File(filepath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY); 
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // to stop the music
    public void stopMusic(){
        clip.close();
    }
}