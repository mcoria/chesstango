package net.chesstango.board.moves.impl.bridge;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.PositionStateWriter;

/**
 * @author Mauricio Coria
 */
interface AlgoPositionState {
    void doSimplePawnMove(PiecePositioned from, PiecePositioned to, PositionStateWriter positionStateWriter);

    void doSimpleNotPawnNorKingMove(PiecePositioned from, PiecePositioned to, PositionStateWriter positionState);

    void doSimpleKingPositionState(PiecePositioned from, PiecePositioned to, PositionStateWriter positionState);

    void doCaptureKingPositionState(PiecePositioned from, PiecePositioned to, PositionStateWriter positionStateWriter);

    void doCaptureNotKingPositionState(PiecePositioned from, PiecePositioned to, PositionStateWriter positionStateWriter);
}
