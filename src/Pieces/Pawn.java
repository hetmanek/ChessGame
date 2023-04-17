package Pieces;

import enums.PieceType;

import java.util.ArrayList;
import java.util.Objects;

public class Pawn extends Piece {

    public Pawn(ArrayList<Integer> coordinate, boolean isWhite, PieceType pieceType) {
        super(coordinate, isWhite, pieceType);
        if (Objects.equals(coordinate.get(0), getInitialRow())) this.moved = true;
    }

    @Override
    public ArrayList<ArrayList<Integer>> validMoves() {
        ArrayList<ArrayList<Integer>> validMovesList = new ArrayList<>();
        Integer currentRow = this.getCoordinate().get(0);
        Integer currentColumn = this.getCoordinate().get(1);
        int k = this.isWhite() ? -1 : 1;
        if (!this.hasMoved()) {
            validMovesList.add(new ArrayList<>() {
                {
                    add(currentRow + (2 * k));
                    add(currentColumn);
                }
            });
        }
        validMovesList.add(new ArrayList<>() {
            {
                add(currentRow + k);
                add(currentColumn);
            }
        });
        validMovesList.add(new ArrayList<>() {
            {
                add(currentRow + k);
                add(currentColumn + k);
            }
        });
        validMovesList.add(new ArrayList<>() {
            {
                add(currentRow + k);
                add(currentColumn - k);
            }
        });
        return validMovesList;
    }

    private Integer getInitialRow() {
        return this.isWhite ? 1 : 6;
    }
}
