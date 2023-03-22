package net.chesstango.board.moves.bridge;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.MovePromotion;
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
public class MovePromotionImp implements MovePromotion {

    protected final PiecePositioned from;
    protected final PiecePositioned to;
    protected final Piece promotion;
    protected final Cardinal direction;

    public MovePromotionImp(PiecePositioned from, PiecePositioned to, Cardinal direction, Piece promotion) {
        this.from = from;
        this.to = to;
        this.direction = direction;
        this.promotion = promotion;
    }

    public MovePromotionImp(PiecePositioned from, PiecePositioned to, Piece promotion) {
        this.from = from;
        this.to = to;
        this.promotion = promotion;
        this.direction =  calculateMoveDirection();
    }

    @Override
    public PiecePositioned getFrom() {
        return from;
    }

    @Override
    public PiecePositioned getTo() {
        return to;
    }


    @Override
    public void executeMove(PiecePlacementWriter board) {
        board.setEmptyPosicion(from);
        board.setPieza(to.getSquare(), this.promotion);
    }

    @Override
    public void undoMove(PiecePlacementWriter board) {
        board.setPosicion(from);
        board.setPosicion(to);
    }

    @Override
    public void executeMove(PositionState positionState) {
        positionState.pushState();
        positionState.incrementFullMoveClock();

        positionState.resetHalfMoveClock();

        positionState.rollTurn();
    }

    @Override
    public void undoMove(PositionState positionState) {
        positionState.popState();
    }

    @Override
    public void executeMove(ColorBoard colorBoard) {
        colorBoard.swapPositions(from.getPiece().getColor(), from.getSquare(), to.getSquare());
    }

    @Override
    public void undoMove(ColorBoard colorBoard) {
        colorBoard.swapPositions(from.getPiece().getColor(), to.getSquare(), from.getSquare());
    }

    @Override
    public void executeMove(MoveCacheBoard moveCache) {

    }

    @Override
    public void undoMove(MoveCacheBoard moveCache) {

    }

    @Override
    public void executeMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState) {

    }

    @Override
    public void undoMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState) {

    }

    @Override
    public Cardinal getMoveDirection() {
        return null;
    }

    @Override
    public Piece getPromotion() {
        return null;
    }

    private Cardinal calculateMoveDirection() {
        Piece piece = getFrom().getPiece();
        return Piece.KNIGHT_WHITE.equals(piece) ||
                Piece.KNIGHT_BLACK.equals(piece)
                ? null : Cardinal.calculateSquaresDirection(getFrom().getSquare(), getTo().getSquare());
    }
}
