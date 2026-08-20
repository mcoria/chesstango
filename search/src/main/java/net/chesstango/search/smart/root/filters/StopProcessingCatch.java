package net.chesstango.search.smart.root.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Acceptor;
import net.chesstango.search.StopSearchingException;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.AlphaBetaFilter;

/**
 * @author Mauricio Coria
 */
public class StopProcessingCatch implements AlphaBetaFilter, Acceptor, SearchByCycleListener {

    @Getter
    @Setter
    private AlphaBetaFilter next;

    @Setter
    private Game game;

    @Getter
    private boolean searchStopped;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        searchStopped = false;
    }


    @Override
    public int alphaBeta(int currentPly, int alpha, int beta) {
        final long startHash = game.getPosition().getZobristHash();
        try {
            return next.alphaBeta(currentPly, alpha, beta);
        } catch (StopSearchingException stopSearchingException) {
            undoMoves(startHash);
            searchStopped = true;
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
