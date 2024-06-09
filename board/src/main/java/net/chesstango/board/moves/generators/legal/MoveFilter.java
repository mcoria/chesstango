package net.chesstango.board.moves.generators.legal;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveFilter {
    boolean isLegalMove(LegalMoveFilter filter);
}
