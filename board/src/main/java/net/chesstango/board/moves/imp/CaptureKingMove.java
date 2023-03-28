package net.chesstango.board.moves.imp;

import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.moves.MoveKing;
import net.chesstango.board.position.PiecePlacementReader;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.imp.PositionState;
import net.chesstango.board.position.imp.ZobristHash;

/**
 * @author Mauricio Coria
 */
class CaptureKingMove extends CaptureMove implements MoveKing {

    public CaptureKingMove(PiecePositioned from, PiecePositioned to) {
        super(from, to);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) && obj instanceof CaptureKingMove;
    }

    @Override
    protected void updatePositionStateBeforeRollTurn(PositionState positionState) {
        super.updatePositionStateBeforeRollTurn(positionState);
        if(Color.WHITE.equals(positionState.getCurrentTurn())){
            positionState.setCastlingWhiteKingAllowed(false);
            positionState.setCastlingWhiteQueenAllowed(false);
        } else {
            positionState.setCastlingBlackKingAllowed(false);
            positionState.setCastlingBlackQueenAllowed(false);
        }
    }
    @Override
    public void executeMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState, PiecePlacementReader board) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
