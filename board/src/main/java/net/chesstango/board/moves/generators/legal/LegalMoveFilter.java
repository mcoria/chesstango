package net.chesstango.board.moves.generators.legal;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.moves.MoveCommand;

/**
 * @author Mauricio Coria
 */
public interface LegalMoveFilter {
    boolean isLegalMove(Move move, MoveCommand command);

    boolean isLegalMoveKing(Move move, MoveCommand command);

    boolean isLegalMoveCastling(MoveCastling moveCastling, MoveCommand command);
}
