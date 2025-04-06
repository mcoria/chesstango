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
public class AlgoPositionStateBlack implements AlgoPositionState {

    @Override
    public void doSimplePawnMove(PiecePositioned from, PiecePositioned to, StateWriter stateWriter) {
        stateWriter.pushState();
        stateWriter.resetHalfMoveClock();
        stateWriter.setEnPassantSquare(null);
        stateWriter.incrementFullMoveClock();
        stateWriter.rollTurn();
    }

    @Override
    public void doSimpleNotPawnNorKingMove(PiecePositioned from, PiecePositioned to, StateWriter stateWriter) {
        stateWriter.pushState();
        stateWriter.incrementHalfMoveClock();
        stateWriter.setEnPassantSquare(null);

        if(MoveCastlingBlackKing.ROOK_FROM.equals(from)){
            stateWriter.setCastlingBlackKingAllowed(false);
        }

        if(MoveCastlingBlackQueen.ROOK_FROM.equals(from)){
            stateWriter.setCastlingBlackQueenAllowed(false);
        }

        stateWriter.incrementFullMoveClock();
        stateWriter.rollTurn();
    }

    @Override
    public void doSimpleKingPositionState(PiecePositioned from, PiecePositioned to, StateWriter stateWriter) {
        stateWriter.pushState();
        stateWriter.incrementHalfMoveClock();
        stateWriter.setEnPassantSquare(null);
        stateWriter.setCastlingBlackKingAllowed(false);
        stateWriter.setCastlingBlackQueenAllowed(false);
        stateWriter.incrementFullMoveClock();
        stateWriter.rollTurn();
    }

    @Override
    public void doCaptureNotKingPositionState(PiecePositioned from, PiecePositioned to, StateWriter stateWriter) {
        stateWriter.pushState();
        stateWriter.resetHalfMoveClock();
        stateWriter.setEnPassantSquare(null);

        if(MoveCastlingBlackKing.ROOK_FROM.equals(from)){
            stateWriter.setCastlingBlackKingAllowed(false);
        }

        if(MoveCastlingBlackQueen.ROOK_FROM.equals(from)){
            stateWriter.setCastlingBlackQueenAllowed(false);
        }

        if(MoveCastlingWhiteKing.ROOK_FROM.equals(to)){
            stateWriter.setCastlingWhiteKingAllowed(false);
        }

        if(MoveCastlingWhiteQueen.ROOK_FROM.equals(to)){
            stateWriter.setCastlingWhiteQueenAllowed(false);
        }

        stateWriter.incrementFullMoveClock();
        stateWriter.rollTurn();
    }

    @Override
    public void doCaptureKingPositionState(PiecePositioned from, PiecePositioned to, StateWriter stateWriter) {
        stateWriter.pushState();
        stateWriter.resetHalfMoveClock();
        stateWriter.setEnPassantSquare(null);

        stateWriter.setCastlingBlackKingAllowed(false);
        stateWriter.setCastlingBlackQueenAllowed(false);

        if(MoveCastlingWhiteKing.ROOK_FROM.equals(to)){
            stateWriter.setCastlingWhiteKingAllowed(false);
        }

        if(MoveCastlingWhiteQueen.ROOK_FROM.equals(to)){
            stateWriter.setCastlingWhiteQueenAllowed(false);
        }

        stateWriter.incrementFullMoveClock();
        stateWriter.rollTurn();
    }
}
