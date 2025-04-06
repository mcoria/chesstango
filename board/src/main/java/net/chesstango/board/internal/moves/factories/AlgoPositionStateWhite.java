package net.chesstango.board.internal.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.internal.moves.MoveCastlingBlackKing;
import net.chesstango.board.internal.moves.MoveCastlingBlackQueen;
import net.chesstango.board.internal.moves.MoveCastlingWhiteKing;
import net.chesstango.board.internal.moves.MoveCastlingWhiteQueen;
import net.chesstango.board.position.StateWriter;

/**
 * @author Mauricio Coria
 */
public class AlgoPositionStateWhite implements AlgoPositionState {

    @Override
    public void doSimplePawnMove(PiecePositioned from, PiecePositioned to, StateWriter stateWriter) {
        stateWriter.pushState();
        stateWriter.resetHalfMoveClock();
        stateWriter.setEnPassantSquare(null);
        stateWriter.rollTurn();
    }

    @Override
    public void doSimpleNotPawnNorKingMove(PiecePositioned from, PiecePositioned to, StateWriter stateWriter) {
        stateWriter.pushState();
        stateWriter.incrementHalfMoveClock();
        stateWriter.setEnPassantSquare(null);

        if(MoveCastlingWhiteKing.ROOK_FROM.equals(from)){
            stateWriter.setCastlingWhiteKingAllowed(false);
        }

        if(MoveCastlingWhiteQueen.ROOK_FROM.equals(from)){
            stateWriter.setCastlingWhiteQueenAllowed(false);
        }

        stateWriter.rollTurn();
    }

    @Override
    public void doSimpleKingPositionState(PiecePositioned from, PiecePositioned to, StateWriter stateWriter) {
        stateWriter.pushState();
        stateWriter.incrementHalfMoveClock();
        stateWriter.setEnPassantSquare(null);
        stateWriter.setCastlingWhiteKingAllowed(false);
        stateWriter.setCastlingWhiteQueenAllowed(false);
        stateWriter.rollTurn();
    }

    @Override
    public void doCaptureNotKingPositionState(PiecePositioned from, PiecePositioned to, StateWriter stateWriter) {
        stateWriter.pushState();
        stateWriter.resetHalfMoveClock();
        stateWriter.setEnPassantSquare(null);

        if(MoveCastlingWhiteKing.ROOK_FROM.equals(from)){
            stateWriter.setCastlingWhiteKingAllowed(false);
        }

        if(MoveCastlingWhiteQueen.ROOK_FROM.equals(from)){
            stateWriter.setCastlingWhiteQueenAllowed(false);
        }

        if(MoveCastlingBlackKing.ROOK_FROM.equals(to)){
            stateWriter.setCastlingBlackKingAllowed(false);
        }

        if(MoveCastlingBlackQueen.ROOK_FROM.equals(to)){
            stateWriter.setCastlingBlackQueenAllowed(false);
        }

        stateWriter.rollTurn();
    }

    @Override
    public void doCaptureKingPositionState(PiecePositioned from, PiecePositioned to, StateWriter stateWriter) {
        stateWriter.pushState();
        stateWriter.resetHalfMoveClock();
        stateWriter.setEnPassantSquare(null);

        stateWriter.setCastlingWhiteKingAllowed(false);
        stateWriter.setCastlingWhiteQueenAllowed(false);

        if(MoveCastlingBlackKing.ROOK_FROM.equals(to)){
            stateWriter.setCastlingBlackKingAllowed(false);
        }

        if(MoveCastlingBlackQueen.ROOK_FROM.equals(to)){
            stateWriter.setCastlingBlackQueenAllowed(false);
        }

        stateWriter.rollTurn();
    }
}
