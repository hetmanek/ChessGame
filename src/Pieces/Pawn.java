package Pieces;

import enums.PieceType;

import java.util.ArrayList;


public class Pawn extends Piece {
    public Pawn(ArrayList<Integer> coordinate, boolean isWhite, PieceType pieceType) {
        super(coordinate, isWhite, pieceType);
    }

    @Override
    public ArrayList<ArrayList<Integer>> validMoves() {
        ArrayList<ArrayList<Integer>> validMovesList = new ArrayList<>();
        Integer currentRow = this.getCoordinate().get(0);
        Integer currentColumn = this.getCoordinate().get(1);
        int k = this.isWhite() ? -1 : 1;
        if (!this.hasMoved()) { //2 para frente
            validMovesList.add(new ArrayList<>() {
                {
                    add(currentRow + (2 * k));
                    add(currentColumn);
                }
            });
        }
        //1 para frente
        validMovesList.add(new ArrayList<>() {
            {
                add(currentRow + k);
                add(currentColumn);
            }
        });
        //kill move direita
        validMovesList.add(new ArrayList<>() {
            {
                add(currentRow + k);
                add(currentColumn + k);
            }
        });
        //kill move esquerda
        validMovesList.add(new ArrayList<>() {
            {
                add(currentRow + k);
                add(currentColumn - k);
            }
        });
        return validMovesList;
    }
}
