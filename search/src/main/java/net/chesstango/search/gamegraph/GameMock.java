package net.chesstango.search.gamegraph;

import net.chesstango.board.*;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.board.position.ChessPositionReader;

/**
 * @author Mauricio Coria
 */
public class GameMock implements Game {
    private int nodesVisited = 0;

    Node currentMockNode;

    @Override
    public Game executeMove(Move move) {
        nodesVisited++;
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
        return currentMockNode.getChessPosition();
    }

    @Override
    public GameStatus getStatus() {
        return currentMockNode.getStatus();
    }

    @Override
    public MoveContainerReader getPossibleMoves() {
        return currentMockNode.getPossibleMoves();
    }

    @Override
    public Move getMove(Square from, Square to) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public Move getMove(Square from, Square to, Piece promotionPiece) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public Game executeMove(Square from, Square to) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public Game executeMove(Square from, Square to, Piece promotionPiece) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }


    @Override
    public <V extends GameVisitor> V accept(V gameVisitor) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public GameStateReader getState() {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public String getInitialFen() {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public void detectRepetitions(boolean flag) {
    }

    @Override
    public <T> T getObject(Class<T> theClass) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public Game mirror() {
        return null;
    }

    public int getNodesVisited() {
        return nodesVisited;
    }

    public Node getNodeMock() {
        return currentMockNode;
    }

}
