package Board;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameManager {
    private JFrame frame;
    private JPanel panel;
    private JButton chooseNewGame;
    private JLabel chooseFENLabel;
    private JTextField chooseFENText;
    private JButton chooseFENButton;
    private final Object[] options = {"Play as White", "Play as Black"};


    public GameManager() {
        this.frame = new JFrame("Hetmachess");

        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        this.chooseNewGame = new JButton("Start a new game");
        chooseNewGame.addActionListener(this::startNewGame);

        this.chooseFENLabel = new JLabel("Start by FEN");
        this.chooseFENText = new JTextField(35);
        this.chooseFENButton = new JButton("Load FEN");
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
        if (fenIsValid(chooseFENText.getText())) {
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

    private boolean fenIsValid(String fen) {
        Pattern pattern = Pattern.compile("((([prnbqkPRNBQK12345678]*/){7})([prnbqkPRNBQK12345678]*)) (w|b) ((K?Q?k?q?)|\\-) (([abcdefgh][36])|\\-) (\\d*) (\\d*)");
        Matcher matcher = pattern.matcher(fen);
        if (matcher.matches()) {    //CHECA SE O PADRAO BATE
            char[] fenArray = fen.toCharArray();
            int count1 = 0;
            int count2 = 0;
            for (char character : fenArray) {
                if (47 < character && character < 58) {   // é um número
                    count1 = count1 + character - 48;
                } else if (64 < character && character < 123) {   // é letra
                    count1++;
                } else if (character == 47) {   //é uma /
                    count2++;
                } else if (count1 == 64 && count2 == 7) {   //CHECA NUMERO DE LINHAS E COLUNAS
                    return matcher.group(1).contains("k") && matcher.group(1).contains("K");    //CHECA SE TEM 2 REIS
                } else break;
            }
        }
        return false;
    }
}

