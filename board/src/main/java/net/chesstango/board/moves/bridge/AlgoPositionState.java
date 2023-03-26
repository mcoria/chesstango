package net.chesstango.board.moves.bridge;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.imp.PositionState;

/**
 * @author Mauricio Coria
 */
interface AlgoPositionState {
    void doSimplePawnMove(PiecePositioned from, PiecePositioned to, PositionState positionState);

    void doSimpleNotPawnNorKingMove(PiecePositioned from, PiecePositioned to, PositionState positionState);

    void doSimpleKingPositionState(PiecePositioned from, PiecePositioned to, PositionState positionState);

    void doCaptureKingPositionState(PiecePositioned from, PiecePositioned to, PositionState positionState);

    void doCaptureNotKingPositionState(PiecePositioned from, PiecePositioned to, PositionState positionState);
}
