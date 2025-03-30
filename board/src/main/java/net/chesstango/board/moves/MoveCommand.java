package net.chesstango.board.moves;

import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.position.Command;

/**
 * @author Mauricio Coria
 */
public interface MoveCommand extends Move, Command {

    boolean isLegalMove(LegalMoveFilter filter);
}
