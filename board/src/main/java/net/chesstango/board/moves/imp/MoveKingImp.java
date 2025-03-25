package net.chesstango.board.moves.imp;

import net.chesstango.board.GameImp;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 */
public class MoveKingImp extends MoveComposed {

    public MoveKingImp(GameImp gameImp, PiecePositioned from, PiecePositioned to, Cardinal direction) {
        super(gameImp, from, to, direction);
    }

    public MoveKingImp(GameImp gameImp, PiecePositioned from, PiecePositioned to) {
        super(gameImp, from, to);
    }

    @Override
    public void doMove(ChessPositionWriter chessPosition) {
        SquareBoardWriter squareBoard = chessPosition.getSquareBoard();
        BitBoardWriter bitBoard = chessPosition.getBitBoard();
        PositionStateWriter positionState = chessPosition.getPositionState();
        MoveCacheBoardWriter moveCache = chessPosition.getMoveCache();
        KingSquare kingSquare = chessPosition.getKingSquare();
        ZobristHashWriter hash = chessPosition.getZobrist();

        doMove(squareBoard);

        doMove(bitBoard);

        doMove(positionState);

        doMove(moveCache);

        doMove(kingSquare);

        doMove(hash);
    }

    @Override
    public void undoMove(ChessPositionWriter chessPosition) {
        SquareBoardWriter squareBoard = chessPosition.getSquareBoard();
        BitBoardWriter bitBoard = chessPosition.getBitBoard();
        PositionStateWriter positionState = chessPosition.getPositionState();
        MoveCacheBoardWriter moveCache = chessPosition.getMoveCache();
        KingSquare kingSquare = chessPosition.getKingSquare();
        ZobristHashWriter hash = chessPosition.getZobrist();

        undoMove(squareBoard);

        undoMove(bitBoard);

        undoMove(positionState);

        undoMove(moveCache);

        undoMove(kingSquare);

        undoMove(hash);
    }


    @Override
    public void doMove(KingSquareWriter kingSquareWriter) {
        kingSquareWriter.setKingSquare(getFrom().getPiece().getColor(), getTo().getSquare());
    }

    @Override
    public void undoMove(KingSquareWriter kingSquareWriter) {
        kingSquareWriter.setKingSquare(getFrom().getPiece().getColor(), getFrom().getSquare());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MoveKingImp theOther) {
            return from.equals(theOther.from) && to.equals(theOther.to);
        }
        return false;
    }

    @Override
    public boolean isLegalMove(LegalMoveFilter filter) {
        return filter.isLegalMoveKing(this);
    }
}
