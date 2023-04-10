package Pieces;

import enums.PieceType;

import java.util.ArrayList;

public abstract class Piece {
    protected ArrayList<Integer> coordinate;
    protected final boolean isWhite;
    protected final PieceType type;
    protected boolean moved = false; //TODO DA MERDA 100% SE UM PEÃO FOR COLOCADO NO MEIO DA BOARD A PARTIR DE UM FEN

    public Piece(ArrayList<Integer> coordinate, boolean isWhite, PieceType type) {
        this.coordinate = coordinate;
        this.isWhite = isWhite;
        this.type = type;
    }

    public int getImageIndex() {
        return this.type.getImageIndex();
    }

    public void setCoordinate(ArrayList<Integer> coordinate) {
        this.coordinate = coordinate;
        this.moved = true;
    }

    public ArrayList<Integer> getCoordinate() {
        return coordinate;
    }

    public boolean isWhite() {
        return this.isWhite;
    }

    public boolean hasMoved() {
        return this.moved;
    }

    public abstract ArrayList<ArrayList<Integer>> validMoves();
}