package net.chesstango.board;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.PseudoMove;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.position.GameStateReader;
import net.chesstango.board.representations.fen.FEN;

import java.util.Iterator;

/**
 * Interface representing a chess game.
 * This interface provides methods to get the initial and current FEN (Forsyth-Edwards Notation),
 * the game state, the chess position, and the game status. It also includes methods to handle
 * the fifty-move rule, the threefold repetition rule, possible moves and game listeners.
 *
 * @author Mauricio Coria
 * @see FEN
 * @see GameStateReader
 * @see ChessPositionReader
 * @see GameStatus
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
     * Gets the previous state of the game.
     *
     * @return the game state
     */
    GameStateReader getPreviousState();

    /**
     * Gets an iterator over the game states.
     *
     * @return an iterator over the game states
     */
    Iterator<GameStateReader> stateIterator();

    /**
     * Gets an iterator over the game states in reverse order.
     *
     * @return an iterator over the game states
     */
    Iterator<GameStateReader> stateIteratorReverse();

    /**
     * Gets the current chess position.
     *
     * @return the chess position
     */
    ChessPositionReader getChessPosition();

    /**
     * Gets the current status of the game.
     *
     * @return the game status
     */
    GameStatus getStatus();

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
}
