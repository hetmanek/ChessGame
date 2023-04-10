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

    public ArrayList<Boolean> getCastlingRights() {
        return this.castlingRights;
    }

    @Override
    public ArrayList<ArrayList<Integer>> validMoves() {//TODO DE REPENTE DA PRA FAZER MELHOR ESSES MOVES AQUI, TÁ MUITO MANUAL...
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
        if (this.castlingRights.get(0)) {//ala da dama
            validMovesList.add(new ArrayList<>() {
                {
                    add(currentRow);
                    add(currentColumn - 2 * k);
                }
            });
        }
        if (this.castlingRights.get(1)) {//ala do rei
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
