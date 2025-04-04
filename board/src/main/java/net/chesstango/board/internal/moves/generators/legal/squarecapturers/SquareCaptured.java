package net.chesstango.board.internal.moves.generators.legal.squarecapturers;

import net.chesstango.board.Color;
import net.chesstango.board.Square;


/**
 * @author Mauricio Coria
 *
 */
public interface SquareCaptured {
    boolean isCaptured(Color color, Square square);
}
