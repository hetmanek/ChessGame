package Board;

import Pieces.*;
import enums.PieceType;

import java.util.*;

public class Position {
    private Map<ArrayList<Integer>, Piece> positionMap = new HashMap<>();
    private boolean turn; // true white, false black
    private int move = 0;
    private int halfmove;
    private int fullmove;
    private Map<Integer, ArrayList<Integer>> enPassantMap = new HashMap<>();
    private ArrayList<Integer> whiteKingCoordinate;
    private ArrayList<Integer> blackKingCoordinate;

    public Position(String fen) {
        char[] fenArray = fen.toCharArray();
        int row = 0;
        int column = 0;
        boolean firstFieldFlag = false;
        int fenField = 0;
        for (char character : fenArray) {
            //fen ex: rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
            if (!firstFieldFlag) {
                if (charIsANumber(character)) {
                    column = column + charToInt(character);
                } else if (charIsALetter(character)) {
                    newPiece(character, arrayListCoordinate(row, column));
                    column++;
                } else if (charIsASlash(character)) {
                    row++;
                    column = 0;
                }
                if (row == 7 && column == 8) firstFieldFlag = true;
            } else {
                if (charIsASpace(character)) {
                    fenField++;
                } else {
                    if (fenField == 1) {
                        this.turn = charIsAw(character);
                    } else if (fenField == 2 && charIsNotADash(character)) {
                        //TODO tem que fazer o rock...
                        //quem pode rockar
                        // - == ninguem
                        // Qk == branca pode para ala da dama e preta pode para ala do rei
                        // KQkq == pode tudo
                    } else if (fenField == 3 && charIsNotADash(character)) {
                        if (charIsALetter(character)) row = charToRow(character);
                        if (charIsANumber(character)) {
                            column = charToColumn(character);
                            this.enPassantMap.put(this.fullmove, arrayListCoordinate(row, column));
                        }
                    } else if (fenField == 4) {
                        this.halfmove = charToInt(character);
                    } else if (fenField == 5) {
                        this.fullmove = charToInt(character);
                    }
                }
            }
        }
    }

    private void newPiece(char character, ArrayList<Integer> coordinate) {
        switch (character) {
            case 'P', 'p' -> this.positionMap.put(coordinate,
                    new Pawn(coordinate, character == 'P', PieceType.valueOf(String.valueOf(character))));

            case 'R', 'r' -> this.positionMap.put(coordinate,
                    new Rook(coordinate, character == 'R', PieceType.valueOf(String.valueOf(character))));

            case 'N', 'n' -> this.positionMap.put(coordinate,
                    new Knight(coordinate, character == 'N', PieceType.valueOf(String.valueOf(character))));

            case 'B', 'b' -> this.positionMap.put(coordinate,
                    new Bishop(coordinate, character == 'B', PieceType.valueOf(String.valueOf(character))));

            case 'Q', 'q' -> this.positionMap.put(coordinate,
                    new Queen(coordinate, character == 'Q', PieceType.valueOf(String.valueOf(character))));

            case 'K', 'k' -> {
                this.positionMap.put(coordinate,
                        new King(coordinate, character == 'K', PieceType.valueOf(String.valueOf(character))));
                if (character == 'K') {
                    this.whiteKingCoordinate = coordinate;
                } else this.blackKingCoordinate = coordinate;
            }
        }
    }

    public Map<ArrayList<Integer>, Piece> getPositionMap() {
        return this.positionMap;
    }

    public Piece getPieceAt(ArrayList<Integer> coordinate) {
        return this.positionMap.get(coordinate);
    }

    public boolean isTurn() {
        return this.turn;
    }

    public void movePiece(Piece piece, ArrayList<Integer> destination) {//TODO NÃO SEI COMO CLENAR ISSO AQUI
        if (piece.isWhite() == this.turn && realValidMoves(piece, false).stream().anyMatch(destination::equals)) {
            if (piece.getClass() == Pawn.class) enPassant(piece, destination);
            if (piece.getClass() == King.class) {
                if (piece.isWhite()) {
                    this.whiteKingCoordinate = destination;
                } else this.blackKingCoordinate = destination;
            }

            this.positionMap.remove(piece.getCoordinate());
            piece.setCoordinate(destination);
            this.positionMap.put(piece.getCoordinate(), piece);

            this.turn = !this.turn;
            this.move++;
            if (!piece.isWhite()) this.fullmove++;
        }
    }

    public ArrayList<ArrayList<Integer>> realValidMoves(Piece piece, boolean flag) {
        ArrayList<ArrayList<Integer>> validMoves = new ArrayList<>(piece.validMoves());
        validMoves.removeIf(x -> isAnAlliedPiece(piece, x));
        validMoves.removeIf(this::isOutOfBounds);

        if (piece.getClass() != Knight.class) {
            validMoves.removeIf(x -> hasPieceBetween(piece.getCoordinate(), x));
        }
        if (piece.getClass() == Pawn.class) {
            validMoves.removeIf(x -> pawnConstraint(piece.getCoordinate(), x));
        }
        if (piece.getClass() == King.class) {
            validMoves.removeIf(x -> isDestinationUnderAttack(piece.isWhite(), x, this.positionMap));
        }
        if (!flag && isKingInCheck(piece.isWhite(), this.positionMap)) {
            System.out.println("teu rei tá em cheque");
            validMoves.removeIf(x -> !moveResolvesCheck(piece, x)); //todo mds o primeiro bug surgio tudo antes disso tá funcionando
        }
        return validMoves;
    }

    private boolean isAnAlliedPiece(Piece piece, ArrayList<Integer> destination) {
        return getPieceAt(destination) != null && piece.isWhite() == getPieceAt(destination).isWhite();
    }

    private boolean isOutOfBounds(ArrayList<Integer> destination) {
        return destination.get(0) > 7 || destination.get(1) > 7;
    }

    private boolean hasPieceBetween(ArrayList<Integer> exitPoint, ArrayList<Integer> destination) { //TODO mds demorei pra entender oq eu fiz aqui kkk
        if (Objects.equals(exitPoint.get(0), destination.get(0))) {
            int kc = exitPoint.get(1) < destination.get(1) ? 1 : -1;
            ArrayList<Integer> currentCoordinate = arrayListCoordinate(exitPoint.get(0), exitPoint.get(1) + kc);
            for (int column = currentCoordinate.get(1);
                 !Objects.equals(currentCoordinate, destination);
                 column = column + kc) {
                if (getPieceAt(currentCoordinate) != null) return true;
                currentCoordinate.set(1, column);
            }
        } else if (Objects.equals(exitPoint.get(1), destination.get(1))) {
            int kr = exitPoint.get(0) < destination.get(0) ? 1 : -1;
            ArrayList<Integer> currentCoordinate = arrayListCoordinate(exitPoint.get(0) + kr, exitPoint.get(1));
            for (int row = currentCoordinate.get(0);
                 !Objects.equals(currentCoordinate, destination);
                 row = row + kr) {
                if (getPieceAt(currentCoordinate) != null) return true;
                currentCoordinate.set(0, row);
            }
        } else if ((Math.abs(exitPoint.get(0) - destination.get(0)) == Math.abs(exitPoint.get(1) - destination.get(1)))) { // mesma diagonal
            int kr = exitPoint.get(0) < destination.get(0) ? 1 : -1;
            int kc = exitPoint.get(1) < destination.get(1) ? 1 : -1;
            ArrayList<Integer> currentCoordinate = arrayListCoordinate(exitPoint.get(0) + kr, exitPoint.get(1) + kc);
            for (int row = currentCoordinate.get(0), column = currentCoordinate.get(1);
                 !Objects.equals(currentCoordinate, destination);
                 row = row + kr, column = column + kc) {
                if (getPieceAt(currentCoordinate) != null) return true;
                currentCoordinate.set(0, row);
                currentCoordinate.set(1, column);
            }
        }
        return false;
    }

    private boolean pawnConstraint(ArrayList<Integer> exitPoint, ArrayList<Integer> destination) {
        if ((Math.abs(exitPoint.get(0) - destination.get(0)) == Math.abs(exitPoint.get(1) - destination.get(1)))) {//é um movimento diagonal(kill move)
            if (getPieceAt(destination) == null) {
                return !canEnPassant(destination);
            }
        } else if (Math.abs(exitPoint.get(0) - destination.get(0)) == 2) { //first move
            return getPieceAt(exitPoint).hasMoved();
        }
        return getPieceAt(destination) != null;
    }

    private boolean canEnPassant(ArrayList<Integer> destination) {
        if (this.enPassantMap.containsKey(this.move - 1)) {
            return this.enPassantMap.get(this.move - 1).equals(destination);
        }
        return false;
    }

    private void enPassant(Piece piece, ArrayList<Integer> destination) {
        int k = piece.isWhite() ? -1 : 1;
        if (Math.abs(piece.getCoordinate().get(0) - destination.get(0)) == 2) { //leap
            this.enPassantMap.put(this.move, arrayListCoordinate(destination.get(0) - k, destination.get(1)));
        }
        if (getPieceAt(destination) == null && (Math.abs(piece.getCoordinate().get(0) - destination.get(0)) == Math.abs(piece.getCoordinate().get(1) - destination.get(1)))) {
            this.positionMap.remove(arrayListCoordinate(destination.get(0) - k, destination.get(1)));
        }
    }

    private boolean isDestinationUnderAttack(Boolean isWhite, ArrayList<Integer> destination, Map<ArrayList<Integer>, Piece> positionMap) { //TODO funciona...mas tá feio
        for (Piece pieceInMap : positionMap.values()) {
            if (pieceInMap.isWhite() != isWhite) {
                //compara somente as diagonais do peão, pois elas não aparecem como valid move se não tiver uma peça lá
                //e o mover para frente do peão é um valid move porem não pode sobrepor peça, logo não deve ser comparado
                if (pieceInMap.getClass() == Pawn.class) {
                    int k = pieceInMap.isWhite() ? -1 : 1;
                    ArrayList<ArrayList<Integer>> pawnDiagonals = new ArrayList<>();
                    pawnDiagonals.add(arrayListCoordinate(pieceInMap.getCoordinate().get(0) + k, pieceInMap.getCoordinate().get(1) + k));
                    pawnDiagonals.add(arrayListCoordinate(pieceInMap.getCoordinate().get(0) + k, pieceInMap.getCoordinate().get(1) - k));
                    if (pawnDiagonals
                            .stream()
                            .anyMatch(destination::equals)) {
                        return true;
                    }
                    //compara com todos os moves do rei, pois tentase comparar com realvalidmoves entraria em um loop infinito
                } else if (pieceInMap.getClass() == King.class) {
                    if (pieceInMap.validMoves()
                            .stream()
                            .anyMatch(destination::equals)) {
                        return true;
                    }
                } else if (realValidMoves(pieceInMap, true)
                        .stream()
                        .anyMatch(destination::equals)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isKingInCheck(boolean isWhite, Map<ArrayList<Integer>, Piece> positionMap) { //TODO TÁ SÓ A ITERAÇÃO ESSES MÉTODOS DE REI
        ArrayList<Integer> kingCoordinate = isWhite ? this.whiteKingCoordinate : this.blackKingCoordinate;
        return isDestinationUnderAttack(isWhite, kingCoordinate, positionMap);
    }

    private boolean moveResolvesCheck(Piece pieceToMove, ArrayList<Integer> destination) {
        Map<ArrayList<Integer>, Piece> positionCopy = this.positionMap;

        positionCopy.remove(pieceToMove.getCoordinate());
        positionCopy.put(destination, pieceToMove);

        ArrayList<Integer> kingCoordinate;
        if (pieceToMove.getClass() == King.class) {
            kingCoordinate = destination;
        } else {
            kingCoordinate = pieceToMove.isWhite() ? this.whiteKingCoordinate : this.blackKingCoordinate;
        }

        return !isDestinationUnderAttack(pieceToMove.isWhite(), kingCoordinate, positionCopy);
    }

    private boolean charIsANumber(char character) {
        return 47 < character && character < 58;
    }

    private boolean charIsALetter(char character) {
        return 64 < character && character < 123;
    }

    private boolean charIsASlash(char character) {
        return character == 47;
    }

    private boolean charIsASpace(char character) {
        return character == 32;
    }

    private boolean charIsAw(char character) {
        return character == 119;
    }

    private boolean charIsNotADash(char character) {
        return character != 45;
    }

    private int charToInt(char character) {
        return character - 48;
    }

    private int charToRow(char character) {
        return character - 96;
    }

    private int charToColumn(char character) {
        return character - 47;
    }

    private ArrayList<Integer> arrayListCoordinate(int row, int column) {
        ArrayList<Integer> coordinate = new ArrayList<>();
        coordinate.add(row);
        coordinate.add(column);
        return coordinate;
    }
}
