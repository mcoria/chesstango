package net.chesstango.board.moves.bridge;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
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
public class MovePawnTwoSquares implements Move {
    protected final PiecePositioned from;
    protected final PiecePositioned to;
    protected final Square enPassantSquare;
    protected final Cardinal direction;

    private ZobritExecutor fnDoZobrit;

    public MovePawnTwoSquares(PiecePositioned from, PiecePositioned to, Cardinal direction, Square enPassantSquare) {
        this.from = from;
        this.to = to;
        this.enPassantSquare = enPassantSquare;
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
    }

    @Override
    public void undoMove(PiecePlacementWriter board) {
        board.setPosicion(from);
        board.setPosicion(to);
    }

    @Override
    public void executeMove(PositionState positionState) {
        positionState.pushState();

        positionState.setEnPassantSquare(enPassantSquare);

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
    }

    @Override
    public void undoMove(ColorBoard colorBoard) {
        colorBoard.swapPositions(from.getPiece().getColor(), to.getSquare(), from.getSquare());
    }

    @Override
    public void executeMove(MoveCacheBoard moveCache) {
        moveCache.pushCleared();
        moveCache.clearPseudoMoves(from.getSquare(), to.getSquare(), true);
    }

    @Override
    public void undoMove(MoveCacheBoard moveCache) {
        moveCache.clearPseudoMoves(from.getSquare(), to.getSquare(), false);
        moveCache.popCleared();
    }

    @Override
    public void executeMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState) {
        fnDoZobrit.apply(from, to, hash, oldPositionState, newPositionState);
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
        if(obj instanceof MovePawnTwoSquares){
            MovePawnTwoSquares theOther = (MovePawnTwoSquares) obj;
            return from.equals(theOther.from) &&  to.equals(theOther.to) && enPassantSquare.equals(theOther.enPassantSquare);
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s %s - %s", from, to, getClass().getSimpleName());
    }


    public void setFnDoZobrit(ZobritExecutor fnDoZobrit) {
        this.fnDoZobrit = fnDoZobrit;
    }

    private Cardinal calculateMoveDirection() {
        Piece piece = getFrom().getPiece();
        return Piece.KNIGHT_WHITE.equals(piece) ||
                Piece.KNIGHT_BLACK.equals(piece)
                ? null : Cardinal.calculateSquaresDirection(getFrom().getSquare(), getTo().getSquare());
    }
}
