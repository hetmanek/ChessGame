package Pieces;

import enums.PieceType;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    private boolean firstMove = true;

    public Pawn(ArrayList<Integer> coordinate, boolean isWhite, PieceType pieceType) {
        super(coordinate, isWhite, pieceType);
    }

    @Override
    public ArrayList<ArrayList<Integer>> validMoves() {
        ArrayList<ArrayList<Integer>> validMovesList = new ArrayList<>();
        Integer currentRow = this.getCoordinate().get(0);
        Integer currentColumn = this.getCoordinate().get(1);
        int k = this.isWhite() ? -1 : 1;
        if (firstMove) { //2 para frente
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
        //TODO En passant
        /**
         An en passant capture can occur after a pawn makes a move of two squares and the square it passes over is
         attacked by an enemy pawn. The enemy pawn is entitled to capture the moved pawn "in passing" as if the latter
         had advanced only one square. The capturing pawn moves to the square over which the moved pawn passed (see diagram),
         and the moved pawn is removed from the board. The option to capture the moved pawn en passant must be exercised
         on the move immediately following the double-step pawn advance, or it is lost for the remainder of the game.
         The en passant capture is the only capture in chess in which the capturing piece does not replace the captured
         piece on the same square.
         */
    }

}
