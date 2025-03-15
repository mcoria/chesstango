package net.chesstango.board.moves.factories.imp;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.PositionStateWriter;

/**
 * @author Mauricio Coria
 */
public interface AlgoPositionState {
    void doSimplePawnMove(PiecePositioned from, PiecePositioned to, PositionStateWriter positionStateWriter);

    void doSimpleNotPawnNorKingMove(PiecePositioned from, PiecePositioned to, PositionStateWriter positionStateWriter);

    void doSimpleKingPositionState(PiecePositioned from, PiecePositioned to, PositionStateWriter positionStateWriter);

    void doCaptureKingPositionState(PiecePositioned from, PiecePositioned to, PositionStateWriter positionStateWriter);

    void doCaptureNotKingPositionState(PiecePositioned from, PiecePositioned to, PositionStateWriter positionStateWriter);
}
