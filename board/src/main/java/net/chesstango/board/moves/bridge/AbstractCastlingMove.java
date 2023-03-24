package net.chesstango.board.moves.bridge;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.position.PiecePlacementReader;
import net.chesstango.board.position.PiecePlacementWriter;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.imp.ColorBoard;
import net.chesstango.board.position.imp.MoveCacheBoard;
import net.chesstango.board.position.imp.PositionState;
import net.chesstango.board.position.imp.ZobristHash;

/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractCastlingMove implements MoveCastling {

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
    public void executeMove(PiecePlacementWriter board) {
        board.move(kingFrom, kingTo);
        board.move(rookFrom, rookTo);
    }


    @Override
    public void undoMove(PiecePlacementWriter board) {
        board.setPosicion(kingFrom);
        board.setPosicion(kingTo);

        board.setPosicion(rookFrom);
        board.setPosicion(rookTo);
    }

    @Override
    public void undoMove(PositionState positionState) {
        positionState.popState();
    }

    @Override
    public void executeMove(ColorBoard colorBoard) {
        colorBoard.swapPositions(kingFrom.getPiece().getColor(), kingFrom.getSquare(), kingTo.getSquare());
        colorBoard.swapPositions(rookFrom.getPiece().getColor(), rookFrom.getSquare(), rookTo.getSquare());
    }

    @Override
    public void undoMove(ColorBoard colorBoard) {
        colorBoard.swapPositions(kingFrom.getPiece().getColor(), kingTo.getSquare(), kingFrom.getSquare());
        colorBoard.swapPositions(rookFrom.getPiece().getColor(), rookTo.getSquare(), rookFrom.getSquare());
    }

    @Override
    public void executeMove(MoveCacheBoard moveCache) {
        moveCache.pushCleared();
        moveCache.clearPseudoMoves(kingFrom.getSquare(), kingTo.getSquare(), rookFrom.getSquare(), rookTo.getSquare(),true);
    }

    @Override
    public void undoMove(MoveCacheBoard moveCache) {
        moveCache.clearPseudoMoves(kingFrom.getSquare(), kingTo.getSquare(), rookFrom.getSquare(), rookTo.getSquare(),false);
        moveCache.popCleared();
    }

    @Override
    public void executeMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState, PiecePlacementReader board) {
        hash.xorPosition(kingFrom);
        hash.xorPosition(PiecePositioned.getPiecePositioned(kingTo.getSquare(), kingFrom.getPiece()));

        hash.xorPosition(rookFrom);
        hash.xorPosition(PiecePositioned.getPiecePositioned(rookTo.getSquare(), rookFrom.getPiece()));

        xorCastling(hash, oldPositionState, newPositionState);

        hash.xorOldEnPassantSquare();

        hash.xorTurn();
    }

    @Override
    public void undoMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState, PiecePlacementReader board) {
        executeMove(hash, oldPositionState, newPositionState, board);
    }

    @Override
    public String toString() {
        return getFrom().toString() + " " + getTo().toString() + " - " + this.getClass().getSimpleName();
    }

    @Override
    public Cardinal getMoveDirection() {
        return null;
    }

    protected abstract void xorCastling(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState);
}
