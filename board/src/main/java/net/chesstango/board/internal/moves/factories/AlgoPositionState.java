package net.chesstango.board.internal.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.StateWriter;

/**
 * @author Mauricio Coria
 */
public interface AlgoPositionState {
    void doSimplePawnMove(PiecePositioned from, PiecePositioned to, StateWriter stateWriter);

    void doSimpleNotPawnNorKingMove(PiecePositioned from, PiecePositioned to, StateWriter stateWriter);

    void doSimpleKingPositionState(PiecePositioned from, PiecePositioned to, StateWriter stateWriter);

    void doCaptureKingPositionState(PiecePositioned from, PiecePositioned to, StateWriter stateWriter);

    void doCaptureNotKingPositionState(PiecePositioned from, PiecePositioned to, StateWriter stateWriter);
}
