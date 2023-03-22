package net.chesstango.board.moves.bridge;

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

import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 *
 */
public class MoveImp implements Move {

    protected final PiecePositioned from;
    protected final PiecePositioned to;
    protected final Cardinal direction;

    private Consumer<PositionState> fnUpdatePositionStateBeforeRollTurn;

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
        board.move(from, to);
    }

    @Override
    public void undoMove(PiecePlacementWriter board) {
        board.setPosicion(to);							//Reestablecemos destino
        board.setPosicion(from);						//Volvemos a origen
    }

    @Override
    public void executeMove(PositionState positionState) {
        positionState.pushState();
        positionState.incrementFullMoveClock();

        if(fnUpdatePositionStateBeforeRollTurn != null) {
            fnUpdatePositionStateBeforeRollTurn.accept(positionState);
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

    }

    @Override
    public void undoMove(MoveCacheBoard moveCache) {

    }

    @Override
    public void executeMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState) {
        hash.xorPosition(from);
        hash.xorPosition(PiecePositioned.getPiecePositioned(to.getSquare(), from.getPiece()));
        hash.xorTurn();
    }

    @Override
    public void undoMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState) {
        executeMove(hash, oldPositionState, newPositionState);
    }

    @Override
    public Cardinal getMoveDirection() {
        return null;
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

    private Cardinal calculateMoveDirection() {
        Piece piece = getFrom().getPiece();
        return Piece.KNIGHT_WHITE.equals(piece) ||
                Piece.KNIGHT_BLACK.equals(piece)
                ? null : Cardinal.calculateSquaresDirection(getFrom().getSquare(), getTo().getSquare());
    }

    public void setFnUpdatePositionStateBeforeRollTurn(Consumer<PositionState> fnUpdatePositionStateBeforeRollTurn) {
        this.fnUpdatePositionStateBeforeRollTurn = fnUpdatePositionStateBeforeRollTurn;
    }
}
