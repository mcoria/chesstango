package net.chesstango.board.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.moves.MoveKing;

/**
 * @author Mauricio Coria
 *
 */
public interface KingMoveFactory {
    MoveKing createSimpleKingMove(PiecePositioned origen, PiecePositioned destino);

    MoveKing createCaptureKingMove(PiecePositioned origen, PiecePositioned destino);

    MoveCastling createCastlingQueenMove();

    MoveCastling createCastlingKingMove();
}
