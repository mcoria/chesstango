package net.chesstango.search.visitors;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.EvaluatorCache;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Search;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SearchAlgorithm;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.alphabeta.filters.once.AspirationWindows;
import net.chesstango.search.smart.alphabeta.filters.once.MoveEvaluationTracker;
import net.chesstango.search.smart.alphabeta.filters.once.StopProcessingCatch;
import net.chesstango.search.smart.features.debug.filters.DebugFilter;
import net.chesstango.search.smart.features.evaluator.comparators.GameEvaluatorCacheComparator;
import net.chesstango.search.smart.features.killermoves.comparators.KillerMoveComparator;
import net.chesstango.search.smart.features.killermoves.filters.KillerMoveTracker;
import net.chesstango.search.smart.features.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.smart.features.pv.filters.TranspositionPV;
import net.chesstango.search.smart.features.pv.filters.TriangularPV;
import net.chesstango.search.smart.features.statistics.evaluation.EvaluatorStatisticsWrapper;
import net.chesstango.search.smart.features.statistics.node.filters.AlphaBetaStatisticsExpected;
import net.chesstango.search.smart.features.statistics.node.filters.AlphaBetaStatisticsVisited;
import net.chesstango.search.smart.features.statistics.node.filters.QuiescenceStatisticsExpected;
import net.chesstango.search.smart.features.statistics.node.filters.QuiescenceStatisticsVisited;
import net.chesstango.search.smart.features.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.smart.features.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTable;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTableQ;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTableRoot;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTableTerminal;
import net.chesstango.search.smart.features.zobrist.filters.ZobristTracker;
import net.chesstango.search.smart.sorters.*;
import net.chesstango.search.smart.sorters.comparators.*;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class ChainPrinterVisitor implements Visitor {

    private final boolean printObjectId;

    private PrintStream out;

    private int nestedChain = 0;
    private boolean alphaBetaFlowControlVisited;
    private boolean extensionFlowControlVisited;
    private Map<String, String> objectMap;
    private int objectCounter = 1;

    public ChainPrinterVisitor(boolean printObjectId) {
        this.printObjectId = printObjectId;
    }

    public ChainPrinterVisitor() {
        this(true);
    }

    public void print(Search search, PrintStream out) {
        this.out = out;
        this.nestedChain = 0;
        this.alphaBetaFlowControlVisited = false;
        this.extensionFlowControlVisited = false;
        this.objectMap = new HashMap<>();
        this.objectCounter = 1;
        printChainText("ROOT");
        search.accept(this);
        this.out.flush();
    }

    @Override
    public void visit(IterativeDeepening iterativeDeepening) {
        printChainDownLine();
        printNodeObjectText(iterativeDeepening);

        SearchAlgorithm algorithm = iterativeDeepening.getSearchAlgorithm();
        algorithm.accept(this);

        printChainText("");
        printChainText("");
        printChainSmartListenerMediator(iterativeDeepening.getSearchListenerMediator());
    }

    @Override
    public void visit(NoIterativeDeepening noIterativeDeepening) {
        printChainDownLine();
        printNodeObjectText(noIterativeDeepening);

        SearchAlgorithm algorithm = noIterativeDeepening.getSearchAlgorithm();
        algorithm.accept(this);

        printChainText("");
        printChainText("");
        printChainSmartListenerMediator(noIterativeDeepening.getSearchListenerMediator());
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
        print(aspirationWindows, aspirationWindows.getNext());
    }

    @Override
    public void visit(TranspositionTableRoot transpositionTableRoot) {
        print(transpositionTableRoot, transpositionTableRoot.getNext());
    }

    @Override
    public void visit(AlphaBetaStatisticsExpected alphaBetaStatisticsExpected) {
        print(alphaBetaStatisticsExpected, alphaBetaStatisticsExpected.getNext());
    }

    @Override
    public void visit(AlphaBeta alphaBeta) {
        printChainDownLine();
        printNodeObjectText(alphaBeta);

        MoveSorter moveSorter = alphaBeta.getMoveSorter();
        printChainDownLine();
        printChainText(" -> Sorter");
        nestedChain++;
        moveSorter.accept(this);
        nestedChain--;

        alphaBeta.getNext().accept(this);
    }

    @Override
    public void visit(Quiescence quiescence) {
        printChainDownLine();
        printChainText(String.format("%s [Evaluator: %s]", objectText(quiescence), printGameEvaluator(quiescence.getEvaluator())));

        MoveSorter moveSorter = quiescence.getMoveSorter();
        printChainDownLine();
        printChainText(" -> Sorter");
        nestedChain++;
        moveSorter.accept(this);
        nestedChain--;

        quiescence.getNext().accept(this);
    }

    @Override
    public void visit(AlphaBetaStatisticsVisited alphaBetaStatisticsVisited) {
        print(alphaBetaStatisticsVisited, alphaBetaStatisticsVisited.getNext());
    }

    @Override
    public void visit(StopProcessingCatch stopProcessingCatch) {
        print(stopProcessingCatch, stopProcessingCatch.getNext());
    }

    @Override
    public void visit(MoveEvaluationTracker moveEvaluationTracker) {
        print(moveEvaluationTracker, moveEvaluationTracker.getNext());
    }

    @Override
    public void visit(TranspositionPV transpositionPV) {
        print(transpositionPV, transpositionPV.getNext());
    }

    @Override
    public void visit(TriangularPV triangularPV) {
        print(triangularPV, triangularPV.getNext());
    }

    @Override
    public void visit(TranspositionTableTerminal transpositionTableTerminal) {
        print(transpositionTableTerminal, transpositionTableTerminal.getNext());
    }

    @Override
    public void visit(TranspositionTable transpositionTable) {
        print(transpositionTable, transpositionTable.getNext());
    }

    @Override
    public void visit(KillerMoveTracker killerMoveTracker) {
        print(killerMoveTracker, killerMoveTracker.getNext());
    }

    @Override
    public void visit(TranspositionTableQ transpositionTableQ) {
        print(transpositionTableQ, transpositionTableQ.getNext());
    }

    @Override
    public void visit(QuiescenceStatisticsExpected quiescenceStatisticsExpected) {
        print(quiescenceStatisticsExpected, quiescenceStatisticsExpected.getNext());
    }

    @Override
    public void visit(QuiescenceStatisticsVisited quiescenceStatisticsVisited) {
        print(quiescenceStatisticsVisited, quiescenceStatisticsVisited.getNext());
    }


    @Override
    public void visit(DebugFilter debugFilter) {
        print(debugFilter, debugFilter.getNext());
    }

    @Override
    public void visit(ZobristTracker zobristTracker) {
        print(zobristTracker, zobristTracker.getNext());
    }

    /**
     * Sorters elements
     */

    @Override
    public void visit(RootMoveSorter rootMoveSorter) {
        print(rootMoveSorter, rootMoveSorter.getNodeMoveSorter());
    }

    @Override
    public void visit(NodeMoveSorter nodeMoveSorter) {
        printChainDownLine();
        printNodeObjectText(nodeMoveSorter);

        MoveComparator moveComparator = nodeMoveSorter.getMoveComparator();
        printChainDownLine();
        printChainText(" -> Comparator");
        nestedChain++;
        moveComparator.accept(this);
        nestedChain--;
    }

    @Override
    public void visit(MoveSorterDebug moveSorterDebug) {
        print(moveSorterDebug, moveSorterDebug.getMoveSorterImp());
    }


    @Override
    public void visit(AlphaBetaFlowControl alphaBetaFlowControl) {
        printChainDownLine();
        if (!alphaBetaFlowControlVisited) {
            alphaBetaFlowControlVisited = true;
            printNodeObjectText(alphaBetaFlowControl);

            AlphaBetaFilter terminalNode = alphaBetaFlowControl.getTerminalNode();
            printChainDownLine();
            printChainText(" -> TerminalNode");
            nestedChain++;
            terminalNode.accept(this);
            nestedChain--;

            AlphaBetaFilter loopNode = alphaBetaFlowControl.getLoopNode();
            out.println();
            printChainText(" -> LoopNode");
            nestedChain++;
            loopNode.accept(this);
            nestedChain--;

            AlphaBetaFilter leafNode = alphaBetaFlowControl.getLeafNode();
            out.println();
            printChainText(" -> LeafNode");
            nestedChain++;
            leafNode.accept(this);
            nestedChain--;

            AlphaBetaFilter horizonNode = alphaBetaFlowControl.getHorizonNode();
            out.println();
            printChainText(" -> HorizonNode");
            nestedChain++;
            horizonNode.accept(this);
            nestedChain--;

            AlphaBetaFilter interiorNode = alphaBetaFlowControl.getInteriorNode();
            out.println();
            printChainText(" -> InteriorNode");
            nestedChain++;
            interiorNode.accept(this);
            nestedChain--;
        } else {
            out.printf("%s%s -> LOOP\n", "\t".repeat(nestedChain), objectText(alphaBetaFlowControl));
        }
    }

    @Override
    public void visit(ExtensionFlowControl extensionFlowControl) {
        printChainDownLine();
        if (!extensionFlowControlVisited) {
            extensionFlowControlVisited = true;
            printNodeObjectText(extensionFlowControl);

            AlphaBetaFilter terminalNode = extensionFlowControl.getTerminalNode();
            printChainDownLine();
            printChainText(" -> TerminalNode");
            nestedChain++;
            terminalNode.accept(this);
            nestedChain--;

            AlphaBetaFilter leafNode = extensionFlowControl.getLeafNode();
            out.println();
            printChainText(" -> LeafNode");
            nestedChain++;
            leafNode.accept(this);
            nestedChain--;

            AlphaBetaFilter quiescenceNode = extensionFlowControl.getQuiescenceNode();
            out.println();
            printChainText(" -> QuiescenceNode");
            nestedChain++;
            quiescenceNode.accept(this);
            nestedChain--;

        } else {
            out.printf("%s%s -> LOOP\n", "\t".repeat(nestedChain), objectText(extensionFlowControl));
        }
    }

    @Override
    public void visit(PrincipalVariationComparator principalVariationComparator) {
        print(principalVariationComparator, principalVariationComparator.getNext());
    }

    @Override
    public void visit(TranspositionHeadMoveComparator transpositionHeadMoveComparator) {
        print(transpositionHeadMoveComparator, transpositionHeadMoveComparator.getNext());
    }

    @Override
    public void visit(TranspositionTailMoveComparator transpositionTailMoveComparator) {
        print(transpositionTailMoveComparator, transpositionTailMoveComparator.getNext());
    }

    @Override
    public void visit(KillerMoveComparator killerMoveComparator) {
        print(killerMoveComparator, killerMoveComparator.getNext());
    }

    @Override
    public void visit(GameEvaluatorCacheComparator gameEvaluatorCacheComparator) {
        printChainDownLine();
        printChainText(String.format("%s [EvaluatorCacheRead: %s]", objectText(gameEvaluatorCacheComparator), objectText(gameEvaluatorCacheComparator.getEvaluatorCacheRead())));

        gameEvaluatorCacheComparator.getNext().accept(this);
    }

    @Override
    public void visit(PromotionComparator promotionComparator) {
        print(promotionComparator, promotionComparator.getNext());
    }

    @Override
    public void visit(RecaptureMoveComparator recaptureMoveComparator) {
        print(recaptureMoveComparator, recaptureMoveComparator.getNext());
    }

    @Override
    public void visit(MvvLvaComparator mvvLvaComparator) {
        print(mvvLvaComparator, mvvLvaComparator.getNext());
    }

    @Override
    public void visit(QuietComparator quietComparator) {
        printChainDownLine();
        printNodeObjectText(quietComparator);

        MoveComparator noQuietNext = quietComparator.getNoQuietNext();
        printChainDownLine();
        printChainText(" -> NoQuietComparatorNode");
        nestedChain++;
        noQuietNext.accept(this);
        nestedChain--;

        MoveComparator quietNext = quietComparator.getQuietNext();
        out.println();
        printChainText(" -> QuietComparatorNode");
        nestedChain++;
        quietNext.accept(this);
        nestedChain--;
    }

    @Override
    public void visit(DefaultMoveComparator defaultMoveComparator) {
        printChainDownLine();
        printNodeObjectText(defaultMoveComparator);
    }


    private void printChainSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        printChainText("SearchByCycleListeners:");
        nestedChain++;
        searchListenerMediator.getSearchByCycleListeners()
                .forEach(this::printNodeObjectText);
        out.println();
        nestedChain--;

        printChainText("SearchByDepthListener:");
        nestedChain++;
        searchListenerMediator.getSearchByDepthListeners()
                .forEach(this::printNodeObjectText);
        out.println();
        nestedChain--;

        printChainText("SearchByWindowsListeners:");
        nestedChain++;
        searchListenerMediator.getSearchByWindowsListeners()
                .forEach(this::printNodeObjectText);
        out.println();
        nestedChain--;

        printChainText("StopSearchingListener:");
        nestedChain++;
        searchListenerMediator.getStopSearchingListeners()
                .forEach(this::printNodeObjectText);
        out.println();
        nestedChain--;

        printChainText("ResetListener:");
        nestedChain++;
        searchListenerMediator.getResetListeners()
                .forEach(this::printNodeObjectText);
        out.println();
        nestedChain--;
    }

    @Override
    public void visit(LoopEvaluation loopEvaluation) {
        printChainDownLine();
        printNodeObjectText(loopEvaluation);
    }

    @Override
    public void visit(AlphaBetaEvaluation alphaBetaEvaluation) {
        printChainDownLine();
        printChainText(String.format("%s [Evaluator: %s]", objectText(alphaBetaEvaluation), printGameEvaluator(alphaBetaEvaluation.getEvaluator())));
    }

    public void print(Object object, Acceptor acceptor) {
        printChainDownLine();
        printNodeObjectText(object);

        acceptor.accept(this);
    }

    private void printChainDownLine() {
        out.printf("%s|\n", "\t".repeat(nestedChain));
    }

    private void printChainText(String text) {
        out.printf("%s%s\n", "\t".repeat(nestedChain), text);
    }

    private void printNodeObjectText(Object object) {
        out.printf("%s%s\n", "\t".repeat(nestedChain), objectText(object));
    }

    private String objectText(Object object) {
        String objectKey = String.format("%s @ %s", object.getClass().getSimpleName(), Integer.toHexString(object.hashCode()));

        return printObjectId ?
                objectKey :
                objectMap.computeIfAbsent(objectKey, k -> String.format("%s @ %d", object.getClass().getSimpleName(), objectCounter++));
    }

    private String printGameEvaluator(Evaluator evaluator) {
        if (evaluator instanceof EvaluatorStatisticsWrapper gameEvaluatorStatisticsWrapper) {
            return String.format("%s -> %s", objectText(gameEvaluatorStatisticsWrapper), printGameEvaluator(gameEvaluatorStatisticsWrapper.getImp()));
        } else if (evaluator instanceof EvaluatorCache gameEvaluatorCache) {
            return String.format("%s -> %s", objectText(gameEvaluatorCache), printGameEvaluator(gameEvaluatorCache.getImp()));
        }

        return objectText(evaluator);
    }

}
