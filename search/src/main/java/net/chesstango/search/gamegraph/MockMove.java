package net.chesstango.search.gamegraph;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.position.ChessPositionWriter;
import net.chesstango.board.position.PiecePlacementWriter;
import net.chesstango.board.position.imp.ColorBoard;
import net.chesstango.board.position.imp.MoveCacheBoard;
import net.chesstango.board.position.imp.PositionState;

public class MockMove implements Move {
    PiecePositioned from;
    PiecePositioned to;


    @Override
    public PiecePositioned getFrom() {
        return from;
    }

    @Override
    public PiecePositioned getTo() {
        return to;
    }

    @Override
    public void executeMove(ChessPositionWriter chessPosition) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public void undoMove(ChessPositionWriter chessPosition) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public boolean filter(MoveFilter filter) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public void executeMove(PiecePlacementWriter board) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public void undoMove(PiecePlacementWriter board) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public void executeMove(PositionState positionState) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public void undoMove(PositionState positionState) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public void executeMove(ColorBoard coloBoard) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public void undoMove(ColorBoard colorBoard) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public void executeMove(MoveCacheBoard moveCache) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public void undoMove(MoveCacheBoard moveCache) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public Cardinal getMoveDirection() {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public int compareTo(Move o) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }
}
