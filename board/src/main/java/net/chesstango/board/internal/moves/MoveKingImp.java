package net.chesstango.board.internal.moves;

import net.chesstango.board.internal.GameImp;
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
    public void doMove(PositionWriter chessPosition) {
        SquareBoardWriter squareBoard = chessPosition.getSquareBoardWriter();
        BitBoardWriter bitBoard = chessPosition.getBitBoardWriter();
        StateWriter positionState = chessPosition.getPositionStateWriter();
        MoveCacheBoardWriter moveCache = chessPosition.getMoveCacheWriter();
        KingSquareWriter kingSquare = chessPosition.getKingSquareWriter();
        ZobristHashWriter hash = chessPosition.getZobristWriter();

        doMove(squareBoard);

        doMove(bitBoard);

        doMove(positionState);

        doMove(moveCache);

        doMove(kingSquare);

        doMove(hash);
    }

    @Override
    public void undoMove(PositionWriter chessPosition) {
        SquareBoardWriter squareBoard = chessPosition.getSquareBoardWriter();
        BitBoardWriter bitBoard = chessPosition.getBitBoardWriter();
        StateWriter positionState = chessPosition.getPositionStateWriter();
        MoveCacheBoardWriter moveCache = chessPosition.getMoveCacheWriter();
        KingSquareWriter kingSquare = chessPosition.getKingSquareWriter();
        ZobristHashWriter hash = chessPosition.getZobristWriter();

        undoMove(squareBoard);

        undoMove(bitBoard);

        undoMove(positionState);

        undoMove(moveCache);

        undoMove(kingSquare);

        undoMove(hash);
    }


    @Override
    public void doMove(KingSquareWriter kingSquare) {
        kingSquare.setKingSquare(getFrom().getPiece().getColor(), getTo().getSquare());
    }

    @Override
    public void undoMove(KingSquareWriter kingSquare) {
        kingSquare.setKingSquare(getFrom().getPiece().getColor(), getFrom().getSquare());
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
        return filter.isLegalMoveKing(this, this);
    }
}
