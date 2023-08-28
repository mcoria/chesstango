package net.chesstango.search.smart.statistics;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.*;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.board.position.ChessPositionReader;

/**
 * @author Mauricio Coria
 */
public class GameStatistics implements Game {
    private Game imp;

    @Setter
    @Getter
    private int executedMoves;

    public GameStatistics(Game imp){
        this.imp = imp;
    }

    @Override
    public String getInitialFen() {
        return imp.getInitialFen();
    }

    @Override
    public void detectRepetitions(boolean flag) {
        imp.detectRepetitions(flag);
    }

    @Override
    public Game executeMove(Move move) {
        executedMoves++;
        imp.executeMove(move);
        return this;
    }

    @Override
    public Game undoMove() {
        imp = imp.undoMove();
        return this;
    }

    @Override
    public <V extends GameVisitor> V accept(V gameVisitor) {
        return imp.accept(gameVisitor);
    }

    @Override
    public GameStateReader getState() {
        return imp.getState();
    }

    @Override
    public ChessPositionReader getChessPosition() {
        return imp.getChessPosition();
    }

    @Override
    public GameStatus getStatus() {
        return imp.getStatus();
    }

    @Override
    public MoveContainerReader getPossibleMoves() {
        return imp.getPossibleMoves();
    }

    @Override
    public Move getMove(Square from, Square to) {
        return imp.getMove(from, to);
    }

    @Override
    public Move getMove(Square from, Square to, Piece promotionPiece) {
        return imp.getMove(from, to, promotionPiece);
    }

    @Override
    public Game executeMove(Square from, Square to) {
        executedMoves++;
        imp = imp.executeMove(from, to);
        return this;
    }

    @Override
    public Game executeMove(Square from, Square to, Piece promotionPiece) {
        executedMoves++;
        imp = imp.executeMove(from, to, promotionPiece);
        return this;
    }

    @Override
    public <T> T getObject(Class<T> theClass) {
        return imp.getObject(theClass);
    }

    @Override
    public Game mirror() {
        return imp.mirror();
    }
}
