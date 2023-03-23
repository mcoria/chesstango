package net.chesstango.board.moves.bridge;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.position.imp.PositionState;

/**
 * @author Mauricio Coria
 */
public class AlgoPositionStateWhite implements AlgoPositionState {

    @Override
    public void doSimplePawnMove(PiecePositioned from, PiecePositioned to, PositionState positionState) {
        positionState.pushState();
        positionState.resetHalfMoveClock();
        positionState.setEnPassantSquare(null);
        positionState.rollTurn();
    }

    @Override
    public void doSimpleNotPawnNorKingMove(PiecePositioned from, PiecePositioned to, PositionState positionState) {
        positionState.pushState();
        positionState.incrementHalfMoveClock();
        positionState.setEnPassantSquare(null);
        positionState.rollTurn();
    }

    @Override
    public void doSimpleKingPositionState(PiecePositioned from, PiecePositioned to, PositionState positionState) {
        positionState.pushState();
        positionState.incrementHalfMoveClock();
        positionState.setEnPassantSquare(null);
        positionState.setCastlingWhiteKingAllowed(false);
        positionState.setCastlingWhiteQueenAllowed(false);
        positionState.rollTurn();
    }

    @Override
    public void doCaptureNotKingPositionState(PiecePositioned from, PiecePositioned to, PositionState positionState) {
        positionState.pushState();
        positionState.resetHalfMoveClock();
        positionState.setEnPassantSquare(null);

        if(CastlingBlackKingMove.ROOK_FROM.equals(to)){
            positionState.setCastlingBlackKingAllowed(false);
        }

        if(CastlingBlackQueenMove.ROOK_FROM.equals(to)){
            positionState.setCastlingBlackQueenAllowed(false);
        }

        positionState.rollTurn();
    }

    @Override
    public void doCaptureKingPositionState(PiecePositioned from, PiecePositioned to, PositionState positionState) {
        positionState.pushState();
        positionState.resetHalfMoveClock();
        positionState.setEnPassantSquare(null);

        positionState.setCastlingWhiteKingAllowed(false);
        positionState.setCastlingWhiteQueenAllowed(false);

        if(CastlingBlackKingMove.ROOK_FROM.equals(to)){
            positionState.setCastlingBlackKingAllowed(false);
        }

        if(CastlingBlackQueenMove.ROOK_FROM.equals(to)){
            positionState.setCastlingBlackQueenAllowed(false);
        }

        positionState.rollTurn();
    }
}
