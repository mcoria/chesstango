package net.chesstango.board.moves;

import net.chesstango.board.moves.generators.legal.LegalMoveFilter;

/**
 * @author Mauricio Coria
 */
public interface MoveCommand extends Move {

    boolean isLegalMove(LegalMoveFilter filter);
}
