package net.chesstango.board.moves.generators.legal;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.internal.moves.MoveComposed;
import net.chesstango.board.position.Command;

/**
 * Interface representing a filter for determining the legality of various chess moves.
 * This interface provides methods to check the legality of different types of moves
 * such as pawn moves, knight moves, bishop moves, rook moves, queen moves, king moves,
 * and castling moves.
 * <p>
 * Each method takes a move and a command as parameters and returns a boolean indicating
 * whether the move is legal according to the rules of chess.
 *
 * @see Move
 * @see MoveCastling
 * @see Command
 * @see MoveComposed
 *
 * @author Mauricio Coria
 */
public interface LegalMoveFilter {

    /**
     * Checks if the pawn move is legal based on the provided command.
     *
     * @param move the pawn move to be checked
     * @param command the command used to determine if the move is legal
     * @return true if the pawn move is legal, false otherwise
     */
    boolean isLegalMovePawn(Move move, Command command);

    /**
     * Checks if the knight move is legal based on the provided command.
     *
     * @param move the knight move to be checked
     * @param command the command used to determine if the move is legal
     * @return true if the knight move is legal, false otherwise
     */
    boolean isLegalMoveKnight(Move move, Command command);

    /**
     * Checks if the bishop move is legal based on the provided command.
     *
     * @param move the bishop move to be checked
     * @param command the command used to determine if the move is legal
     * @return true if the bishop move is legal, false otherwise
     */
    boolean isLegalMoveBishop(Move move, Command command);

    /**
     * Checks if the rook move is legal based on the provided command.
     *
     * @param move the rook move to be checked
     * @param command the command used to determine if the move is legal
     * @return true if the rook move is legal, false otherwise
     */
    boolean isLegalMoveRook(Move move, Command command);

    /**
     * Checks if the queen move is legal based on the provided command.
     *
     * @param move the queen move to be checked
     * @param command the command used to determine if the move is legal
     * @return true if the queen move is legal, false otherwise
     */
    boolean isLegalMoveQueen(Move move, Command command);

    /**
     * Checks if the king move is legal based on the provided command.
     *
     * @param move the king move to be checked
     * @param command the command used to determine if the move is legal
     * @return true if the king move is legal, false otherwise
     */
    boolean isLegalMoveKing(Move move, Command command);

    /**
     * Checks if the castling move is legal based on the provided command.
     *
     * @param moveCastling the castling move to be checked
     * @param command the command used to determine if the move is legal
     * @return true if the castling move is legal, false otherwise
     */
    boolean isLegalMoveCastling(MoveCastling moveCastling, Command command);

}