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
        int k = this.isWhite() ? -1 : 1;
        //frente direita
        validMovesList.add(new ArrayList<>() {
            {
                add(currentRow + (2*k));
                add(currentColumn + k);
            }
        });
        //frente esquerda
        validMovesList.add(new ArrayList<>() {
            {
                add(currentRow + (2*k));
                add(currentColumn - k);
            }
        });
        //direita frente
        validMovesList.add(new ArrayList<>() {
            {
                add(currentRow + k);
                add(currentColumn + (2*k));
            }
        });
        //direita trás
        validMovesList.add(new ArrayList<>() {
            {
                add(currentRow - k);
                add(currentColumn + (2*k));
            }
        });
        //esquerda frente
        validMovesList.add(new ArrayList<>() {
            {
                add(currentRow + k);
                add(currentColumn - (2*k));
            }
        });
        //esquerda trás
        validMovesList.add(new ArrayList<>() {
            {
                add(currentRow - k);
                add(currentColumn - (2*k));
            }
        });
        //trás direita
        validMovesList.add(new ArrayList<>() {
            {
                add(currentRow - (2*k));
                add(currentColumn + k);
            }
        });
        //trás esquerda
        validMovesList.add(new ArrayList<>() {
            {
                add(currentRow - (2*k));
                add(currentColumn - k);
            }
        });
        return validMovesList;
    }
}
