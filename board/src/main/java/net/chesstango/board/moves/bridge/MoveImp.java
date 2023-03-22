package net.chesstango.board.moves.bridge;

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
 *
 */
public class MoveImp implements Move {

    @Override
    public PiecePositioned getFrom() {
        return null;
    }

    @Override
    public PiecePositioned getTo() {
        return null;
    }

    @Override
    public void executeMove(PiecePlacementWriter board) {

    }

    @Override
    public void undoMove(PiecePlacementWriter board) {

    }

    @Override
    public void executeMove(PositionState positionState) {

    }

    @Override
    public void undoMove(PositionState positionState) {

    }

    @Override
    public void executeMove(ColorBoard coloBoard) {

    }

    @Override
    public void undoMove(ColorBoard colorBoard) {

    }

    @Override
    public void executeMove(MoveCacheBoard moveCache) {

    }

    @Override
    public void undoMove(MoveCacheBoard moveCache) {

    }

    @Override
    public void executeMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState) {

    }

    @Override
    public void undoMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState) {

    }

    @Override
    public Cardinal getMoveDirection() {
        return null;
    }

    @Override
    public int compareTo(Move o) {
        return 0;
    }
}
