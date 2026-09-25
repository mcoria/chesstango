package net.chesstango.search.alphabeta.core.visitors;

import net.chesstango.board.moves.Move;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.core.filters.AlphaBeta;
import net.chesstango.search.alphabeta.quiescence.QSAlphaBeta;
import net.chesstango.search.alphabeta.quiescence.QSStandingPat;
import net.chesstango.search.alphabeta.transposition.filters.TranspositionTable;
import net.chesstango.search.alphabeta.transposition.filters.TranspositionTableQ;
import net.chesstango.search.alphabeta.transposition.filters.TranspositionTableRoot;

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
    public void visit(QSStandingPat qsStandingPat) {
        qsStandingPat.setBestMoves(bestMoves);
    }

    @Override
    public void visit(QSAlphaBeta qsAlphaBeta) {
        qsAlphaBeta.setBestMoves(bestMoves);
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
