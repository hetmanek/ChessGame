package Board;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameManager {
    private final JFrame frame;
    private final JTextField chooseFENText;
    private final Object[] options = {"Play as White", "Play as Black"};

    public GameManager() {
        this.frame = new JFrame("Hetmachess");

        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        JButton chooseNewGame = new JButton("Start a new game");
        chooseNewGame.addActionListener(this::startNewGame);

        JLabel chooseFENLabel = new JLabel("Start by FEN");
        this.chooseFENText = new JTextField(35);
        JButton chooseFENButton = new JButton("Load FEN");
        chooseFENButton.addActionListener(this::startByFEN);

        panel.add(chooseNewGame);
        panel.add(chooseFENLabel);
        panel.add(chooseFENText);
        panel.add(chooseFENButton);

        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }

    private void startNewGame(ActionEvent e) {
        int option = JOptionPane.showOptionDialog(frame,
                "Choose your color",
                "New game",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);
        new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", option == 0);
        this.frame.dispose();
    }

    private void startByFEN(ActionEvent e) {
        if (isValidFen(chooseFENText.getText())) {
            int option = JOptionPane.showOptionDialog(frame,
                    "Choose your color",
                    "Valid FEN!!",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);
            new Board(chooseFENText.getText(), option == 0);
            this.frame.dispose();
        } else JOptionPane.showMessageDialog(frame, "invalid FEN");
    }

    private boolean isValidFen(String fen) {
        Pattern pattern = Pattern.compile("^(([pnbrqk1-8]{1,8}/){7}[pnbrqk1-8]{1,8}) (w|b) ((K?Q?k?q?){1,4}|-) (([abcdefgh][1-8])|-) (\\d+) (\\d+)$", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fen);
        if (matcher.matches()) {
            char[] fenArray = fen.toCharArray();
            int squereCount = 0;
            int rowCount = 0;
            for (char character : fenArray) {
                if (47 < character && character < 58) {
                    squereCount += character - 48;
                } else if (64 < character && character < 123) {
                    squereCount++;
                } else if (character == 47) {
                    rowCount++;
                } else if (squereCount == 64 && rowCount == 7) {
                    return matcher.group(1).contains("k") && matcher.group(1).contains("K");
                }
            }
        }
        return false;
    }
}