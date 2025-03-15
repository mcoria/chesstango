package net.chesstango.board.moves.factories.imp;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.moves.imp.MoveCastlingBlackKing;
import net.chesstango.board.moves.imp.MoveCastlingBlackQueen;
import net.chesstango.board.moves.imp.MoveCastlingWhiteKing;
import net.chesstango.board.moves.imp.MoveCastlingWhiteQueen;
import net.chesstango.board.position.PositionStateWriter;

/**
 * @author Mauricio Coria
 */
public class AlgoPositionStateBlack implements AlgoPositionState {

    @Override
    public void doSimplePawnMove(PiecePositioned from, PiecePositioned to, PositionStateWriter positionStateWriter) {
        positionStateWriter.pushState();
        positionStateWriter.resetHalfMoveClock();
        positionStateWriter.setEnPassantSquare(null);
        positionStateWriter.incrementFullMoveClock();
        positionStateWriter.rollTurn();
    }

    @Override
    public void doSimpleNotPawnNorKingMove(PiecePositioned from, PiecePositioned to, PositionStateWriter positionStateWriter) {
        positionStateWriter.pushState();
        positionStateWriter.incrementHalfMoveClock();
        positionStateWriter.setEnPassantSquare(null);

        if(MoveCastlingBlackKing.ROOK_FROM.equals(from)){
            positionStateWriter.setCastlingBlackKingAllowed(false);
        }

        if(MoveCastlingBlackQueen.ROOK_FROM.equals(from)){
            positionStateWriter.setCastlingBlackQueenAllowed(false);
        }

        positionStateWriter.incrementFullMoveClock();
        positionStateWriter.rollTurn();
    }

    @Override
    public void doSimpleKingPositionState(PiecePositioned from, PiecePositioned to, PositionStateWriter positionStateWriter) {
        positionStateWriter.pushState();
        positionStateWriter.incrementHalfMoveClock();
        positionStateWriter.setEnPassantSquare(null);
        positionStateWriter.setCastlingBlackKingAllowed(false);
        positionStateWriter.setCastlingBlackQueenAllowed(false);
        positionStateWriter.incrementFullMoveClock();
        positionStateWriter.rollTurn();
    }

    @Override
    public void doCaptureNotKingPositionState(PiecePositioned from, PiecePositioned to, PositionStateWriter positionStateWriter) {
        positionStateWriter.pushState();
        positionStateWriter.resetHalfMoveClock();
        positionStateWriter.setEnPassantSquare(null);

        if(MoveCastlingBlackKing.ROOK_FROM.equals(from)){
            positionStateWriter.setCastlingBlackKingAllowed(false);
        }

        if(MoveCastlingBlackQueen.ROOK_FROM.equals(from)){
            positionStateWriter.setCastlingBlackQueenAllowed(false);
        }

        if(MoveCastlingWhiteKing.ROOK_FROM.equals(to)){
            positionStateWriter.setCastlingWhiteKingAllowed(false);
        }

        if(MoveCastlingWhiteQueen.ROOK_FROM.equals(to)){
            positionStateWriter.setCastlingWhiteQueenAllowed(false);
        }

        positionStateWriter.incrementFullMoveClock();
        positionStateWriter.rollTurn();
    }

    @Override
    public void doCaptureKingPositionState(PiecePositioned from, PiecePositioned to, PositionStateWriter positionStateWriter) {
        positionStateWriter.pushState();
        positionStateWriter.resetHalfMoveClock();
        positionStateWriter.setEnPassantSquare(null);

        positionStateWriter.setCastlingBlackKingAllowed(false);
        positionStateWriter.setCastlingBlackQueenAllowed(false);

        if(MoveCastlingWhiteKing.ROOK_FROM.equals(to)){
            positionStateWriter.setCastlingWhiteKingAllowed(false);
        }

        if(MoveCastlingWhiteQueen.ROOK_FROM.equals(to)){
            positionStateWriter.setCastlingWhiteQueenAllowed(false);
        }

        positionStateWriter.incrementFullMoveClock();
        positionStateWriter.rollTurn();
    }
}
