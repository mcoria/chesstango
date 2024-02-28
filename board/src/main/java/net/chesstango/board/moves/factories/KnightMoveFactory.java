package net.chesstango.board.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 */
public interface KnightMoveFactory {
    Move createSimpleKnightMove(PiecePositioned origen, PiecePositioned destino);

    Move createCaptureKnightMove(PiecePositioned origen, PiecePositioned destino);
}
