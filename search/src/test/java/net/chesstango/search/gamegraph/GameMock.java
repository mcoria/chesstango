package net.chesstango.search.gamegraph;

import net.chesstango.board.*;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.board.position.ChessPositionReader;

public class GameMock implements Game {
    protected Node currentMockNode;

    @Override
    public Game executeMove(Move move) {
        currentMockNode.executeMove(move, this);
        return this;
    }

    @Override
    public Game undoMove() {
        currentMockNode.undoMove(this);
        return this;
    }

    @Override
    public ChessPositionReader getChessPosition() {
        return null;
    }

    @Override
    public GameStatus getStatus() {
        return null;
    }

    @Override
    public MoveContainerReader getPossibleMoves() {
        return currentMockNode.getPossibleMoves();
    }

    @Override
    public Move getMove(Square from, Square to) {
        return null;
    }

    @Override
    public Move getMove(Square from, Square to, Piece promotionPiece) {
        return null;
    }

    @Override
    public Game executeMove(Square from, Square to) {
        return null;
    }


    @Override
    public <V extends GameVisitor> V accept(V gameVisitor) {
        return null;
    }

    @Override
    public void detectRepetitions(boolean flag) {
    }

    @Override
    public <T> T getObject(Class<T> theClass) {
        return null;
    }

}
