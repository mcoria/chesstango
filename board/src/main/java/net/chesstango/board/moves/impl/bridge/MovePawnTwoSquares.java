package net.chesstango.board.moves.impl.bridge;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.*;
import net.chesstango.board.position.imp.MoveCacheBoard;
import net.chesstango.board.position.imp.ZobristHash;

/**
 * @author Mauricio Coria
 */
class MovePawnTwoSquares implements Move {
    protected final PiecePositioned from;
    protected final PiecePositioned to;
    protected final Square enPassantSquare;
    protected final Cardinal direction;

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
    public void executeMove(BoardWriter board) {
        board.move(from, to);
    }

    @Override
    public void undoMove(BoardWriter board) {
        board.setPosition(from);
        board.setPosition(to);
    }

    @Override
    public void executeMove(PositionStateWriter positionState) {
        positionState.pushState();

        positionState.setEnPassantSquare(enPassantSquare);

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
    }

    @Override
    public void undoMove(ColorBoardWriter colorBoard) {
        colorBoard.swapPositions(from.getPiece().getColor(), to.getSquare(), from.getSquare());
    }

    @Override
    public void executeMove(MoveCacheBoard moveCache) {
        moveCache.pushCleared();
        moveCache.clearPseudoMoves(from.getSquare(), to.getSquare(), enPassantSquare, true);
    }

    @Override
    public void undoMove(MoveCacheBoard moveCache) {
        moveCache.clearPseudoMoves(from.getSquare(), to.getSquare(), enPassantSquare, false);
        moveCache.popCleared();
    }

    @Override
    public void executeMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState, BoardReader board) {
        hash.pushState();

        hash.xorPosition(from);

        hash.xorPosition(PiecePositioned.getPiecePositioned(to.getSquare(), from.getPiece()));

        hash.xorOldEnPassantSquare();

        if(enPassantSquare.equals(newPositionState.getEnPassantSquare())) {
            Square leftSquare = Square.getSquare(to.getSquare().getFile() - 1, to.getSquare().getRank());
            Square rightSquare = Square.getSquare(to.getSquare().getFile() + 1, to.getSquare().getRank());
            if (leftSquare != null && from.getPiece().getOpposite().equals(board.getPiece(leftSquare)) ||
                    rightSquare != null && from.getPiece().getOpposite().equals(board.getPiece(rightSquare))) {
                hash.xorEnPassantSquare(enPassantSquare);
            }
        }

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

    private Cardinal calculateMoveDirection() {
        Piece piece = getFrom().getPiece();
        return Piece.KNIGHT_WHITE.equals(piece) ||
                Piece.KNIGHT_BLACK.equals(piece)
                ? null : Cardinal.calculateSquaresDirection(getFrom().getSquare(), getTo().getSquare());
    }
}
