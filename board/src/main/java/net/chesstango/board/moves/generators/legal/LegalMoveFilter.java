package net.chesstango.board.moves.generators.legal;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.moves.imp.MoveComposed;
import net.chesstango.board.position.Command;

/**
 * @author Mauricio Coria
 */
public interface LegalMoveFilter {

    boolean isLegalMovePawn(Move move, Command command);

    boolean isLegalMoveKnight(Move move, Command command);

    boolean isLegalMoveBishop(Move move, Command command);

    boolean isLegalMoveRook(Move move, Command command);

    boolean isLegalMoveQueen(Move move, Command command);

    boolean isLegalMoveKing(Move move, Command command);

    boolean isLegalMoveCastling(MoveCastling moveCastling, Command command);

}
