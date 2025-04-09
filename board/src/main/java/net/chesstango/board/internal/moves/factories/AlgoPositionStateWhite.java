package net.chesstango.board.internal.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.internal.moves.MoveCastlingBlackKing;
import net.chesstango.board.internal.moves.MoveCastlingBlackQueen;
import net.chesstango.board.internal.moves.MoveCastlingWhiteKing;
import net.chesstango.board.internal.moves.MoveCastlingWhiteQueen;
import net.chesstango.board.position.PositionStateWriter;

/**
 * @author Mauricio Coria
 */
public class AlgoPositionStateWhite implements AlgoPositionState {

    @Override
    public void doSimplePawnMove(PiecePositioned from, PiecePositioned to, PositionStateWriter positionStateWriter) {
        positionStateWriter.resetHalfMoveClock();
        positionStateWriter.setEnPassantSquare(null);
        positionStateWriter.rollTurn();
    }

    @Override
    public void doSimpleNotPawnNorKingMove(PiecePositioned from, PiecePositioned to, PositionStateWriter positionStateWriter) {
        positionStateWriter.incrementHalfMoveClock();
        positionStateWriter.setEnPassantSquare(null);

        if(MoveCastlingWhiteKing.ROOK_FROM.equals(from)){
            positionStateWriter.setCastlingWhiteKingAllowed(false);
        }

        if(MoveCastlingWhiteQueen.ROOK_FROM.equals(from)){
            positionStateWriter.setCastlingWhiteQueenAllowed(false);
        }

        positionStateWriter.rollTurn();
    }

    @Override
    public void doSimpleKingPositionState(PiecePositioned from, PiecePositioned to, PositionStateWriter positionStateWriter) {
        positionStateWriter.incrementHalfMoveClock();
        positionStateWriter.setEnPassantSquare(null);
        positionStateWriter.setCastlingWhiteKingAllowed(false);
        positionStateWriter.setCastlingWhiteQueenAllowed(false);
        positionStateWriter.rollTurn();
    }

    @Override
    public void doCaptureNotKingPositionState(PiecePositioned from, PiecePositioned to, PositionStateWriter positionStateWriter) {
        positionStateWriter.resetHalfMoveClock();
        positionStateWriter.setEnPassantSquare(null);

        if(MoveCastlingWhiteKing.ROOK_FROM.equals(from)){
            positionStateWriter.setCastlingWhiteKingAllowed(false);
        }

        if(MoveCastlingWhiteQueen.ROOK_FROM.equals(from)){
            positionStateWriter.setCastlingWhiteQueenAllowed(false);
        }

        if(MoveCastlingBlackKing.ROOK_FROM.equals(to)){
            positionStateWriter.setCastlingBlackKingAllowed(false);
        }

        if(MoveCastlingBlackQueen.ROOK_FROM.equals(to)){
            positionStateWriter.setCastlingBlackQueenAllowed(false);
        }

        positionStateWriter.rollTurn();
    }

    @Override
    public void doCaptureKingPositionState(PiecePositioned from, PiecePositioned to, PositionStateWriter positionStateWriter) {
        positionStateWriter.resetHalfMoveClock();
        positionStateWriter.setEnPassantSquare(null);

        positionStateWriter.setCastlingWhiteKingAllowed(false);
        positionStateWriter.setCastlingWhiteQueenAllowed(false);

        if(MoveCastlingBlackKing.ROOK_FROM.equals(to)){
            positionStateWriter.setCastlingBlackKingAllowed(false);
        }

        if(MoveCastlingBlackQueen.ROOK_FROM.equals(to)){
            positionStateWriter.setCastlingBlackQueenAllowed(false);
        }

        positionStateWriter.rollTurn();
    }
}
