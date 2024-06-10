package net.chesstango.board.moves.generators.legal;

/**
 * @author Mauricio Coria
 *
 */
public interface LegalMoveFilterSelector {
    boolean isLegalMove(LegalMoveFilter filter);
}
