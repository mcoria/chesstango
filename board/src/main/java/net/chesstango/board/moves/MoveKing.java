package net.chesstango.board.moves;

import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.position.MoveKingCommand;

/**
 * @author Mauricio Coria
 */
public interface MoveKing extends Move, MoveKingCommand {

    @Override
    default boolean isLegalMove(LegalMoveFilter filter) {
        return filter.isLegalMoveKing(this);
    }
}
