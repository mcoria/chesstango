package net.chesstango.board.moves.bridge;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.PiecePlacementWriter;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.imp.ColorBoard;
import net.chesstango.board.position.imp.MoveCacheBoard;
import net.chesstango.board.position.imp.PositionState;
import net.chesstango.board.position.imp.ZobristHash;

/**
 * @author Mauricio Coria
 */
public class MoveCaptureEnPassant implements Move {
    protected final PiecePositioned from;
    protected final PiecePositioned to;
    protected final PiecePositioned capture;
    protected final Cardinal direction;


    public MoveCaptureEnPassant(PiecePositioned from, PiecePositioned to, Cardinal direction, PiecePositioned capture) {
        this.from = from;
        this.to = to;
        this.capture = capture;
        this.direction = direction;
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
        board.move(from, to);
        board.setEmptyPosicion(capture);
    }

    @Override
    public void undoMove(PiecePlacementWriter board) {
        board.setPosicion(from);
        board.setPosicion(to);
        board.setPosicion(capture);
    }

    @Override
    public void executeMove(PositionState positionState) {
        positionState.pushState();

        positionState.setEnPassantSquare(null);


        positionState.resetHalfMoveClock();

        if(Color.BLACK.equals(positionState.getCurrentTurn())){
            positionState.incrementFullMoveClock();
        }

        positionState.rollTurn();
    }

    @Override
    public void undoMove(PositionState positionState) {
        positionState.popState();
    }

    @Override
    public void executeMove(ColorBoard colorBoard) {
        colorBoard.swapPositions(from.getPiece().getColor(), from.getSquare(), to.getSquare());

        colorBoard.removePositions(capture);
    }

    @Override
    public void undoMove(ColorBoard colorBoard) {
        colorBoard.swapPositions(from.getPiece().getColor(), to.getSquare(), from.getSquare());

        colorBoard.addPositions(capture);
    }

    @Override
    public void executeMove(MoveCacheBoard moveCache) {
        moveCache.pushCleared();
        moveCache.clearPseudoMoves(from.getSquare(), to.getSquare(), capture.getSquare(), true);
    }

    @Override
    public void undoMove(MoveCacheBoard moveCache) {
        moveCache.clearPseudoMoves(from.getSquare(), to.getSquare(), capture.getSquare(), false);
        moveCache.popCleared();
    }

    @Override
    public void executeMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState) {
        hash.xorPosition(from);

        hash.xorPosition(capture);

        hash.xorPosition(PiecePositioned.getPiecePositioned(to.getSquare(), from.getPiece()));

        hash.xorTurn();
    }

    @Override
    public void undoMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState) {
        executeMove(hash, oldPositionState, newPositionState);
    }

    @Override
    public Cardinal getMoveDirection() {
        return direction;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MoveCaptureEnPassant){
            MoveCaptureEnPassant theOther = (MoveCaptureEnPassant) obj;
            return from.equals(theOther.from) &&  to.equals(to);
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s %s - %s", from, to, getClass().getSimpleName());
    }


    private Cardinal calculateMoveDirection() {
        Piece piece = getFrom().getPiece();
        return Piece.KNIGHT_WHITE.equals(piece) ||
                Piece.KNIGHT_BLACK.equals(piece)
                ? null : Cardinal.calculateSquaresDirection(getFrom().getSquare(), getTo().getSquare());
    }
}
