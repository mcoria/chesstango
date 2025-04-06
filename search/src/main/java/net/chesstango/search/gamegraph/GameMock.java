package net.chesstango.search.gamegraph;

import lombok.Getter;
import net.chesstango.board.*;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.PseudoMove;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.position.CareTakerReader;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.position.CareTakerRecord;
import net.chesstango.board.position.GameStateReader;
import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENEncoder;

import java.util.Iterator;

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
    public ChessPositionReader getChessPosition() {
        return currentMockNode.getChessPosition();
    }

    @Override
    public GameStatus getStatus() {
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
    public CareTakerReader getGameHistory() {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public CareTakerRecord getPreviousState() {
        throw new UnsupportedOperationException("Method not implemented yet");
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

    Node getNodeMock() {
        return currentMockNode;
    }

}
