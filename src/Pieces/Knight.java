package Pieces;

import enums.PieceType;

import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(ArrayList<Integer> coordinate, boolean isWhite, PieceType pieceType) {
        super(coordinate, isWhite, pieceType);
    }

    @Override
    public ArrayList<ArrayList<Integer>> validMoves() {
        ArrayList<ArrayList<Integer>> validMovesList = new ArrayList<>();
        Integer currentRow = this.getCoordinate().get(0);
        Integer currentColumn = this.getCoordinate().get(1);
        for (int row = -2; row <= 2; row++) {
            for (int column = -2; column <= 2; column++) {
                if (Math.abs(row) + Math.abs(column) == 3) {
                    int finalRow = row;
                    int finalColumn = column;
                    validMovesList.add(new ArrayList<>() {
                        {
                            add(currentRow + finalRow);
                            add(currentColumn + finalColumn);
                        }
                    });
                }
            }
        }
        return validMovesList;
    }
}
