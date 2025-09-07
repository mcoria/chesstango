package net.chesstango.search.visitors;

import net.chesstango.search.Search;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.filters.AlphaBeta;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.filters.once.AspirationWindows;
import net.chesstango.search.smart.alphabeta.filters.once.MoveEvaluationTracker;
import net.chesstango.search.smart.features.pv.filters.TranspositionPV;
import net.chesstango.search.smart.features.statistics.node.filters.AlphaBetaStatisticsExpected;
import net.chesstango.search.smart.features.statistics.node.filters.AlphaBetaStatisticsVisited;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTableRoot;

/**
 * @author Mauricio Coria
 */
public class ChainPrinterVisitor implements Visitor {

    int nestedChain = 0;

    public void print(Search search) {
        VisitorIterator visitorIterator = new VisitorIterator(this);
        visitorIterator.iterate(search);
    }

    @Override
    public void visit(IterativeDeepening iterativeDeepening) {
        printNodeObjectText(iterativeDeepening);
        printChainDownLine();
    }

    @Override
    public void visit(AlphaBetaFacade alphaBetaFacade) {
        printNodeObjectText(alphaBetaFacade);
        printChainDownLine();
    }

    @Override
    public void visit(AspirationWindows aspirationWindows) {
        printNodeObjectText(aspirationWindows);
        printChainDownLine();
    }

    @Override
    public void visit(TranspositionTableRoot transpositionTableRoot) {
        printNodeObjectText(transpositionTableRoot);
        printChainDownLine();
    }

    @Override
    public void visit(AlphaBetaStatisticsExpected alphaBetaStatisticsExpected) {
        printNodeObjectText(alphaBetaStatisticsExpected);
        printChainDownLine();
    }

    @Override
    public void visit(AlphaBeta alphaBeta) {
        printNodeObjectText(alphaBeta);
        printChainDownLine();
    }

    @Override
    public void visit(AlphaBetaStatisticsVisited alphaBetaStatisticsVisited) {
        printNodeObjectText(alphaBetaStatisticsVisited);
        printChainDownLine();
    }

    @Override
    public void visit(MoveEvaluationTracker moveEvaluationTracker) {
        printNodeObjectText(moveEvaluationTracker);
        printChainDownLine();
    }

    @Override
    public void visit(TranspositionPV transpositionPV) {
        printNodeObjectText(transpositionPV);
        printChainDownLine();
    }

    @Override
    public void visit(AlphaBetaFlowControl alphaBetaFlowControl) {
        printNodeObjectText(alphaBetaFlowControl);
    }

    private void printChainDownLine() {
        System.out.printf("%s|\n", "\t".repeat(nestedChain));
    }

    private void printChainText(String text) {
        System.out.printf("%s%s\n", "\t".repeat(nestedChain), text);
    }

    private void printNodeObjectText(Object object) {
        System.out.printf("%s%s\n", "\t".repeat(nestedChain), objectText(object));
    }

    private String objectText(Object object) {
        return String.format("%s @ %s", object.getClass().getSimpleName(), Integer.toHexString(object.hashCode()));
    }

}
