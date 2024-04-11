package net.chesstango.search.smart.features.statistics.game;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.*;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.board.position.ChessPositionReader;

/**
 * @author Mauricio Coria
 */
public class GameStatisticsWrapper implements Game {
    private Game imp;

    @Setter
    @Getter
    private int executedMoves;

    public GameStatisticsWrapper(Game imp) {
        this.imp = imp;
    }

    @Override
    public String getInitialFEN() {
        return imp.getInitialFEN();
    }

    @Override
    public void threefoldRepetitionRule(boolean flag) {
        imp.threefoldRepetitionRule(flag);
    }

    @Override
    public void fiftyMovesRule(boolean flag) {
        imp.fiftyMovesRule(flag);
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
    public void accept(GameVisitor visitor) {
        imp.accept(visitor);
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
    public Game mirror() {
        return imp.mirror();
    }
}
