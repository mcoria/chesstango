package net.chesstango.board.moves.imp;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.position.MoveCommand;

/**
 * @author Mauricio Coria
 */
public interface IMoveCommand extends Move, MoveCommand {

    boolean isLegalMove(LegalMoveFilter filter);
}
