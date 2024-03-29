package net.chesstango.board.moves.imp;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.MoveCaptureEnPassant;
import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 */
public class MovePawnCaptureEnPassant implements MoveCaptureEnPassant {
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
    public void executeMove(SquareBoardWriter squareBoard) {
        squareBoard.move(from, to);
        squareBoard.setEmptyPosition(capture);
    }

    @Override
    public void undoMove(SquareBoardWriter squareBoard) {
        squareBoard.setPosition(from);
        squareBoard.setPosition(to);
        squareBoard.setPosition(capture);
    }

    @Override
    public void executeMove(PositionStateWriter positionState) {
        positionState.pushState();

        positionState.setEnPassantSquare(null);


        positionState.resetHalfMoveClock();

        if (Color.BLACK.equals(from.getPiece().getColor())) {
            positionState.incrementFullMoveClock();
        }

        positionState.rollTurn();
    }

    @Override
    public void undoMove(PositionStateWriter positionStateWriter) {
        positionStateWriter.popState();
    }

    @Override
    public void executeMove(BitBoardWriter bitBoardWriter) {
        bitBoardWriter.swapPositions(from.getPiece(), from.getSquare(), to.getSquare());

        bitBoardWriter.removePosition(capture);
    }

    @Override
    public void undoMove(BitBoardWriter bitBoardWriter) {
        bitBoardWriter.swapPositions(from.getPiece(), to.getSquare(), from.getSquare());

        bitBoardWriter.addPosition(capture);
    }

    @Override
    public void executeMove(MoveCacheBoardWriter moveCache) {
        moveCache.affectedPositionsByMove(from.getSquare(), to.getSquare(), capture.getSquare());
        moveCache.push();
    }

    @Override
    public void undoMove(MoveCacheBoardWriter moveCache) {
        moveCache.affectedPositionsByMove(from.getSquare(), to.getSquare(), capture.getSquare());
        moveCache.pop();
    }

    @Override
    public void executeMove(ZobristHashWriter hash, PositionStateReader oldPositionState, PositionStateReader newPositionState, SquareBoardReader board) {
        hash.pushState();

        hash.xorPosition(from);

        hash.xorPosition(capture);

        hash.xorPosition(PiecePositioned.getPiecePositioned(to.getSquare(), from.getPiece()));

        hash.clearEnPassantSquare();

        hash.xorTurn();
    }

    @Override
    public void undoMove(ZobristHashWriter hash, PositionStateReader oldPositionState, PositionStateReader newPositionState, SquareBoardReader board) {
        hash.popState();
    }

    @Override
    public Cardinal getMoveDirection() {
        return direction;
    }

    @Override
    public boolean isQuiet() {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MovePawnCaptureEnPassant theOther) {
            return from.equals(theOther.from) && to.equals(theOther.to);
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s %s - %s", from, to, getClass().getSimpleName());
    }



    @Override
    public PiecePositioned getCapture() {
        return capture;
    }
}
