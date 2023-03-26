package net.chesstango.board.moves.bridge;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
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
class MoveImp implements Move {
    protected final PiecePositioned from;
    protected final PiecePositioned to;
    protected final Cardinal direction;
    private MoveExecutor<PositionState> fnDoPositionState;
    private MoveExecutor<PiecePlacementWriter> fnDoMovePiecePlacement;
    private MoveExecutor<PiecePlacementWriter> fnUndoMovePiecePlacement;

    private MoveExecutor<ColorBoard> fnDoColorBoard;
    private MoveExecutor<ColorBoard> fnUndoColorBoard;

    private ZobritExecutor fnDoZobrit;

    public MoveImp(PiecePositioned from, PiecePositioned to, Cardinal direction) {
        this.from = from;
        this.to = to;
        this.direction = direction;
    }

    public MoveImp(PiecePositioned from, PiecePositioned to) {
        this.from = from;
        this.to = to;
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
        fnDoMovePiecePlacement.apply(from, to, board);
    }

    @Override
    public void undoMove(PiecePlacementWriter board) {
        fnUndoMovePiecePlacement.apply(from, to, board);
    }

    @Override
    public void executeMove(PositionState positionState) {
        fnDoPositionState.apply(from, to, positionState);
    }

    @Override
    public void undoMove(PositionState positionState) {
        positionState.popState();
    }

    @Override
    public void executeMove(ColorBoard colorBoard) {
        fnDoColorBoard.apply(from, to, colorBoard);
    }

    @Override
    public void undoMove(ColorBoard colorBoard) {
        fnUndoColorBoard.apply(from, to, colorBoard);
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
    public void executeMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState, PiecePlacementReader board) {
        fnDoZobrit.apply(from, to, hash, oldPositionState, newPositionState);
    }

    @Override
    public void undoMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState, PiecePlacementReader board) {
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

    public void setFnDoPositionState(MoveExecutor<PositionState> fnDoPositionState) {
        this.fnDoPositionState = fnDoPositionState;
    }

    public void setFnDoMovePiecePlacement(MoveExecutor<PiecePlacementWriter> fnDoMovePiecePlacement) {
        this.fnDoMovePiecePlacement = fnDoMovePiecePlacement;
    }

    public void setFnUndoMovePiecePlacement(MoveExecutor<PiecePlacementWriter> fnUndoMovePiecePlacement) {
        this.fnUndoMovePiecePlacement = fnUndoMovePiecePlacement;
    }

    public void setFnDoColorBoard(MoveExecutor<ColorBoard> fnDoColorBoard) {
        this.fnDoColorBoard = fnDoColorBoard;
    }

    public void setFnUndoColorBoard(MoveExecutor<ColorBoard> fnUndoColorBoard) {
        this.fnUndoColorBoard = fnUndoColorBoard;
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
