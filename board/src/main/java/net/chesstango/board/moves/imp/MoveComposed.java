package net.chesstango.board.moves.imp;

import lombok.Setter;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 */
public class MoveComposed extends MoveImp {

    @Setter
    private MoveExecutor<PositionStateWriter> fnDoPositionState;

    @Setter
    private MoveExecutor<SquareBoardWriter> fnDoSquareBoard;

    @Setter
    private MoveExecutor<SquareBoardWriter> fnUndoSquareBoard;

    @Setter
    private MoveExecutor<BitBoardWriter> fnDoColorBoard;

    @Setter
    private MoveExecutor<BitBoardWriter> fnUndoColorBoard;

    @Setter
    private ZobristExecutor fnDoZobrist;

    public MoveComposed(PiecePositioned from, PiecePositioned to, Cardinal direction) {
        super(from, to, direction);
    }

    public MoveComposed(PiecePositioned from, PiecePositioned to) {
        super(from, to);
    }

    @Override
    public void doMove(SquareBoardWriter squareBoard) {
        fnDoSquareBoard.apply(from, to, squareBoard);
    }

    @Override
    public void undoMove(SquareBoardWriter squareBoard) {
        fnUndoSquareBoard.apply(from, to, squareBoard);
    }

    @Override
    public void doMove(PositionStateWriter positionState) {
        fnDoPositionState.apply(from, to, positionState);
    }

    @Override
    public void undoMove(PositionStateWriter positionStateWriter) {
        positionStateWriter.popState();
    }

    @Override
    public void doMove(BitBoardWriter bitBoard) {
        fnDoColorBoard.apply(from, to, bitBoard);
    }

    @Override
    public void undoMove(BitBoardWriter bitBoard) {
        fnUndoColorBoard.apply(from, to, bitBoard);
    }

    @Override
    public void doMove(MoveCacheBoardWriter moveCache) {
        moveCache.affectedPositionsByMove(from.getSquare(), to.getSquare());
        moveCache.push();
    }

    @Override
    public void undoMove(MoveCacheBoardWriter moveCache) {
        moveCache.affectedPositionsByMove(from.getSquare(), to.getSquare());
        moveCache.pop();
    }

    @Override
    public void doMove(ZobristHashWriter hash, ChessPositionReader chessPositionReader) {
        fnDoZobrist.apply(from, to, hash, chessPositionReader);
    }

    @Override
    public void undoMove(ZobristHashWriter hash) {
        hash.popState();
    }

    @Override
    public Cardinal getMoveDirection() {
        return direction;
    }

    @Override
    public boolean isQuiet() {
        return to.getPiece() == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MoveComposed theOther) {
            return from.equals(theOther.from) && to.equals(theOther.to);
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
