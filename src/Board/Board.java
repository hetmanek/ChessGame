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
    private final JFrame frame;
    private final JPanel panel;
    private final Image[] images;
    private final int scale = 96;
    private Piece selectedPiece = null;
    private boolean clickFlag;


    public Board(String startAs, boolean asWhite) {
        this.images = pieceImages();
        this.frame = new JFrame("Hetmachess");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setBounds(20, 20, (int) (8.177 * this.scale), (int) (8.415 * this.scale));
        Position position = new Position(startAs);
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
                if (!clickFlag) {
                    selectedPiece = position.getPieceAt(arrayListCoordinate(getPanelY(e, asWhite), getPanelX(e, asWhite)));
                    if (selectedPiece != null && turnToPlay(selectedPiece, position)) {
                        for (ArrayList<Integer> realValidMove : position.realValidMoves(selectedPiece, false)) {
                            highlightRealValidMoves(realValidMoveOrientation(realValidMove, asWhite), panel.getGraphics());
                        }
                        clickFlag = true;
                    }
                } else {
                    if (selectedPiece != null) {
                        position.movePiece(selectedPiece, arrayListCoordinate(getPanelY(e, asWhite), getPanelX(e, asWhite)));
                        frame.repaint();
                        selectedPiece = null;
                        clickFlag = false;
                    }
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

    private void squareColor(Graphics g) {
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                if ((row + column) % 2 == 0) {
                    g.setColor(new Color(255, 233, 197));
                } else {
                    g.setColor(new Color(150, 77, 34));
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

    private void highlightRealValidMoves(ArrayList<Integer> realValidMove, Graphics g) {
        g.drawImage(movesCircle(),
                (realValidMove.get(1)) * this.scale + (this.scale / 4),
                realValidMove.get(0) * this.scale + (this.scale / 4),
                this.panel);
    }

    private Map<ArrayList<Integer>, Piece> boardOrientation(Map<ArrayList<Integer>, Piece> position, boolean asWhite) {
        if (asWhite) {
            return position;
        } else {
            return rotateBoard(position);
        }
    }

    private Map<ArrayList<Integer>, Piece> rotateBoard(Map<ArrayList<Integer>, Piece> position) {
        Map<ArrayList<Integer>, Piece> rotatedPosition = new HashMap<>();
        position.forEach((k, v) -> {
            rotatedPosition.put(arrayListCoordinate(7 - k.get(0), 7 - k.get(1)), v);
        });
        return rotatedPosition;
    }

    private ArrayList<Integer> realValidMoveOrientation(ArrayList<Integer> realValidMove, boolean asWhite) {
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
        return asWhite ? e.getX() / this.scale : 7 - (e.getX() / this.scale);
    }

    private int getPanelY(MouseEvent e, boolean asWhite) {
        return asWhite ? e.getY() / this.scale : 7 - (e.getY() / this.scale);
    }//TODO TA MUITO DESLOCADO O EIXO Y

    private boolean turnToPlay(Piece piece, Position position) {
        return position.isTurn() == piece.isWhite();
    }

    private ArrayList<Integer> arrayListCoordinate(int row, int column) {
        ArrayList<Integer> coordinate = new ArrayList<>();
        coordinate.add(row);
        coordinate.add(column);
        return coordinate;
    }
}