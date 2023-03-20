package Board;

import Pieces.*;
import enums.PieceType;
import java.util.*;

public class Position {
    private Map<ArrayList<Integer>, Piece> positionMap = new HashMap<>();

    public Position(String fen) {
        char[] fenArray = fen.toCharArray();
        int row = 0;
        int column = 0;
        boolean flag = false;
        int count = 0;
        for (char character : fenArray) {
            //  rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
            if (!flag) {
                if (47 < character && character < 58) {
                    column = column + character - 48;
                    // é um número fazer board position + i - 48
                } else if (64 < character && character < 123) {
                    ArrayList<Integer> coordinate = new ArrayList<>();
                    coordinate.add(row);
                    coordinate.add(column);
                    newPiece(character, coordinate);
                    column++;
                    // é letra
                } else if (character == 47) {
                    row++;
                    column = 0;
                    //é uma / pular linha
                }
                if (row == 7 && column == 8) flag = true;
            } else {
                //TODO
                //parte depois das peças
                if (count == 1) {
                    //TODO
                    //character desta posição representa quem pode jogar
                }
                if (count == 3) {
                    //TODO
                    //quem pode rockar
                    // - == ninguem
                    // Qk == branca pode para ala da dama e preta pode para ala do rei
                    // KQkq == pode tudo
                }
            }
        }
    }

    private void newPiece(char character, ArrayList<Integer> coordinate) {
        switch (character) {
            case 'P', 'p' -> positionMap.put(coordinate,
                    new Pawn(coordinate, character == 'P', PieceType.valueOf(String.valueOf(character))));

            case 'R', 'r' -> positionMap.put(coordinate,
                    new Rook(coordinate, character == 'R', PieceType.valueOf(String.valueOf(character))));

            case 'N', 'n' -> positionMap.put(coordinate,
                    new Knight(coordinate, character == 'N', PieceType.valueOf(String.valueOf(character))));

            case 'B', 'b' -> positionMap.put(coordinate,
                    new Bishop(coordinate, character == 'B', PieceType.valueOf(String.valueOf(character))));

            case 'Q', 'q' -> positionMap.put(coordinate,
                    new Queen(coordinate, character == 'Q', PieceType.valueOf(String.valueOf(character))));

            case 'K', 'k' -> positionMap.put(coordinate,
                    new King(coordinate, character == 'K', PieceType.valueOf(String.valueOf(character))));
        }
    }

    public Map<ArrayList<Integer>, Piece> getPositionMap() {
        return this.positionMap;
    }

    public Piece getPieceAt(ArrayList<Integer> coordinate) {
        return this.positionMap.get(coordinate);
    }
}
