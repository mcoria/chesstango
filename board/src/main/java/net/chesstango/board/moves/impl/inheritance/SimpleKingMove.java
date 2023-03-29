package net.chesstango.board.moves.impl.inheritance;

import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.moves.MoveKing;
import net.chesstango.board.position.BoardReader;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.imp.PositionState;
import net.chesstango.board.position.imp.ZobristHash;

/**
 * @author Mauricio Coria
 */
class SimpleKingMove extends SimpleMove implements MoveKing {

    public SimpleKingMove(PiecePositioned from, PiecePositioned to) {
        super(from, to);
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
    public void executeMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState, BoardReader board) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void undoMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState, BoardReader board) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) && obj instanceof SimpleKingMove;
    }
}