package net.chesstango.board.moves.bridge;

import net.chesstango.board.Square;
import net.chesstango.board.position.imp.PositionState;

/**
 * @author Mauricio Coria
 */
public class AlgoPositionState {

    public void kingWhiteUpdatePositionStateBeforeRollTurn(PositionState positionState) {
        positionState.incrementHalfMoveClock();
        positionState.setEnPassantSquare(null);
        positionState.setCastlingWhiteKingAllowed(false);
        positionState.setCastlingWhiteQueenAllowed(false);
    }
    public void kingBlackUpdatePositionStateBeforeRollTurn(PositionState positionState) {
        positionState.incrementHalfMoveClock();
        positionState.setEnPassantSquare(null);
        positionState.setCastlingBlackKingAllowed(false);
        positionState.setCastlingBlackQueenAllowed(false);
    }

    public void kingWhiteCaptureUpdatePositionStateBeforeRollTurn(PositionState positionState) {
        positionState.resetHalfMoveClock();
        positionState.setEnPassantSquare(null);
        positionState.setCastlingWhiteKingAllowed(false);
        positionState.setCastlingWhiteQueenAllowed(false);
    }

    public void kingBlackCaptureUpdatePositionStateBeforeRollTurn(PositionState positionState) {
        positionState.resetHalfMoveClock();
        positionState.setEnPassantSquare(null);
        positionState.setCastlingBlackKingAllowed(false);
        positionState.setCastlingBlackQueenAllowed(false);
    }

    public void twoSquaresPawnMove(PositionState positionState, Square enPassantSquare) {
        positionState.resetHalfMoveClock();
        positionState.setEnPassantSquare(enPassantSquare);
    }
}
