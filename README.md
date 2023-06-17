# Board package:
## Board.java:
Responsible for the logic and representation of the chessboard.
Uses the Java Swing library to create a graphical window.
Contains methods to paint the board, load piece images, draw the current position of the pieces, highlight valid moves, and handle mouse click events.
Manages user interaction with the board, such as selecting and moving pieces.
Provides a graphical interface for playing the game.
## GameManager.java:
Responsible for managing the chess game as a whole.
Creates a graphical window using the Swing library to display game start options.
Allows the player to start a new game or load a chess position using FEN notation.
Validates the FEN notation input to ensure its correctness.
Controls the transition between the options window and the board initialization.
## Position.java:
This class represents a position on a chessboard and has various functionalities related to piece movement.

The "Position" class has the following attributes:
```
positionMap: A map that maps coordinates (represented by a list of integers) to piece objects.
enPassantMap: A map that maps move number to the en passant coordinate.
isWhiteTurn: A boolean indicating whether it is white's turn.
move: The number of moves that have been made.
halfmove: The number of half-moves since the last capture or pawn move.
whiteKingCoordinate: The coordinates of the white king.
blackKingCoordinate: The coordinates of the black king.
```
The constructor of the "Position" class takes a FEN (Forsyth–Edwards Notation) string as a parameter and initializes the class attributes based on the information contained in the FEN. FEN represents the current configuration of the chessboard.

The "Position" class also has methods such as:
```
newPiece(): Helper method used by the constructor to create new instances of chess pieces based on the characters in the FEN string.
movePiece(): Moves a piece to the destination coordinate, updates the game, and checks if the game is over.
isValidMove(): Checks if a move is valid for a given piece and destination.
pawnMove(): Performs the movement of a pawn, taking into account special moves like en passant and promotion.
kingMove(): Performs the movement of the king, considering castling moves.
updateGame(): Updates the game state after a move, checking for capture, pawn move, or game over.
gameOver(): Checks if the game is over (checkmate or draw).
positionValidMoves(): Returns a list of valid moves for a given piece.
moveIsUnavailable(): Checks if a move is invalid for a piece, considering movement restrictions, allied pieces, etc.
And other helper methods.
```
Overall, the code implements the basic logic for piece movement in a game of chess, checking the validity of moves, updating the game state, and checking if the game is over.


# Pieces package:
The "Pieces" package contains several classes that represent different chess pieces. Each class represents a specific piece, and these classes extend the abstract class "Piece".
The abstract class "Piece" defines attributes and methods common to all pieces. It has the following attributes:
```
"coordinate": a list of coordinates representing the current position of the piece on the board.
"isWhite": a boolean indicating whether the piece is white or black.
"type": an enum representing the type of the piece (e.g., King, Queen, Bishop, etc.).
"moved": a boolean indicating whether the piece has already moved.
```
The abstract class "Piece" has methods such as:
```
"getImageIndex()": returns the index of the image associated with the piece.
"setCoordinate(ArrayList<Integer> coordinate)": sets the coordinates of the piece.
"getCoordinate()": returns the current coordinates of the piece.
"isWhite()": checks if the piece is white.
"hasMoved()": checks if the piece has already moved.
"validMoves()": an abstract method that returns a list of valid moves for the piece.
```
Each specific piece class, such as the "King" class, extends the abstract class "Piece" and implements its own "validMoves()" method. These methods calculate the specific valid moves for each type of piece, taking into account the current position and other specific rules.
Thus, the "Pieces" package provides a structure for representing and managing the chess pieces, with an abstract class that defines the general behavior of the pieces and specific classes for each type of piece that implement their own movement rules.

# Enums package:
The class "PieceType" is an enumeration (enum) that represents the types of chess pieces. It defines the different types of pieces using uppercase and lowercase letters, where uppercase letters represent the white pieces and lowercase letters represent the black pieces.
Each piece type is assigned an image index that corresponds to the index of the associated image in a collection or array of images. The image index is used to retrieve the correct image for each piece when displaying the chessboard or pieces in a graphical user interface.
The enum constructor takes an integer parameter, "imageIndex," and assigns it to the corresponding enum constant. This allows each piece type to have a unique image index associated with it.
The purpose of this class in the overall project is to provide a convenient and standardized way of representing and identifying different types of chess pieces. It simplifies the process of associating images with each piece type and allows for easy retrieval of the correct image index when needed.
