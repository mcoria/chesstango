package net.chesstango.board.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.moves.imp.MoveCastlingImp;
import net.chesstango.board.moves.imp.MoveCommand;
import net.chesstango.board.moves.imp.MoveKingImp;

/**
 * @author Mauricio Coria
 *
 */
public interface KingMoveFactory {
    MoveCommand createSimpleKingMove(PiecePositioned from, PiecePositioned to);

    MoveCommand createCaptureKingMove(PiecePositioned from, PiecePositioned to);

    MoveCommand createCastlingQueenMove();

    MoveCommand createCastlingKingMove();
}
