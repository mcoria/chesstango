package net.chesstango.board;

import net.chesstango.board.builders.GameBuilder;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.PseudoMove;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.position.GameHistoryReader;
import net.chesstango.board.position.GameStateReader;
import net.chesstango.board.position.PositionReader;
import net.chesstango.board.representations.move.SimpleMoveDecoder;
import net.chesstango.board.representations.pgn.GameToPGN;
import net.chesstango.board.representations.pgn.PGNToGame;
import net.chesstango.gardel.epd.EPD;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.pgn.PGN;

import java.util.List;

/**
 * Interface representing a chess game.
 * This interface provides methods to get the initial and current FEN (Forsyth-Edwards Notation),
 * the game state, the chess position, and the game status. It also includes methods to handle
 * the fifty-move rule, the threefold repetition rule, possible moves and game listeners.
 *
 * @author Mauricio Coria
 * @see FEN
 * @see GameStateReader
 * @see PositionReader
 * @see Status
 * @see MoveContainerReader
 * @see Move
 * @see Square
 * @see Piece
 * @see GameListener
 */
public interface Game {
    /**
     * Gets the initial FEN of the game.
     *
     * @return the initial FEN
     */
    FEN getInitialFEN();

    /**
     * Gets the current FEN of the game.
     *
     * @return the current FEN
     */
    FEN getCurrentFEN();

    /**
     * Gets the current state of the game.
     *
     * @return the game state
     */
    GameStateReader getState();


    /**
     * Gets the game history.
     *
     * @return the game history
     */
    GameHistoryReader getHistory();

    /**
     * Gets the current chess position.
     *
     * @return the chess position
     */
    PositionReader getPosition();

    /**
     * Gets the current status of the game.
     *
     * @return the game status
     */
    Status getStatus();

    /**
     * Sets the fifty-move rule flag.
     *
     * @param flag the fifty-move rule flag
     */
    void fiftyMovesRule(boolean flag);

    /**
     * Sets the threefold repetition rule flag.
     *
     * @param flag the threefold repetition rule flag
     */
    void threefoldRepetitionRule(boolean flag);

    /**
     * Gets the possible legal moves in the current position.
     *
     * @return a container of possible moves
     */
    MoveContainerReader<Move> getPossibleMoves();

    /**
     * Gets the possible pseudo moves in the current position.
     *
     * @return a container of possible moves
     */
    MoveContainerReader<PseudoMove> getPseudoMoves();

    /**
     * Adds a game listener.
     *
     * @param gameListener the game listener to add
     */
    void addGameListener(GameListener gameListener);

    /**
     * Gets a move from the specified starting and ending squares.
     *
     * @param from the starting square
     * @param to   the ending square
     * @return the move
     */
    Move getMove(Square from, Square to);

    /**
     * Gets a move from the specified starting and ending squares with a promotion piece.
     *
     * @param from           the starting square
     * @param to             the ending square
     * @param promotionPiece the promotion piece
     * @return the move
     */
    Move getMove(Square from, Square to, Piece promotionPiece);

    /**
     * Executes a move from the specified starting and ending squares.
     *
     * @param from the starting square
     * @param to   the ending square
     * @return the game after the move is executed
     */
    Game executeMove(Square from, Square to);

    /**
     * Executes a move from the specified starting and ending squares with a promotion piece.
     *
     * @param from           the starting square
     * @param to             the ending square
     * @param promotionPiece the promotion piece
     * @return the game after the move is executed
     */
    Game executeMove(Square from, Square to, Piece promotionPiece);

    /**
     * Undoes the last move.
     *
     * @return the game after the move is undone
     */
    Game undoMove();


    /**
     * Mirrors the current game position.
     *
     * @return the mirrored game
     */
    Game mirror();


    /**
     * Encodes the current game state into a PGN (Portable Game Notation) representation.
     *
     * @return a {@code PGN} object representing the current state and history of the game
     */
    default PGN encode() {
        return new GameToPGN().decode(this);
    }

    /**
     * Creates a new Game instance based on a given FEN (Forsyth–Edwards Notation) representation.
     *
     * @param fen the FEN object representing the state of a chess game
     * @return a new Game instance initialized using the specified FEN
     */
    static Game from(FEN fen) {
        GameBuilder builder = new GameBuilder();

        fen.export(builder);

        return builder.getPositionRepresentation();
    }


    /**
     * Creates a new Game instance based on a given FEN (Forsyth–Edwards Notation) and a list of moves.
     * The method initializes the game with the provided FEN position and then executes each move in the list sequentially.
     *
     * @param fen   the FEN object representing the initial state of the chess game
     * @param moves a list of moves in string format to be executed from the initial position
     * @return a new Game instance initialized using the specified FEN and with all moves executed
     * @throws RuntimeException if any of the moves in the list is invalid or cannot be executed
     */
    static Game from(FEN fen, List<String> moves) {
        SimpleMoveDecoder simpleMoveDecoder = new SimpleMoveDecoder();
        Game game = from(fen);
        for (String moveStr : moves) {
            Move move = simpleMoveDecoder.decode(game.getPossibleMoves(), moveStr);
            if (move == null) {
                throw new RuntimeException(String.format("No move found %s", moveStr));
            }
            move.executeMove();
        }
        return game;
    }


    /**
     * Creates a new Game instance based on a given PGN (Portable Game Notation).
     *
     * @param pgn the PGN object containing the chess game information
     * @return a new Game instance initialized from the PGN data
     */
    static Game from(PGN pgn) {
        return new PGNToGame().encode(pgn);
    }

    /**
     * Creates a new Game instance based on a given EPD (Extended Position Description).
     *
     * @param epd the EPD object containing information about the chess position
     * @return a new Game instance initialized using the FEN derived from the EPD
     */
    static Game from(EPD epd) {
        return from(FEN.of(epd.getFenWithoutClocks() + " 0 1"));
    }
}
