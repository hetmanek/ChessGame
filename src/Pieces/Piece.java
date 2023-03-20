package Pieces;

import enums.PieceType;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
    private ArrayList<Integer> coordinate;
    private final boolean isWhite;
    private final PieceType type;

    public Piece(ArrayList<Integer> coordinate, boolean isWhite, PieceType type) {
        this.coordinate = coordinate;
        this.isWhite = isWhite;
        this.type = type;
    }

    public int getImageIndex() {
        return this.type.getImageIndex();
    }

    public ArrayList<Integer> getCoordinate() {
        return coordinate;
    }
    public boolean isWhite() {
        return this.isWhite;
    }

    public abstract ArrayList<ArrayList<Integer>> validMoves();


}