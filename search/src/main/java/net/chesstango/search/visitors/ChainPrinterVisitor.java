package net.chesstango.search.visitors;

import net.chesstango.search.Search;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.SearchAlgorithm;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.filters.AlphaBeta;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
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
        printChainText("ROOT");
        search.accept(this);
    }

    @Override
    public void visit(IterativeDeepening iterativeDeepening) {
        printChainDownLine();
        printNodeObjectText(iterativeDeepening);

        SearchAlgorithm algorithm = iterativeDeepening.getSearchAlgorithm();
        algorithm.accept(this);
    }

    @Override
    public void visit(AlphaBetaFacade alphaBetaFacade) {
        printChainDownLine();
        printNodeObjectText(alphaBetaFacade);

        AlphaBetaFilter alphaBeta = alphaBetaFacade.getAlphaBetaFilter();
        alphaBeta.accept(this);
    }

    @Override
    public void visit(AspirationWindows aspirationWindows) {
        printChainDownLine();
        printNodeObjectText(aspirationWindows);

        aspirationWindows.getNext().accept(this);
    }

    @Override
    public void visit(TranspositionTableRoot transpositionTableRoot) {
        printChainDownLine();
        printNodeObjectText(transpositionTableRoot);

        transpositionTableRoot.getNext().accept(this);
    }

    @Override
    public void visit(AlphaBetaStatisticsExpected alphaBetaStatisticsExpected) {
        printChainDownLine();
        printNodeObjectText(alphaBetaStatisticsExpected);

        alphaBetaStatisticsExpected.getNext().accept(this);
    }

    @Override
    public void visit(AlphaBeta alphaBeta) {
        printChainDownLine();
        printNodeObjectText(alphaBeta);

        alphaBeta.getNext().accept(this);
    }

    @Override
    public void visit(AlphaBetaStatisticsVisited alphaBetaStatisticsVisited) {
        printChainDownLine();
        printNodeObjectText(alphaBetaStatisticsVisited);

        alphaBetaStatisticsVisited.getNext().accept(this);
    }

    @Override
    public void visit(MoveEvaluationTracker moveEvaluationTracker) {
        printChainDownLine();
        printNodeObjectText(moveEvaluationTracker);

        moveEvaluationTracker.getNext().accept(this);
    }

    @Override
    public void visit(TranspositionPV transpositionPV) {
        printChainDownLine();
        printNodeObjectText(transpositionPV);

        transpositionPV.getNext().accept(this);
    }

    @Override
    public void visit(AlphaBetaFlowControl alphaBetaFlowControl) {
        printChainDownLine();
        printNodeObjectText(alphaBetaFlowControl);

        AlphaBetaFilter terminalNode = alphaBetaFlowControl.getTerminalNode();
        printChainDownLine();
        printChainText(" -> TerminalNode");
        nestedChain++;
        terminalNode.accept(this);
        nestedChain--;

        AlphaBetaFilter loopNode = alphaBetaFlowControl.getLoopNode();
        System.out.println();
        printChainText(" -> LoopNode");
        nestedChain++;
        loopNode.accept(this);
        nestedChain--;

        AlphaBetaFilter leafNode = alphaBetaFlowControl.getLeafNode();
        System.out.println();
        printChainText(" -> LeafNode");
        nestedChain++;
        leafNode.accept(this);
        nestedChain--;

        AlphaBetaFilter horizonNode = alphaBetaFlowControl.getHorizonNode();
        System.out.println();
        printChainText(" -> HorizonNode");
        nestedChain++;
        horizonNode.accept(this);
        nestedChain--;

        AlphaBetaFilter interiorNode = alphaBetaFlowControl.getInteriorNode();
        System.out.println();
        printChainText(" -> InteriorNode");
        nestedChain++;
        interiorNode.accept(this);
        nestedChain--;
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
