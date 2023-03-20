package Pieces;

import enums.PieceType;

import java.util.ArrayList;

public class King extends Piece {

    public King(ArrayList<Integer> coordinate, boolean isWhite, PieceType pieceType) {
        super(coordinate, isWhite, pieceType);
    }

    @Override
    public ArrayList<ArrayList<Integer>> validMoves() {
        ArrayList<ArrayList<Integer>> validMovesList = new ArrayList<>();
        Integer currentRow = this.getCoordinate().get(0);
        Integer currentColumn = this.getCoordinate().get(1);
        int k = this.isWhite() ? -1 : 1;
        //N
        validMovesList.add(new ArrayList<>() {
            {
                add(currentRow + k);
                add(currentColumn);
            }
        });
        //NE
        validMovesList.add(new ArrayList<>() {
            {
                add(currentRow + k);
                add(currentColumn + k);
            }
        });
        //E
        validMovesList.add(new ArrayList<>() {
            {
                add(currentRow);
                add(currentColumn + k);
            }
        });
        //SE
        validMovesList.add(new ArrayList<>() {
            {
                add(currentRow - k);
                add(currentColumn + k);
            }
        });
        //S
        validMovesList.add(new ArrayList<>() {
            {
                add(currentRow - k);
                add(currentColumn);
            }
        });
        //SO
        validMovesList.add(new ArrayList<>() {
            {
                add(currentRow - k);
                add(currentColumn - k);
            }
        });
        //O
        validMovesList.add(new ArrayList<>() {
            {
                add(currentRow);
                add(currentColumn - k);
            }
        });
        //NO
        validMovesList.add(new ArrayList<>() {
            {
                add(currentRow + k);
                add(currentColumn - k);
            }
        });
        return validMovesList;
    }
}
