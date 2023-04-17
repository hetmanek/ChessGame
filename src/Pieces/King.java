package Pieces;

import enums.PieceType;

import java.util.ArrayList;

public class King extends Piece {
    public King(ArrayList<Integer> coordinate, boolean isWhite, PieceType pieceType) {
        super(coordinate, isWhite, pieceType);
    }

    private ArrayList<Boolean> castlingRights = new ArrayList<>() {
        {
            add(false);
            add(false);
        }
    };

    public void setCastlingRights(int index, boolean canCastle) {
        this.castlingRights.set(index, canCastle);
    }

    @Override
    public ArrayList<ArrayList<Integer>> validMoves() {
        ArrayList<ArrayList<Integer>> validMovesList = new ArrayList<>();
        Integer currentRow = this.getCoordinate().get(0);
        Integer currentColumn = this.getCoordinate().get(1);
        int k = this.isWhite() ? -1 : 1;

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x == 0 && y == 0) continue;
                int row = currentRow + x * k;
                int column = currentColumn + y * k;
                validMovesList.add(new ArrayList<>() {
                    {
                        add(row);
                        add(column);
                    }
                });
            }
        }
        if (this.castlingRights.get(0)) {
            validMovesList.add(new ArrayList<>() {
                {
                    add(currentRow);
                    add(currentColumn - 2 * k);
                }
            });
        }
        if (this.castlingRights.get(1)) {
            validMovesList.add(new ArrayList<>() {
                {
                    add(currentRow);
                    add(currentColumn + 2 * k);
                }
            });
        }
        return validMovesList;
    }
}
