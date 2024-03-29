package net.chesstango.board.moves.imp;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.*;

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
    public void executeMove(SquareBoardWriter squareBoard) {
        squareBoard.move(from, to);
    }

    @Override
    public void undoMove(SquareBoardWriter squareBoard) {
        squareBoard.setPosition(from);
        squareBoard.setPosition(to);
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
    public void executeMove(BitBoardWriter bitBoardWriter) {
        bitBoardWriter.swapPositions(from.getPiece(), from.getSquare(), to.getSquare());
    }

    @Override
    public void undoMove(BitBoardWriter bitBoardWriter) {
        bitBoardWriter.swapPositions(from.getPiece(), to.getSquare(), from.getSquare());
    }

    @Override
    public void executeMove(MoveCacheBoardWriter moveCache) {
        moveCache.affectedPositionsByMove(from.getSquare(), to.getSquare(), enPassantSquare);
        moveCache.push();
    }

    @Override
    public void undoMove(MoveCacheBoardWriter moveCache) {
        moveCache.affectedPositionsByMove(from.getSquare(), to.getSquare(), enPassantSquare);
        moveCache.pop();
    }

    @Override
    public void executeMove(ZobristHashWriter hash, PositionStateReader oldPositionState, PositionStateReader newPositionState, SquareBoardReader board) {
        hash.pushState();

        hash.xorPosition(from);

        hash.xorPosition(PiecePositioned.getPiecePositioned(to.getSquare(), from.getPiece()));

        hash.clearEnPassantSquare();

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
    public void undoMove(ZobristHashWriter hash, PositionStateReader oldPositionState, PositionStateReader newPositionState, SquareBoardReader board) {
        hash.popState();
    }

    @Override
    public Cardinal getMoveDirection() {
        return direction;
    }

    @Override
    public boolean isQuiet() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MovePawnTwoSquares theOther){
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
