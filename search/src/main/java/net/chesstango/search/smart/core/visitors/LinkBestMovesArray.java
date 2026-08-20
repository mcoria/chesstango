package net.chesstango.search.smart.core.visitors;

import net.chesstango.board.moves.Move;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.core.filters.AlphaBeta;
import net.chesstango.search.smart.core.filters.QuiescenceStandingPat;
import net.chesstango.search.smart.quiescence.Quiescence;
import net.chesstango.search.smart.transposition.filters.TranspositionTable;
import net.chesstango.search.smart.transposition.filters.TranspositionTableQ;
import net.chesstango.search.smart.transposition.filters.TranspositionTableRoot;

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

    @Override
    public void visit(QuiescenceStandingPat quiescenceStandingPat) {
        quiescenceStandingPat.setBestMoves(bestMoves);
    }

    @Override
    public void visit(TranspositionTableRoot transpositionTableRoot) {
        transpositionTableRoot.setBestMoves(bestMoves);
    }

    @Override
    public void visit(TranspositionTable transpositionTable) {
        transpositionTable.setBestMoves(bestMoves);
    }

    @Override
    public void visit(TranspositionTableQ transpositionTableQ) {
        transpositionTableQ.setBestMoves(bestMoves);
    }

}
