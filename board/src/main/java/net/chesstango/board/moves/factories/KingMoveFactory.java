package net.chesstango.board.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.moves.PseudoMove;

/**
 * @author Mauricio Coria
 *
 */
public interface KingMoveFactory {
    PseudoMove createSimpleKingMove(PiecePositioned from, PiecePositioned to);

    PseudoMove createCaptureKingMove(PiecePositioned from, PiecePositioned to);

    PseudoMove createCastlingQueenMove();

    PseudoMove createCastlingKingMove();
}
