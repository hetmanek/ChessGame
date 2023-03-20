package Board;

import Pieces.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


//TODO implementar visao das negra via asWhite
public class Board {
    private JFrame frame;
    private JPanel panel;
    private Image[] images = pieceImages();
    private Piece selectedPiece = null;

    public Board(String startAs, boolean asWhite) {
        this.frame = new JFrame("Hetmachess");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setBounds(20, 20, 768, 791);
        Position position = new Position(startAs);
        this.panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                squareColor(g);
                drawPosition(position.getPositionMap(), g);
            }
        };
        frame.add(panel);
        frame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                ArrayList<Integer> coordinate = new ArrayList<>() {
                    {
                        add(e.getY() / 94);
                        add(e.getX() / 94);
                    }
                };
                selectedPiece = position.getPieceAt(coordinate);
                if (selectedPiece != null) {
                    //TODO HIGHLIGTH POSSIBLE MOVES
                    System.out.println(selectedPiece.validMoves());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                List<Integer> coordinate = new ArrayList<>();
                coordinate.add(e.getY() / 94);
                coordinate.add(e.getX() / 94);
//                selectedPiece.moveTo(coordinate);
                frame.repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        frame.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedPiece != null) {
                    List<Integer> coordinate = new ArrayList<>();
                    coordinate.add(e.getY() / 94);
                    coordinate.add(e.getX() / 94);
                    //TODO ARRASTA A PEÇA
//                    selectedPiece.setCoordinate(coordinate);
                    frame.repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
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
                g.fillRect(row * 94, column * 94, 94, 94);
            }
        }
    }

    private Image[] pieceImages() {
        try {
            BufferedImage imagePieces = ImageIO.read(new File("C:\\Users\\kaiov\\IdeaProjects\\ChessGame\\src\\chess.png"));
            Image[] images = new Image[12];
            int index = 0;
            for (int y = 0; y < 400; y += 200) {
                for (int x = 0; x < 1200; x += 200) {
                    images[index] = imagePieces.getSubimage(x, y, 200, 200).getScaledInstance(94, 94, BufferedImage.SCALE_SMOOTH);
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
                        g.drawImage(this.images[v.getImageIndex()], k.get(1) * 94, k.get(0) * 94, this.panel);
                }
        );
    }
}
