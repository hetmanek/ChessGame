package Pieces;

import enums.PieceType;

import java.util.ArrayList;

public class Queen extends Piece {
    public Queen(ArrayList<Integer> coordinate, boolean isWhite, PieceType pieceType) {
        super(coordinate, isWhite, pieceType);
    }

    @Override
    public ArrayList<ArrayList<Integer>> validMoves() {
        ArrayList<ArrayList<Integer>> validMovesList = new ArrayList<>();
        Integer currentRow = this.getCoordinate().get(0);
        Integer currentColumn = this.getCoordinate().get(1);
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                if (Math.abs(currentRow - row) == Math.abs(currentColumn - column)
                        || (currentRow == row) ^ (currentColumn == column)) {
                    int finalRow = row;
                    int finalColumn = column;
                    validMovesList.add(new ArrayList<>() {
                        {
                            add(finalRow);
                            add(finalColumn);
                        }
                    });
                }
            }
        }
        validMovesList.remove(this.getCoordinate());
        return validMovesList;
    }
}
