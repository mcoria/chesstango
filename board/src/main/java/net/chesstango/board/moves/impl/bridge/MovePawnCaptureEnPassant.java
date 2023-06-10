package net.chesstango.board.moves.impl.bridge;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.*;
import net.chesstango.board.position.imp.ZobristHash;

/**
 * @author Mauricio Coria
 */
class MovePawnCaptureEnPassant implements Move {
    protected final PiecePositioned from;
    protected final PiecePositioned to;
    protected final PiecePositioned capture;
    protected final Cardinal direction;


    public MovePawnCaptureEnPassant(PiecePositioned from, PiecePositioned to, Cardinal direction, PiecePositioned capture) {
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
    public void executeMove(BoardWriter board) {
        board.move(from, to);
        board.setEmptyPosition(capture);
    }

    @Override
    public void undoMove(BoardWriter board) {
        board.setPosition(from);
        board.setPosition(to);
        board.setPosition(capture);
    }

    @Override
    public void executeMove(PositionStateWriter positionState) {
        positionState.pushState();

        positionState.setEnPassantSquare(null);


        positionState.resetHalfMoveClock();

        if(Color.BLACK.equals(from.getPiece().getColor())){
            positionState.incrementFullMoveClock();
        }

        positionState.rollTurn();
    }

    @Override
    public void undoMove(PositionStateWriter positionStateWriter) {
        positionStateWriter.popState();
    }

    @Override
    public void executeMove(ColorBoardWriter colorBoard) {
        colorBoard.swapPositions(from.getPiece().getColor(), from.getSquare(), to.getSquare());

        colorBoard.removePositions(capture);
    }

    @Override
    public void undoMove(ColorBoardWriter colorBoard) {
        colorBoard.swapPositions(from.getPiece().getColor(), to.getSquare(), from.getSquare());

        colorBoard.addPositions(capture);
    }

    @Override
    public void executeMove(MoveCacheBoardWriter moveCache) {
        moveCache.pushCleared();
        moveCache.clearPseudoMoves(from.getSquare(), to.getSquare(), capture.getSquare(), true);
    }

    @Override
    public void undoMove(MoveCacheBoardWriter moveCache) {
        moveCache.clearPseudoMoves(from.getSquare(), to.getSquare(), capture.getSquare(), false);
        moveCache.popCleared();
    }

    @Override
    public void executeMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState, BoardReader board) {
        hash.pushState();

        hash.xorPosition(from);

        hash.xorPosition(capture);

        hash.xorPosition(PiecePositioned.getPiecePositioned(to.getSquare(), from.getPiece()));

        hash.xorOldEnPassantSquare();

        hash.xorTurn();
    }

    @Override
    public void undoMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState, BoardReader board) {
        hash.popState();
    }

    @Override
    public Cardinal getMoveDirection() {
        return direction;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MovePawnCaptureEnPassant){
            MovePawnCaptureEnPassant theOther = (MovePawnCaptureEnPassant) obj;
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
