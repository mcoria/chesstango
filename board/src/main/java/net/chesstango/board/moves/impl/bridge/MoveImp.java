package net.chesstango.board.moves.impl.bridge;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 *
 */
class MoveImp implements Move {
    protected final PiecePositioned from;
    protected final PiecePositioned to;
    protected final Cardinal direction;
    private MoveExecutor<PositionStateWriter> fnDoPositionState;
    private MoveExecutor<SquareBoardWriter> fnDoSquareBoard;
    private MoveExecutor<SquareBoardWriter> fnUndoSquareBoard;

    private MoveExecutor<BitBoardWriter> fnDoColorBoard;
    private MoveExecutor<BitBoardWriter> fnUndoColorBoard;

    private ZobristExecutor fnDoZobrist;

    public MoveImp(PiecePositioned from, PiecePositioned to, Cardinal direction) {
        this.from = from;
        this.to = to;
        this.direction = direction;
        assert(direction == null || direction.equals(Cardinal.calculateSquaresDirection(from.getSquare(), to.getSquare())));
    }

    public MoveImp(PiecePositioned from, PiecePositioned to) {
        this.from = from;
        this.to = to;
        this.direction = calculateMoveDirection();
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
    public void executeMove(SquareBoardWriter board) {
        fnDoSquareBoard.apply(from, to, board);
    }

    @Override
    public void undoMove(SquareBoardWriter board) {
        fnUndoSquareBoard.apply(from, to, board);
    }

    @Override
    public void executeMove(PositionStateWriter positionState) {
        fnDoPositionState.apply(from, to, positionState);
    }

    @Override
    public void undoMove(PositionStateWriter positionStateWriter) {
        positionStateWriter.popState();
    }

    @Override
    public void executeMove(BitBoardWriter colorBoard) {
        fnDoColorBoard.apply(from, to, colorBoard);
    }

    @Override
    public void undoMove(BitBoardWriter colorBoard) {
        fnUndoColorBoard.apply(from, to, colorBoard);
    }

    @Override
    public void executeMove(MoveCacheBoardWriter moveCache) {
        moveCache.pushCleared();
        moveCache.clearPseudoMoves(from.getSquare(), to.getSquare(), true);
    }

    @Override
    public void undoMove(MoveCacheBoardWriter moveCache) {
        moveCache.clearPseudoMoves(from.getSquare(), to.getSquare(), false);
        moveCache.popCleared();
    }

    @Override
    public void executeMove(ZobristHashWriter hash, PositionStateReader oldPositionState, PositionStateReader newPositionState, SquareBoardReader board) {
        fnDoZobrist.apply(from, to, hash, oldPositionState, newPositionState);
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
    public boolean equals(Object obj) {
        if(obj instanceof Move){
            Move theOther = (Move) obj;
            return getFrom().equals(theOther.getFrom()) &&  getTo().equals(theOther.getTo());
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s %s - %s", from, to, getClass().getSimpleName());
    }

    public void setFnDoPositionState(MoveExecutor<PositionStateWriter> fnDoPositionState) {
        this.fnDoPositionState = fnDoPositionState;
    }

    public void setFnDoSquareBoard(MoveExecutor<SquareBoardWriter> fnDoSquareBoard) {
        this.fnDoSquareBoard = fnDoSquareBoard;
    }

    public void setFnUndoSquareBoard(MoveExecutor<SquareBoardWriter> fnUndoSquareBoard) {
        this.fnUndoSquareBoard = fnUndoSquareBoard;
    }

    public void setFnDoColorBoard(MoveExecutor<BitBoardWriter> fnDoColorBoard) {
        this.fnDoColorBoard = fnDoColorBoard;
    }

    public void setFnUndoColorBoard(MoveExecutor<BitBoardWriter> fnUndoColorBoard) {
        this.fnUndoColorBoard = fnUndoColorBoard;
    }

    public void setFnDoZobrist(ZobristExecutor fnDoZobrist) {
        this.fnDoZobrist = fnDoZobrist;
    }

    private Cardinal calculateMoveDirection() {
        Piece piece = getFrom().getPiece();
        return Piece.KNIGHT_WHITE.equals(piece) ||
                Piece.KNIGHT_BLACK.equals(piece)
                ? null : Cardinal.calculateSquaresDirection(getFrom().getSquare(), getTo().getSquare());
    }
}
