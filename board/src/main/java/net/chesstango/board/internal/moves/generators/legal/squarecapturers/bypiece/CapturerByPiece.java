package net.chesstango.board.internal.moves.generators.legal.squarecapturers.bypiece;

import net.chesstango.board.Square;

/**
 * @author Mauricio Coria
 */
public interface CapturerByPiece {
    boolean positionCaptured(Square square, long possibleThreats);
}
