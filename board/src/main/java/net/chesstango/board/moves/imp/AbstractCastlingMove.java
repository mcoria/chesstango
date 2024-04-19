package net.chesstango.board.moves.imp;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 *
 */
abstract class AbstractCastlingMove implements MoveCastling {

    protected final PiecePositioned kingFrom;   // King
    protected final PiecePositioned kingTo;     // King

    protected final PiecePositioned rookFrom;
    protected final PiecePositioned rookTo;

    public AbstractCastlingMove(PiecePositioned kingFrom, PiecePositioned kingTo, PiecePositioned rookFrom, PiecePositioned rookTo) {
        this.kingFrom = kingFrom;
        this.kingTo = kingTo;

        this.rookFrom = rookFrom;
        this.rookTo = rookTo;
    }

    @Override
    public PiecePositioned getFrom() {
        return kingFrom;
    }

    @Override
    public PiecePositioned getTo() {
        return kingTo;
    }

    @Override
    public void executeMove(SquareBoardWriter squareBoard) {
        squareBoard.move(kingFrom, kingTo);
        squareBoard.move(rookFrom, rookTo);
    }


    @Override
    public void undoMove(SquareBoardWriter squareBoard) {
        squareBoard.setPosition(kingFrom);
        squareBoard.setPosition(kingTo);

        squareBoard.setPosition(rookFrom);
        squareBoard.setPosition(rookTo);
    }

    @Override
    public void undoMove(PositionStateWriter positionStateWriter) {
        positionStateWriter.popState();
    }

    @Override
    public void executeMove(BitBoardWriter bitBoardWriter) {
        bitBoardWriter.swapPositions(kingFrom.getPiece(), kingFrom.getSquare(), kingTo.getSquare());
        bitBoardWriter.swapPositions(rookFrom.getPiece(), rookFrom.getSquare(), rookTo.getSquare());
    }

    @Override
    public void undoMove(BitBoardWriter bitBoardWriter) {
        bitBoardWriter.swapPositions(kingFrom.getPiece(), kingTo.getSquare(), kingFrom.getSquare());
        bitBoardWriter.swapPositions(rookFrom.getPiece(), rookTo.getSquare(), rookFrom.getSquare());
    }

    @Override
    public void executeMove(MoveCacheBoardWriter moveCache) {
        moveCache.affectedPositionsByMove(kingFrom.getSquare(), kingTo.getSquare(), rookFrom.getSquare(), rookTo.getSquare());
        moveCache.push();
    }

    @Override
    public void undoMove(MoveCacheBoardWriter moveCache) {
        moveCache.affectedPositionsByMove(kingFrom.getSquare(), kingTo.getSquare(), rookFrom.getSquare(), rookTo.getSquare());
        moveCache.pop();
    }

    @Override
    public void executeMove(ZobristHashWriter hash, ChessPositionReader chessPositionReader) {
        hash.pushState();

        hash.xorPosition(kingFrom);
        hash.xorPosition(PiecePositioned.getPiecePositioned(kingTo.getSquare(), kingFrom.getPiece()));

        hash.xorPosition(rookFrom);
        hash.xorPosition(PiecePositioned.getPiecePositioned(rookTo.getSquare(), rookFrom.getPiece()));

        xorCastling(hash, chessPositionReader.getPreviousPositionState(), chessPositionReader);

        hash.clearEnPassantSquare();

        hash.xorTurn();
    }

    @Override
    public void undoMove(ZobristHashWriter hash) {
        hash.popState();
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
