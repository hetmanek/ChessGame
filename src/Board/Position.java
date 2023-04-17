package Board;

import Pieces.*;
import enums.PieceType;

import java.util.*;

public class Position {
    private Map<ArrayList<Integer>, Piece> positionMap = new HashMap<>();
    private Map<Integer, ArrayList<Integer>> enPassantMap = new HashMap<>();
    private boolean isWhiteTurn;
    private int move = 0;
    private int halfmove;
    private int fullmove; //todo pra q?
    private ArrayList<Integer> whiteKingCoordinate;
    private ArrayList<Integer> blackKingCoordinate;

    public Position(String fen) {
        int row = 0;
        int column = 0;
        int fenField = 0;
        for (char character : fen.toCharArray()) {
            if (this.charIsASpace(character)) fenField++;
            if (fenField == 0) {
                if (charIsANumber(character)) {
                    column += charToInt(character);
                } else if (charIsALetter(character)) {
                    newPiece(character, arrayListCoordinate(row, column));
                    column++;
                } else if (charIsASlash(character)) {
                    row++;
                    column = 0;
                }
            } else if (fenField == 1) {
                this.isWhiteTurn = charIsAw(character);
            } else if (fenField == 2 && charIsNotADash(character)) {
                switch (character) {
                    case 'Q' -> ((King) getPieceAt(whiteKingCoordinate, this.positionMap)).setCastlingRights(0, true);
                    case 'K' -> ((King) getPieceAt(whiteKingCoordinate, this.positionMap)).setCastlingRights(1, true);
                    case 'q' -> ((King) getPieceAt(blackKingCoordinate, this.positionMap)).setCastlingRights(0, true);
                    case 'k' -> ((King) getPieceAt(blackKingCoordinate, this.positionMap)).setCastlingRights(1, true);
                }
            } else if (fenField == 3 && charIsNotADash(character)) {
                if (this.charIsALetter(character)) row = this.charToRow(character);
                if (this.charIsANumber(character)) {
                    column = charToColumn(character);
                    this.enPassantMap.put(this.move, arrayListCoordinate(row, column));
                }
            } else if (fenField == 4) {
                this.halfmove = charToInt(character);
            } else if (fenField == 5) {
                this.fullmove = charToInt(character);
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

    public void movePiece(Piece piece, ArrayList<Integer> destination) {
        if (isValidMove(piece, destination)) {
            if (piece.getClass() == Pawn.class) piece = pawnMove(piece, destination);
            if (piece.getClass() == King.class) kingMove((King) piece, destination);
            updateGame(piece, destination);

            this.positionMap.remove(piece.getCoordinate());
            piece.setCoordinate(destination);
            this.positionMap.put(piece.getCoordinate(), piece);

            gameOver(!piece.isWhite());
        }
    }

    private boolean isValidMove(Piece piece, ArrayList<Integer> destination) {
        return piece.isWhite() == this.isWhiteTurn
                && positionValidMoves(piece, this.positionMap, false).stream().anyMatch(destination::equals);
    }

    private Piece pawnMove(Piece pawn, ArrayList<Integer> destination) {
        enPassant(pawn, destination);
        return promotion(pawn, destination);
    }

    private void kingMove(King king, ArrayList<Integer> destination) {
        king.setCastlingRights(0, false);
        king.setCastlingRights(1, false);
        if (king.isWhite()) {
            this.whiteKingCoordinate = destination;
        } else this.blackKingCoordinate = destination;
        castle(king, destination);
    }

    private void updateGame(Piece piece, ArrayList<Integer> destination) {
        if (piece.getClass() == Pawn.class || getPieceAt(destination, this.positionMap) != null) {
            this.halfmove = 0;
        } else this.halfmove++;
        if (this.halfmove == 100) Board.draw();
        this.isWhiteTurn = !this.isWhiteTurn;
        this.move++;
        if (!piece.isWhite()) this.fullmove++;
    }

    private void gameOver(boolean isWhite) {
        boolean hasAnyPositionValidMove = false;
        for (Piece pieceInMap : this.positionMap.values()) {
            if (pieceInMap.isWhite() == isWhite) {
                if (!positionValidMoves(pieceInMap, this.positionMap, false).isEmpty()) {
                    hasAnyPositionValidMove = true;
                    break;
                }
            }
        }
        if (!hasAnyPositionValidMove) {
            if (isKingInCheck(isWhite, this.positionMap)) {
                Board.checkmate(!isWhite);
            } else Board.draw();
        }
    }

//    public ArrayList<ArrayList<Integer>> positionValidMoves(Piece piece, Map<ArrayList<Integer>, Piece> positionMap, boolean flag) { //TODO NÃO SEI TIRAR ESSE FLAG AAAA
//        ArrayList<ArrayList<Integer>> validMoves = new ArrayList<>(piece.validMoves());
//
//        validMoves.removeIf(this::isOutOfBounds);
//        validMoves.removeIf(destination -> isAnAlliedPiece(piece, destination, positionMap));
//
//        if (piece.getClass() != Knight.class)
//            validMoves.removeIf(destination -> hasPieceBetween(piece.getCoordinate(), destination, positionMap));
//
//        if (piece.getClass() == Pawn.class)
//            validMoves.removeIf(destination -> pawnConstraint(piece.getCoordinate(), destination, positionMap));
//
//        if (piece.getClass() == King.class)
//            validMoves.removeIf(destination -> kingConstraint(piece, destination, positionMap));
//
//        else if (!flag) validMoves.removeIf(destination -> kingIsPinned(piece, destination, positionMap));
//
//        if (!flag && isKingInCheck(piece.isWhite(), positionMap))
//            validMoves.removeIf(destination -> !moveResolvesCheck(piece, destination, positionMap));
//
//        return validMoves;
//    }


    public ArrayList<ArrayList<Integer>> positionValidMoves(Piece piece, Map<ArrayList<Integer>, Piece> positionMap, boolean flag) { //TODO NÃO SEI TIRAR ESSE FLAG AAAA
        ArrayList<ArrayList<Integer>> validMoves = new ArrayList<>(piece.validMoves());

        validMoves.removeIf(destination -> moveIsUnavailable(piece, destination, positionMap, flag));
        return validMoves;
    }

    private boolean moveIsUnavailable(Piece piece, ArrayList<Integer> destination, Map<ArrayList<Integer>, Piece> positionMap, boolean flag) {
        if (isOutOfBounds(destination)) return true;
        if (isAnAlliedPiece(piece, destination, positionMap)) return true;
        if (piece.getClass() != Knight.class)
            if (hasPieceBetween(piece.getCoordinate(), destination, positionMap)) return true;
        if (piece.getClass() == Pawn.class)
            if (pawnConstraint(piece.getCoordinate(), destination, positionMap)) return true;

        if (piece.getClass() == King.class) {
            if (kingConstraint(piece, destination, positionMap)) return true;
        } else if (!flag) if (kingIsPinned(piece, destination, positionMap)) return true;

        if (!flag && isKingInCheck(piece.isWhite(), positionMap))
            if (!moveResolvesCheck(piece, destination, positionMap)) return true;

        return false;
    }


    private boolean isAnAlliedPiece(Piece piece, ArrayList<Integer> destination, Map<ArrayList<Integer>, Piece> positionMap) {
        return getPieceAt(destination, positionMap) != null && piece.isWhite() == getPieceAt(destination, positionMap).isWhite();
    }

    private boolean isOutOfBounds(ArrayList<Integer> destination) {
        return destination.get(0) < 0 || destination.get(0) > 7 || destination.get(1) < 0 || destination.get(1) > 7;
    }

    private boolean kingIsPinned(Piece pieceToMove, ArrayList<Integer> destination, Map<ArrayList<Integer>, Piece> positionMap) {
        Map<ArrayList<Integer>, Piece> positionCopy = new HashMap<>(positionMap);

        positionCopy.remove(pieceToMove.getCoordinate());
        positionCopy.put(destination, pieceToMove);

        return isKingInCheck(pieceToMove.isWhite(), positionCopy);
    }

    private boolean hasPieceBetween(ArrayList<Integer> exitPoint, ArrayList<Integer> destination, Map<ArrayList<Integer>, Piece> positionMap) {
        if (Objects.equals(exitPoint.get(0), destination.get(0))) {
            int columnIncrement = exitPoint.get(1) < destination.get(1) ? 1 : -1;
            ArrayList<Integer> currentCoordinate = arrayListCoordinate(exitPoint.get(0), exitPoint.get(1) + columnIncrement);
            for (int column = currentCoordinate.get(1);
                 !Objects.equals(currentCoordinate, destination);
                 column += columnIncrement) {
                if (getPieceAt(currentCoordinate, positionMap) != null) return true;
                currentCoordinate.set(1, column);
            }
        } else if (Objects.equals(exitPoint.get(1), destination.get(1))) {
            int rowIncrement = exitPoint.get(0) < destination.get(0) ? 1 : -1;
            ArrayList<Integer> currentCoordinate = arrayListCoordinate(exitPoint.get(0) + rowIncrement, exitPoint.get(1));
            for (int row = currentCoordinate.get(0);
                 !Objects.equals(currentCoordinate, destination);
                 row += rowIncrement) {
                if (getPieceAt(currentCoordinate, positionMap) != null) return true;
                currentCoordinate.set(0, row);
            }
        } else if ((Math.abs(exitPoint.get(0) - destination.get(0)) == Math.abs(exitPoint.get(1) - destination.get(1)))) { // mesma diagonal
            int rowIncrement = exitPoint.get(0) < destination.get(0) ? 1 : -1;
            int columnIncrement = exitPoint.get(1) < destination.get(1) ? 1 : -1;
            ArrayList<Integer> currentCoordinate = arrayListCoordinate(exitPoint.get(0) + rowIncrement, exitPoint.get(1) + columnIncrement);
            for (int row = currentCoordinate.get(0), column = currentCoordinate.get(1);
                 !Objects.equals(currentCoordinate, destination);
                 row += rowIncrement, column += columnIncrement) {
                if (getPieceAt(currentCoordinate, positionMap) != null) return true;
                currentCoordinate.set(0, row);
                currentCoordinate.set(1, column);
            }
        }
        return false;
    }

    private boolean pawnConstraint(ArrayList<Integer> exitPoint, ArrayList<Integer> destination, Map<ArrayList<Integer>, Piece> positionMap) {
        if ((Math.abs(exitPoint.get(0) - destination.get(0)) == Math.abs(exitPoint.get(1) - destination.get(1)))) {
            if (getPieceAt(destination, positionMap) == null) {
                return !canEnPassant(destination);
            } else return false;
        } else if (Math.abs(exitPoint.get(0) - destination.get(0)) == 2) {
            if (getPieceAt(exitPoint, positionMap).hasMoved()) return true;
        }
        return getPieceAt(destination, positionMap) != null;
    }

    private boolean canEnPassant(ArrayList<Integer> destination) {
        if (this.enPassantMap.containsKey(this.move - 1)) {
            return this.enPassantMap.get(this.move - 1).equals(destination);
        }
        return false;
    }

    private void enPassant(Piece piece, ArrayList<Integer> destination) {
        int pawnDirection = piece.isWhite() ? -1 : 1;
        if (Math.abs(piece.getCoordinate().get(0) - destination.get(0)) == 2) {
            this.enPassantMap.put(this.move, arrayListCoordinate(destination.get(0) - pawnDirection, destination.get(1)));
        }
        if (getPieceAt(destination, this.positionMap) == null
                && (Math.abs(piece.getCoordinate().get(0) - destination.get(0)) == Math.abs(piece.getCoordinate().get(1) - destination.get(1)))) {
            this.positionMap.remove(arrayListCoordinate(destination.get(0) - pawnDirection, destination.get(1)));
        }
    }

    private Piece promotion(Piece pawn, ArrayList<Integer> destination) {
        if (destination.get(0) == 0 || destination.get(0) == 7) {
            switch (Board.promotion()) {
                case 0 ->
                        pawn = new Rook(pawn.getCoordinate(), pawn.isWhite(), PieceType.valueOf(pawn.isWhite() ? "R" : "r"));

                case 1 ->
                        pawn = new Knight(pawn.getCoordinate(), pawn.isWhite(), PieceType.valueOf(pawn.isWhite() ? "N" : "n"));

                case 2 ->
                        pawn = new Bishop(pawn.getCoordinate(), pawn.isWhite(), PieceType.valueOf(pawn.isWhite() ? "B" : "b"));

                case 3 ->
                        pawn = new Queen(pawn.getCoordinate(), pawn.isWhite(), PieceType.valueOf(pawn.isWhite() ? "Q" : "q"));
            }
        }
        return pawn;
    }

    private boolean isDestinationUnderAttack(Boolean isWhite, ArrayList<Integer> destination, Map<ArrayList<Integer>, Piece> positionMap) {
        for (Piece pieceInMap : positionMap.values()) {
            if (pieceInMap.isWhite() != isWhite) {
                if (pieceInMap.getClass() == Pawn.class) {
                    int pawnDirection = pieceInMap.isWhite() ? -1 : 1;
                    ArrayList<ArrayList<Integer>> pawnDiagonals = new ArrayList<>();
                    pawnDiagonals.add(arrayListCoordinate(pieceInMap.getCoordinate().get(0) + pawnDirection, pieceInMap.getCoordinate().get(1) + pawnDirection));
                    pawnDiagonals.add(arrayListCoordinate(pieceInMap.getCoordinate().get(0) + pawnDirection, pieceInMap.getCoordinate().get(1) - pawnDirection));
                    if (pawnDiagonals
                            .stream()
                            .anyMatch(destination::equals)) {
                        return true;
                    }
                } else if (pieceInMap.getClass() == King.class) {
                    if (pieceInMap.validMoves()
                            .stream()
                            .anyMatch(destination::equals)) {
                        return true;
                    }
                } else if (positionValidMoves(pieceInMap, positionMap, true)
                        .stream()
                        .anyMatch(destination::equals)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isKingInCheck(boolean isWhite, Map<ArrayList<Integer>, Piece> positionMap) {
        ArrayList<Integer> kingCoordinate = isWhite ? this.whiteKingCoordinate : this.blackKingCoordinate;
        return isDestinationUnderAttack(isWhite, kingCoordinate, positionMap);
    }

    private boolean moveResolvesCheck(Piece pieceToMove, ArrayList<Integer> destination, Map<ArrayList<Integer>, Piece> positionMap) {
        Map<ArrayList<Integer>, Piece> positionCopy = new HashMap<>(positionMap);

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

    private boolean kingConstraint(Piece king, ArrayList<Integer> destination, Map<ArrayList<Integer>, Piece> positionMap) {
        if (isDestinationUnderAttack(king.isWhite(), destination, positionMap)) return true;
        if (Math.abs(king.getCoordinate().get(1) - destination.get(1)) == 2) {
            return !canCastle(king, destination, positionMap);
        }
        return false;
    }

    private boolean canCastle(Piece king, ArrayList<Integer> destination, Map<ArrayList<Integer>, Piece> positionMap) {
        boolean isQueenSideCastle = king.getCoordinate().get(1) - destination.get(1) > 0;
        int rookColumnOffset = isQueenSideCastle ? -4 : 3;
        int kingColumnIncrement = isQueenSideCastle ? -1 : 1;

        ArrayList<Integer> rookCoordinate = getRookCoordinate(king, rookColumnOffset);
        if (!getPieceAt(rookCoordinate, positionMap).hasMoved() && !hasPieceBetween(king.getCoordinate(), rookCoordinate, positionMap)) {
            for (int column = king.getCoordinate().get(1); column != rookCoordinate.get(1); column += kingColumnIncrement) {
                if (isDestinationUnderAttack(king.isWhite(), arrayListCoordinate(king.getCoordinate().get(0), column), positionMap)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private ArrayList<Integer> getRookCoordinate(Piece king, int rookColumnOffset) {
        return arrayListCoordinate(king.getCoordinate().get(0), king.getCoordinate().get(1) + rookColumnOffset);
    }

    private void castle(Piece king, ArrayList<Integer> destination) {
        if (Math.abs(king.getCoordinate().get(1) - destination.get(1)) == 2) {
            boolean isQueenSideCastle = king.getCoordinate().get(1) - destination.get(1) > 0;
            int rookColumnOffset = isQueenSideCastle ? -4 : 3;
            int kingColumnIncrement = isQueenSideCastle ? -1 : 1;

            ArrayList<Integer> rookCoordinate = arrayListCoordinate(king.getCoordinate().get(0), king.getCoordinate().get(1) + rookColumnOffset);
            ArrayList<Integer> rookDestination = arrayListCoordinate(king.getCoordinate().get(0), king.getCoordinate().get(1) + kingColumnIncrement);
            Piece rook = getPieceAt(rookCoordinate, this.positionMap);
            this.positionMap.remove(rookCoordinate);
            rook.setCoordinate(rookDestination);
            this.positionMap.put(rook.getCoordinate(), rook);
        }
    }

    public Map<ArrayList<Integer>, Piece> getPositionMap() {
        return this.positionMap;
    }

    public Piece getPieceAt(ArrayList<Integer> coordinate, Map<ArrayList<Integer>, Piece> positionMap) {
        return positionMap.get(coordinate);
    }

    public boolean isWhiteTurn() {
        return this.isWhiteTurn;
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
