import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TrieDisplay2 extends JPanel implements KeyListener, ActionListener {

    private JFrame frame;
    private int width = 1000, height = 900;

    private Trie trie;
    private String word; //word is the word the user types 
    private boolean wordsLoaded;
    private ArrayList<String> fullText; // whole text
    private Image backgroundImage; //bg image
    private int textWidth; // width of the text
    private JButton hintButton; //button to get a hint
    private int score; //score of the player depending if they got it right/wrong
    private BackgroundMusic bgMusic; //background music

    private String gameLine; //the lyric on screen for the game
    private String missingWord; // the word missing for the user to guess
    private String userGuess; // the user's guess
    private int hintCount; // number of hints used
    private String hint; // the word that the hint is
    private boolean hintUsed; //if the hint was used or not


    public TrieDisplay2() {
        frame = new JFrame("Fill in the Rosie Lyrics");
        frame.setSize(width, height);
        frame.add(this);
        frame.addKeyListener(this);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        backgroundImage = new ImageIcon("rosie.jpg").getImage();
        bgMusic = new BackgroundMusic();
        bgMusic.playMusic("drinksorcoffee.wav");

        word = "";
        textWidth = 0;
        wordsLoaded = false;
        fullText = new ArrayList<>();
        trie = new Trie();
        score = 0;
        
        userGuess = "";
        hintCount = 0;
        hint = "";
        hintUsed = false;

        //hint button 
        hintButton = new JButton("Hint");
        hintButton.setFont(new Font("Monospaced", Font.BOLD, 24));
        hintButton.setBounds(40, 340, 150, 50);
        hintButton.setBackground(new Color(207, 0, 24));
        hintButton.setForeground(Color.WHITE);
        hintButton.setFocusPainted(false);
        hintButton.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));
        hintButton.addActionListener(this);

        this.setLayout(null);
        this.add(hintButton);

        readFileToTrie("rosie_lyrics.txt");
        wordsLoaded = true;
        startGameMode();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, frame.getWidth(), frame.getHeight());
        g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        // header & info
        g2.setFont(new Font("Monospaced", Font.BOLD, 30));

        g2.setColor(new Color(200,90,90) );
        g2.drawString("Guess the Lyrics: Rosie Edition", 42, 52); // drop shadow
        
        g2.setColor(Color.PINK);
        g2.drawString("Guess the Lyrics: Rosie Edition", 40, 50);
        
        g2.setFont(new Font("Monospaced", Font.PLAIN, 15));
        g2.drawString("Press esc to leave", 800, 50);

        g2.setFont(new Font("Monospaced", Font.PLAIN, 30));
        g2.drawString("Score: " + score, 40, 120);

        // the lyric needed to be completed
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.PLAIN, 25));
        g2.drawString("Complete the line:", 40, 180);
        g2.drawString(gameLine.replace(missingWord, "______"), 40, 230);

        // user's guess
        g2.drawString("Your Guess: ", 40, 270);
        g2.setColor(Color.PINK);
        g2.drawString(userGuess, 220, 270);

        // the hint
        g2.setColor(Color.WHITE);
        if (hintUsed) {
            g2.setColor(new Color(207, 0, 24));
            g2.drawString(hint, 40, 310);
        }

        textWidth = g2.getFontMetrics().stringWidth(String.join(word)); //calc the width of the text
        
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // return back to homescreen
        if (keyCode == KeyEvent.VK_ESCAPE){
            bgMusic.stopMusic();
            TrieDisplay.homescreen(frame);
            frame.requestFocusInWindow();
        }
            // backspace
            if (keyCode == KeyEvent.VK_BACK_SPACE && !userGuess.isEmpty()) {
                userGuess = userGuess.substring(0, userGuess.length() - 1);
            } 
            
            // text does not go off screen
            if (textWidth <= 600){
                // letters & quote
                if ((keyCode >= KeyEvent.VK_A && keyCode <= KeyEvent.VK_Z)) {
                    userGuess += KeyEvent.getKeyText(keyCode).toLowerCase();
                } 
                
                if (keyCode == KeyEvent.VK_QUOTE) {
                    word += "'";
                }
            }
            
            // enter to check guess & reset hint button
            if (keyCode == KeyEvent.VK_ENTER) {
                checkGuess();
                hintButton.setEnabled(true);
            } 
            
            // user can press 0 instead of the button
            if (keyCode == KeyEvent.VK_0) {
                provideHint();
            }
        
        repaint();
    }

    // provide a hint to the user
    private void provideHint() {
        if (hintCount < missingWord.length()) {
            hintCount++;
            hint = "Hint: " + missingWord.substring(0, hintCount);
            hintUsed = true;
        } else {
            hint = "No more hints! (Press Enter to continue)";
            hintButton.setEnabled(false);
        }
        repaint();
    }

    // checking the user's guess and incrementing/decrementing score
    private void checkGuess() {
        if (userGuess.equalsIgnoreCase(missingWord)) {
            score++;
        } else {
            score--;
        }
        startGameMode();
    }

    // start the game
    private void startGameMode() {
        Random rand = new Random(); //random variable
        List<String> lines = new ArrayList<>(fullText); //lines of the songs
        gameLine = lines.get(rand.nextInt(lines.size())); //lyric

        String[] words = gameLine.split(" "); //split the words of the lyric
        int missingIndex = rand.nextInt(words.length); //choosing a random word to be missing
        missingWord = words[missingIndex]; //the missing word
        words[missingIndex] = "______"; 
        gameLine = String.join(" ", words);

        userGuess = "";
        hintCount = 0;
        hintUsed = false;

    }

    // reading the file to trie
    public void readFileToTrie(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.toLowerCase().replaceAll("[^a-zA-Z' ]", "").trim();
                for (String word : line.split(" ")) {
                    trie.insert(word);
                }
                fullText.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void keyReleased(KeyEvent e) {

    }

    public void keyTyped(KeyEvent e) {

    }

    // providing the hint when hintbutton is clicked
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == hintButton) {
            provideHint();
            frame.requestFocus(); 
        }
    }

    public static void main(String[] args) {
        new TrieDisplay2();
    }
}