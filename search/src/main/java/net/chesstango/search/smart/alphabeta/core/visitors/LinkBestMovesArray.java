package net.chesstango.search.smart.alphabeta.core.visitors;

import net.chesstango.board.moves.Move;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.core.filters.AlphaBeta;
import net.chesstango.search.smart.alphabeta.quiescence.Quiescence;

/**
 *
 * @author Mauricio Coria
 */
public class LinkBestMovesArray implements Visitor {

    private final Move[] bestMoves;

    public LinkBestMovesArray(final Move[] bestMoves) {
        this.bestMoves = bestMoves;
    }

    @Override
    public void visit(AlphaBeta alphaBeta) {
        alphaBeta.setBestMoves(bestMoves);
    }

    @Override
    public void visit(Quiescence quiescence) {
        quiescence.setBestMoves(bestMoves);
    }

}
