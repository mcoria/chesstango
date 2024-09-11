package net.chesstango.search.gamegraph;

import lombok.Getter;
import net.chesstango.board.*;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENEncoder;

/**
 * @author Mauricio Coria
 */
public class GameMock implements Game {

    @Getter
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
    public void accept(GameVisitor visitor) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public GameStateReader getState() {
        return currentMockNode.getState();
    }

    @Override
    public FEN getInitialFEN() {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public FEN getCurrentFEN() {
        FENEncoder encoder = new FENEncoder();
        getChessPosition().constructChessPositionRepresentation(encoder);
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

    public Node getNodeMock() {
        return currentMockNode;
    }

}
