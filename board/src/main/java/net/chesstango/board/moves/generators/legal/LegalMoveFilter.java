package net.chesstango.board.moves.generators.legal;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.position.Command;

/**
 * @author Mauricio Coria
 */
public interface LegalMoveFilter {
    boolean isLegalMove(Move move, Command command);

    boolean isLegalMoveKing(Move move, Command command);

    boolean isLegalMoveCastling(MoveCastling moveCastling, Command command);
}
