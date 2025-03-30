package net.chesstango.board.moves.imp;

import net.chesstango.board.GameImp;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 */
public abstract class MoveCastlingImp extends MoveKingImp implements MoveCastling {

    protected final PiecePositioned rookFrom;
    protected final PiecePositioned rookTo;

    public MoveCastlingImp(GameImp gameImp, PiecePositioned kingFrom, PiecePositioned kingTo, PiecePositioned rookFrom, PiecePositioned rookTo) {
        super(gameImp, kingFrom, kingTo);

        this.rookFrom = rookFrom;
        this.rookTo = rookTo;
    }

    @Override
    public void doMove(SquareBoardWriter squareBoard) {
        squareBoard.move(from, to);
        squareBoard.move(rookFrom, rookTo);
    }


    @Override
    public void undoMove(SquareBoardWriter squareBoard) {
        squareBoard.setPosition(from);
        squareBoard.setPosition(to);

        squareBoard.setPosition(rookFrom);
        squareBoard.setPosition(rookTo);
    }

    @Override
    public void doMove(BitBoardWriter bitBoardWriter) {
        bitBoardWriter.swapPositions(from.getPiece(), from.getSquare(), to.getSquare());
        bitBoardWriter.swapPositions(rookFrom.getPiece(), rookFrom.getSquare(), rookTo.getSquare());
    }

    @Override
    public void undoMove(BitBoardWriter bitBoardWriter) {
        bitBoardWriter.swapPositions(from.getPiece(), to.getSquare(), from.getSquare());
        bitBoardWriter.swapPositions(rookFrom.getPiece(), rookTo.getSquare(), rookFrom.getSquare());
    }

    @Override
    public void doMove(MoveCacheBoardWriter moveCache) {
        moveCache.affectedPositionsByMove(from.getSquare(), to.getSquare(), rookFrom.getSquare(), rookTo.getSquare());
        moveCache.push();
    }

    @Override
    public void undoMove(MoveCacheBoardWriter moveCache) {
        moveCache.affectedPositionsByMove(from.getSquare(), to.getSquare(), rookFrom.getSquare(), rookTo.getSquare());
        moveCache.pop();
    }

    @Override
    public void doMove(ZobristHashWriter hash) {
        hash.pushState();

        hash.xorPosition(from);
        hash.xorPosition(PiecePositioned.getPiecePositioned(to.getSquare(), from.getPiece()));

        hash.xorPosition(rookFrom);
        hash.xorPosition(PiecePositioned.getPiecePositioned(rookTo.getSquare(), rookFrom.getPiece()));

        ChessPositionReader chessPositionReader = gameImp.getChessPosition();

        xorCastling(hash, chessPositionReader.getPreviousPositionState(), chessPositionReader);

        hash.clearEnPassantSquare();

        hash.xorTurn();
    }

    @Override
    public boolean isLegalMove(LegalMoveFilter filter) {
        return filter.isLegalMoveCastling(this, this);
    }

    @Override
    public String toString() {
        return getFrom().toString() + " " + getTo().toString() + " - " + this.getClass().getSimpleName();
    }

    @Override
    public Cardinal getMoveDirection() {
        return null;
    }

    protected abstract void xorCastling(ZobristHashWriter hash, PositionStateReader oldPositionState, PositionStateReader newPositionState);
}
