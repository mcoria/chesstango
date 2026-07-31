package net.chesstango.search.smart.alphabeta.root.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Acceptor;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.StopSearchingException;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.root.RootMoveEvaluationCollection;

/**
 * @author Mauricio Coria
 */
@Setter
public class StopProcessingCatch implements AlphaBetaFilter, Acceptor {

    @Getter
    private AlphaBetaFilter next;

    private Game game;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    @Override
    public int alphaBeta(int currentPly, int alpha, int beta) {
        final long startHash = game.getPosition().getZobristHash();
        try {
            return next.alphaBeta(currentPly, alpha, beta);
        } catch (StopSearchingException stopSearchingException) {
            undoMoves(startHash);
            throw stopSearchingException;
        }
    }

    private void undoMoves(long startHash) {
        long currentHash = game.getPosition().getZobristHash();
        while (currentHash != startHash) {
            game.undoMove();
            currentHash = game.getPosition().getZobristHash();
        }
    }
}
