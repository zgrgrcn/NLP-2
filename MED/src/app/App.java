package app;

/**
 * MED calculater with gui 
 *
 * @author Ozgur gurcan
 * @since 2019 Dec 19
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

//https://www.javatpoint.com/java-swing
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class App {

    static final boolean DEBUGMODE = false;

    private static String readFileAsString(String fileName) throws IOException {
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);// for turkish letters
        // write("kelime sayisi: " + data.length());
        return data;
    }

    private static int min(int x, int y, int z) {
        if (x <= y && x <= z)
            return x;
        if (y <= x && y <= z)
            return y;
        else
            return z;
    }

    private static int editDistDP(String str1, String str2, int m, int n) {
        // https://www.geeksforgeeks.org/edit-distance-dp-5/
        int dp[][] = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0)
                    dp[i][j] = j;
                else if (j == 0)
                    dp[i][j] = i;
                else if (str1.charAt(i - 1) == str2.charAt(j - 1))
                    dp[i][j] = dp[i - 1][j - 1];
                else
                    dp[i][j] = 1 + min(dp[i][j - 1], dp[i - 1][j], dp[i - 1][j - 1]);
            }
        }
        return dp[m][n];
    }

    private static String readFromUser() throws IOException {
        if (!DEBUGMODE)
            return "";
        BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Please enter a word: ");
        String theWord = obj.readLine();
        // write("Secilen kelime: " + theWord);
        return theWord;
    }

    private static void write(String str) {
        if (DEBUGMODE)
            System.out.println(str);
    }

    private static Word[] differentWords(String[] _tokens, String _userWord) {
        Word[] differentWords = new Word[_tokens.length];
        for (int i = 0; i < _tokens.length; i++) {
            differentWords[i] = new Word(_tokens[i],
                    editDistDP(_tokens[i], _userWord, _tokens[i].length(), _userWord.length()));
        }
        return differentWords;
    }

    public static void main(String[] args) throws IOException {
        GUI gui = new GUI();
        String[] tokens = readFileAsString(System.getProperty("user.dir") + "/tr_dictionary.txt").split("\\n");
        // String userWord = readFromUser();

        gui.myWord.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                list(tokens, gui.myWord.getText(), gui);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                list(tokens, gui.myWord.getText(), gui);
            }

            @Override
            public void changedUpdate(DocumentEvent arg0) {
                list(tokens, gui.myWord.getText(), gui);
            }
        });
    }

    private static void list(String[] tokens, String userWord, GUI gui) {
        long startTime = System.currentTimeMillis();// ### Start Timer ### //

        Word[] differentWords = differentWords(tokens, userWord);
        Arrays.sort(differentWords);

        gui.text.setText(""); // clear
        for (int i = 0; i < 5; i++) {
            // write(differentWords[i].toString());
            gui.text.append(differentWords[i].toString() + "\n");
        }
        // write("time passed: " + (System.currentTimeMillis() - startTime) + " milli
        // seconds");
        gui.text.append("time passed: " + (System.currentTimeMillis() - startTime) + " milli seconds\n"); // ### END
                                                                                                          // Timer
    }
}

class Word implements Comparable<Word> {
    public String name;
    public int distance;

    public Word(String _name, int _distance) {
        name = _name;
        distance = _distance;
    }

    @Override
    public String toString() {
        return (this.name + " " + this.distance);
    }

    @Override
    public int compareTo(Word w) {
        if (this.distance != w.distance) {
            return this.distance - w.distance;
        }
        return 0;
    }
};

class GUI {
    JFrame frame;
    JTextArea text;
    JTextField myWord;

    GUI() {
        frame = new JFrame();// creating instance of JFrame
        frame.setTitle("Ozgur Gurcan - 2016510032");

        text = new JTextArea();
        text.setBounds(30, 80, 270, 100);
        text.setEditable(false);
        frame.add(text);

        myWord = new JTextField("Kelime girin.");
        myWord.setBounds(30, 30, 270, 50);
        frame.add(myWord);

        frame.setSize(340, 250);// 340 width and 250 height
        frame.setLayout(null);// using no layout managers
        frame.setVisible(true);// making the frame visible
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}