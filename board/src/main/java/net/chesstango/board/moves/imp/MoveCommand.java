package net.chesstango.board.moves.imp;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.position.ChessPositionVisitor;

/**
 * @author Mauricio Coria
 */
public interface MoveCommand extends Move, ChessPositionVisitor {

    boolean isLegalMove(LegalMoveFilter filter);
}
