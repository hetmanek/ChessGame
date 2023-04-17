package Board;

import Pieces.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Board {
    private static final String CIRCLE_FILE_PATH = "src/circle.png";
    private static final String CHESS_FILE_PATH = "src/chess.png";
    private static final Object[] options = {"Rook", "Knight", "Bishop", "Queen"};
    private final JFrame frame;
    private final JPanel panel;
    private final Image[] images;
    private final int scale = 100;
    private final int strokWidth = this.scale / 10;
    private final Color selectedPieceColor = new Color(160, 50, 50, 255);
    private final Color lightSquareColor = new Color(255, 233, 197);
    private final Color darkSquareColor = new Color(152, 82, 40);
    private Piece selectedPiece = null;
    private boolean clickFlag;

    public Board(String fen, boolean asWhite) {
        this.images = pieceImages();
        this.frame = new JFrame("Hetmachess");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setBounds(100, 100, (8 * this.scale) + 16, (8 * this.scale) + 39);
        Position position = new Position(fen);
        this.panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                squareColor(g);
                drawPosition(boardOrientation(position.getPositionMap(), asWhite), g);
            }
        };

        frame.add(panel);
        frame.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                ArrayList<Integer> clickCoordinate = arrayListCoordinate(getPanelY(e, asWhite), getPanelX(e, asWhite));
                if (!clickFlag) {
                    selectedPiece = position.getPieceAt(clickCoordinate, position.getPositionMap());
                    if (selectedPiece != null && isTurnToPlay(selectedPiece, position)) {
                        highlightSelectedPiece((Graphics2D) panel.getGraphics(), asWhite);
                        for (ArrayList<Integer> positionValidMove : position.positionValidMoves(selectedPiece, position.getPositionMap(), false)) {
                            highlightPositionValidMoves(positionValidMoveOrientation(positionValidMove, asWhite), panel.getGraphics());
                        }
                        clickFlag = true;
                    }
                } else if (selectedPiece != null) {
                    position.movePiece(selectedPiece, clickCoordinate);
                    frame.repaint();
                    selectedPiece = null;
                    clickFlag = false;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        frame.setVisible(true);
    }

    protected static void checkmate(boolean isWhite) {
        String color = isWhite ? "white" : "black";
        JOptionPane.showMessageDialog(null, "Checkmate, " + color + " won!");
    }

    protected static void draw() {
        JOptionPane.showMessageDialog(null, "Draw!");
    }

    protected static Integer promotion() {
        return JOptionPane.showOptionDialog(null,
                "Promote to:",
                "Promotion",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                3);
    }

    private void squareColor(Graphics g) {
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                if ((row + column) % 2 == 0) {
                    g.setColor(this.lightSquareColor);
                } else {
                    g.setColor(this.darkSquareColor);
                }
                g.fillRect(row * this.scale, column * this.scale, this.scale, this.scale);
            }
        }
    }

    private Image[] pieceImages() {
        try {
            BufferedImage imagePieces = ImageIO.read(new File(CHESS_FILE_PATH));
            Image[] images = new Image[12];
            int index = 0;
            for (int y = 0; y < 400; y += 200) {
                for (int x = 0; x < 1200; x += 200) {
                    images[index] = imagePieces.getSubimage(x, y, 200, 200).getScaledInstance(this.scale, this.scale, BufferedImage.SCALE_SMOOTH);
                    index++;
                }
            }
            return images;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void drawPosition(Map<ArrayList<Integer>, Piece> positionMap, Graphics g) {
        positionMap.forEach((k, v) -> {
                    if (v != null)
                        g.drawImage(this.images[v.getImageIndex()],
                                k.get(1) * this.scale,
                                k.get(0) * this.scale,
                                this.panel);
                }
        );
    }

    private Image movesCircle() {
        try {
            Image image = ImageIO.read(new File(CIRCLE_FILE_PATH));
            image = image.getScaledInstance(this.scale / 2, this.scale / 2, BufferedImage.SCALE_SMOOTH);
            return image;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void highlightPositionValidMoves(ArrayList<Integer> positionValidMove, Graphics g) {
        g.drawImage(movesCircle(),
                (positionValidMove.get(1)) * this.scale + (this.scale / 4),
                positionValidMove.get(0) * this.scale + (this.scale / 4),
                this.panel);
    }

    private void highlightSelectedPiece(Graphics2D g2d, boolean asWhite) {
        int row = selectedPiece.getCoordinate().get(1);
        int column = selectedPiece.getCoordinate().get(0);
        if (!asWhite) {
            row = 7 - row;
            column = 7 - column;
        }
        g2d.setColor(this.selectedPieceColor);
        g2d.setStroke(new BasicStroke(this.strokWidth));
        g2d.drawRect(row * this.scale + strokWidth / 2, column * this.scale + strokWidth / 2, this.scale - strokWidth, this.scale - strokWidth);
    }

    private Map<ArrayList<Integer>, Piece> boardOrientation(Map<ArrayList<Integer>, Piece> position,
                                                            boolean asWhite) {
        if (asWhite) {
            return position;
        } else {
            return rotateBoard(position);
        }
    }

    private Map<ArrayList<Integer>, Piece> rotateBoard(Map<ArrayList<Integer>, Piece> position) {
        Map<ArrayList<Integer>, Piece> rotatedPosition = new HashMap<>();
        position.forEach((k, v) ->
                rotatedPosition.put(arrayListCoordinate(7 - k.get(0), 7 - k.get(1)), v));
        return rotatedPosition;
    }

    private ArrayList<Integer> positionValidMoveOrientation(ArrayList<Integer> realValidMove, boolean asWhite) {
        if (asWhite) {
            return realValidMove;
        } else {
            return rotateValidMoves(realValidMove);
        }
    }

    private ArrayList<Integer> rotateValidMoves(ArrayList<Integer> validMoves) {
        return arrayListCoordinate(7 - validMoves.get(0), 7 - validMoves.get(1));
    }

    private int getPanelX(MouseEvent e, boolean asWhite) {
        return (asWhite ? (e.getX() - 8) / this.scale : 7 - ((e.getX() - 8) / this.scale));
    }

    private int getPanelY(MouseEvent e, boolean asWhite) {
        return asWhite ? (e.getY() - 31) / this.scale : 7 - ((e.getY() - 31) / this.scale);
    }

    private boolean isTurnToPlay(Piece piece, Position position) {
        return position.isWhiteTurn() == piece.isWhite();
    }

    private ArrayList<Integer> arrayListCoordinate(int row, int column) {
        ArrayList<Integer> coordinate = new ArrayList<>();
        coordinate.add(row);
        coordinate.add(column);
        return coordinate;
    }
}