package net.chesstango.search.gamegraph;

import lombok.Getter;
import net.chesstango.board.*;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.PseudoMove;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.position.GameHistoryReader;
import net.chesstango.board.position.PositionReader;
import net.chesstango.board.position.GameStateReader;
import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENBuilder;

/**
 * @author Mauricio Coria
 */
public class GameMock implements Game {

    @Getter
    private int nodesVisited = 0;

    Node currentMockNode;

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
    public PositionReader getPosition() {
        return currentMockNode.getChessPosition();
    }

    @Override
    public Status getStatus() {
        return currentMockNode.getStatus();
    }

    @Override
    public MoveContainerReader<Move> getPossibleMoves() {
        return currentMockNode.getPossibleMoves();
    }

    @Override
    public MoveContainerReader<PseudoMove> getPseudoMoves() {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public void addGameListener(GameListener gameListener) {
        throw new UnsupportedOperationException("Method not implemented yet");
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
    public GameStateReader getState() {
        return currentMockNode.getState();
    }

    @Override
    public GameHistoryReader getHistory() {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public FEN getInitialFEN() {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public FEN getCurrentFEN() {
        FENBuilder encoder = new FENBuilder();
        getPosition().constructChessPositionRepresentation(encoder);
        return encoder.getChessRepresentation();
    }

    @Override
    public void threefoldRepetitionRule(boolean flag) {
    }

    @Override
    public void fiftyMovesRule(boolean flag) {
    }

    @Override
    public Game mirror() {
        return null;
    }

    Node getNodeMock() {
        return currentMockNode;
    }

}
