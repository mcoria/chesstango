package net.chesstango.search.visitors;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.EvaluatorCache;
import net.chesstango.evaluation.EvaluatorCacheRead;
import net.chesstango.search.*;
import net.chesstango.search.smart.*;
import net.chesstango.search.smart.core.filters.AlphaBeta;
import net.chesstango.search.smart.core.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.core.filters.QuiescenceStandingPat;
import net.chesstango.search.smart.debug.filters.DebugFilter;
import net.chesstango.search.smart.egtb.filters.EgtbEvaluation;
import net.chesstango.search.smart.evaluator.EvaluatorCacheDebug;
import net.chesstango.search.smart.evaluator.EvaluatorDebug;
import net.chesstango.search.smart.evaluator.comparators.GameEvaluatorCacheComparator;
import net.chesstango.search.smart.evaluator.filters.AlphaBetaEvaluation;
import net.chesstango.search.smart.evaluator.filters.LoopEvaluation;
import net.chesstango.search.smart.killermoves.comparators.KillerMoveComparator;
import net.chesstango.search.smart.killermoves.filters.KillerMoveTracker;
import net.chesstango.search.smart.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.smart.pv.filters.ExtendPV;
import net.chesstango.search.smart.pv.filters.PropagatePV;
import net.chesstango.search.smart.pv.groupsorters.PrincipalVariationGroup;
import net.chesstango.search.smart.pv.model.PVCalculator;
import net.chesstango.search.smart.pv.model.PVWalkerFromTT;
import net.chesstango.search.smart.SearchByDepthImp;
import net.chesstango.search.smart.root.filters.AspirationWindows;
import net.chesstango.search.smart.root.filters.RootMoveEvaluationTracker;
import net.chesstango.search.smart.root.filters.StopProcessingCatch;
import net.chesstango.search.smart.statistics.evaluation.EvaluatorStatisticsCollector;
import net.chesstango.search.smart.statistics.node.filters.*;
import net.chesstango.search.smart.statistics.transposition.TTableStatisticsComparatorCollector;
import net.chesstango.search.smart.statistics.transposition.TTableStatisticsNodeCollector;
import net.chesstango.search.smart.statistics.transposition.TTableStatisticsPVCollector;
import net.chesstango.search.smart.transposition.*;
import net.chesstango.search.smart.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.smart.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.smart.transposition.filters.*;
import net.chesstango.search.smart.zobrist.filters.ZobristTracker;
import net.chesstango.search.sorters.*;
import net.chesstango.search.sorters.comparators.*;
import net.chesstango.search.sorters.groupsorters.CatchAllNullGroup;
import net.chesstango.search.sorters.groupsorters.CatchAllSortGroup;
import net.chesstango.search.sorters.groupsorters.NoQuietBifurcation;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Esta clase recorre ella misma toda la estructura
 *
 * @author Mauricio Coria
 */
public class ChainPrinterVisitor implements Visitor {

    private final boolean printObjectId;

    private PrintStream out;

    private int nestedChain = 0;
    private boolean alphaBetaFlowControlVisited;
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

        SearchByDepth algorithm = iterativeDeepening.getSearchByDepth();
        traverse(algorithm);

        printChainText("");
        printChainText("");
        printChainSmartListenerMediator(iterativeDeepening.getListenerMediator());
    }

    @Override
    public void visit(NoIterativeDeepening noIterativeDeepening) {
        printChainDownLine();
        printNodeObjectText(noIterativeDeepening);

        SearchByDepth algorithm = noIterativeDeepening.getSearchByDepth();
        traverse(algorithm);

        printChainText("");
        printChainText("");
        printChainSmartListenerMediator(noIterativeDeepening.getListenerMediator());
    }

    @Override
    public void visit(SearchByDepthImp searchByDepthImp) {
        print(searchByDepthImp, searchByDepthImp.getNext());
    }

    @Override
    public void visit(AspirationWindows aspirationWindows) {
        print(aspirationWindows, aspirationWindows.getNext());
    }

    @Override
    public void visit(TranspositionTableRoot transpositionTableRoot) {
        printChainDownLine();
        printChainText(String.format("%s [TTable: %s]", objectText(transpositionTableRoot), printTTable(transpositionTableRoot.getTTable())));

        traverse(transpositionTableRoot.getNext());
    }

    @Override
    public void visit(AlphaBetaRootNodeStatistics alphaBetaRootNodeStatistics) {
        print(alphaBetaRootNodeStatistics, alphaBetaRootNodeStatistics.getNext());
    }

    @Override
    public void visit(AlphaBetaInteriorNodeVisited alphaBetaInteriorNodeVisited) {
        print(alphaBetaInteriorNodeVisited, alphaBetaInteriorNodeVisited.getNext());
    }

    @Override
    public void visit(AlphaBetaInteriorNodeExpected alphaBetaInteriorNodeExpected) {
        print(alphaBetaInteriorNodeExpected, alphaBetaInteriorNodeExpected.getNext());
    }

    @Override
    public void visit(AlphaBetaQuiescenceNodeVisited alphaBetaQuiescenceNodeVisited) {
        print(alphaBetaQuiescenceNodeVisited, alphaBetaQuiescenceNodeVisited.getNext());
    }

    @Override
    public void visit(AlphaBetaQuiescenceNodeExpected alphaBetaQuiescenceNodeExpected) {
        print(alphaBetaQuiescenceNodeExpected, alphaBetaQuiescenceNodeExpected.getNext());
    }

    @Override
    public void visit(AlphaBetaLeafNodeStatistics alphaBetaLeafNodeStatistics) {
        print(alphaBetaLeafNodeStatistics, alphaBetaLeafNodeStatistics.getNext());
    }

    @Override
    public void visit(AlphaBetaTerminalNodeStatistics alphaBetaTerminalNodeStatistics) {
        print(alphaBetaTerminalNodeStatistics, alphaBetaTerminalNodeStatistics.getNext());
    }

    @Override
    public void visit(AlphaBetaLoopNodeStatistics alphaBetaLoopNodeStatistics) {
        print(alphaBetaLoopNodeStatistics, alphaBetaLoopNodeStatistics.getNext());
    }


    @Override
    public void visit(AlphaBetaEgtbNodeStatistics alphaBetaEgtbNodeStatistics) {
        print(alphaBetaEgtbNodeStatistics, alphaBetaEgtbNodeStatistics.getNext());
    }

    @Override
    public void visit(AlphaBeta alphaBeta) {
        printChainDownLine();
        printNodeObjectText(alphaBeta);

        MoveSorter moveSorter = alphaBeta.getMoveSorter();
        printChainDownLine();
        printChainText(" -> Sorter");
        nestedChain++;
        traverse(moveSorter);
        nestedChain--;

        traverse(alphaBeta.getNext());
    }

    @Override
    public void visit(QuiescenceStandingPat quiescenceStandingPat) {
        printChainDownLine();

        printChainText(String.format("%s [Evaluator: %s]", objectText(quiescenceStandingPat), printGameEvaluator(quiescenceStandingPat.getEvaluator())));

        traverse(quiescenceStandingPat.getNext());
    }

    @Override
    public void visit(StopProcessingCatch stopProcessingCatch) {
        print(stopProcessingCatch, stopProcessingCatch.getNext());
    }

    @Override
    public void visit(RootMoveEvaluationTracker moveEvaluationTracker) {
        print(moveEvaluationTracker, moveEvaluationTracker.getNext());
    }

    @Override
    public void visit(PropagatePV propagatePV) {
        print(propagatePV, propagatePV.getNext());
    }

    @Override
    public void visit(ExtendPV extendPV) {
        print(extendPV, extendPV.getNext());
    }

    @Override
    public void visit(TranspositionTableTerminal transpositionTableTerminal) {
        printChainDownLine();
        printChainText(String.format("%s [TTable: %s]", objectText(transpositionTableTerminal), printTTable(transpositionTableTerminal.getTTable())));

        traverse(transpositionTableTerminal.getNext());
    }

    @Override
    public void visit(TranspositionTableLeaf transpositionTableLeaf) {
        printChainDownLine();
        printChainText(String.format("%s [TTable: %s]", objectText(transpositionTableLeaf), printTTable(transpositionTableLeaf.getTTable())));

        traverse(transpositionTableLeaf.getNext());
    }

    @Override
    public void visit(TranspositionTable transpositionTable) {
        printChainDownLine();

        printChainText(String.format("%s [TTable: %s]", objectText(transpositionTable), printTTable(transpositionTable.getTTable())));
        printChainText(String.format("|\t %s", printPVWalkerFromTT(transpositionTable.getPvWalkerFromTT())));

        traverse(transpositionTable.getNext());
    }

    @Override
    public void visit(TranspositionTableQ transpositionTableQ) {
        printChainDownLine();

        printChainText(String.format("%s [TTable: %s]", objectText(transpositionTableQ), printTTable(transpositionTableQ.getTTable())));
        printChainText(String.format("|\t %s", printPVWalkerFromTT(transpositionTableQ.getPvWalkerFromTT())));

        traverse(transpositionTableQ.getNext());
    }

    @Override
    public void visit(KillerMoveTracker killerMoveTracker) {
        print(killerMoveTracker, killerMoveTracker.getNext());
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
        printChainDownLine();
        printNodeObjectText(rootMoveSorter);
    }

    @Override
    public void visit(NodeGroupSorter nodeGroupSorter) {
        printChainDownLine();
        printNodeObjectText(nodeGroupSorter);

        printGroupSorter(nodeGroupSorter.getGroupSorter());
    }


    @Override
    public void visit(NodeMoveSorter nodeMoveSorter) {
        printChainDownLine();
        printNodeObjectText(nodeMoveSorter);

        MoveComparator moveComparator = nodeMoveSorter.getMoveComparator();
        printChainDownLine();
        printChainText(" -> Comparator");
        nestedChain++;
        traverse(moveComparator);
        nestedChain--;
    }

    @Override
    public void visit(MoveSorterDebug moveSorterDebug) {
        print(moveSorterDebug, moveSorterDebug.getNext());
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
            traverse(terminalNode);
            nestedChain--;

            AlphaBetaFilter egtbNode = alphaBetaFlowControl.getEgtbNode();
            out.println();
            printChainText(" -> EgtbNode");
            nestedChain++;
            traverse(egtbNode);
            nestedChain--;

            AlphaBetaFilter loopNode = alphaBetaFlowControl.getLoopNode();
            out.println();
            printChainText(" -> LoopNode");
            nestedChain++;
            traverse(loopNode);
            nestedChain--;

            AlphaBetaFilter leafNode = alphaBetaFlowControl.getLeafNode();
            out.println();
            printChainText(" -> LeafNode");
            nestedChain++;
            traverse(leafNode);
            nestedChain--;

            AlphaBetaFilter interiorNode = alphaBetaFlowControl.getInteriorNode();
            out.println();
            printChainText(" -> InteriorNode");
            nestedChain++;
            traverse(interiorNode);
            nestedChain--;

            AlphaBetaFilter horizonNode = alphaBetaFlowControl.getQuiescenceNode();
            if (horizonNode != null) {
                out.println();
                printChainText(" -> QuiescenceNode");
                nestedChain++;
                traverse(horizonNode);
                nestedChain--;
            }

        } else {
            out.printf("%s%s -> LOOP\n", "\t".repeat(nestedChain), objectText(alphaBetaFlowControl));
        }
    }

    /**
     *
     * MoveComparators
     */

    @Override
    public void visit(PrincipalVariationComparator principalVariationComparator) {
        print(principalVariationComparator, principalVariationComparator.getNext());
    }

    @Override
    public void visit(TranspositionHeadMoveComparator transpositionHeadMoveComparator) {
        printChainDownLine();
        printChainText(String.format("%s [TTable: %s]", objectText(transpositionHeadMoveComparator), printTTable(transpositionHeadMoveComparator.getTTable())));

        traverse(transpositionHeadMoveComparator.getNext());
    }

    @Override
    public void visit(TranspositionTailMoveComparator transpositionTailMoveComparator) {
        printChainDownLine();
        printChainText(String.format("%s [TTable: %s]", objectText(transpositionTailMoveComparator), printTTable(transpositionTailMoveComparator.getTTable())));

        traverse(transpositionTailMoveComparator.getNext());
    }

    @Override
    public void visit(KillerMoveComparator killerMoveComparator) {
        print(killerMoveComparator, killerMoveComparator.getNext());
    }

    @Override
    public void visit(GameEvaluatorCacheComparator gameEvaluatorCacheComparator) {
        printChainDownLine();
        printChainText(String.format("%s [EvaluatorCacheRead: %s]", objectText(gameEvaluatorCacheComparator), printEvaluatorCacheRead(gameEvaluatorCacheComparator.getEvaluatorCacheRead())));

        traverse(gameEvaluatorCacheComparator.getNext());
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
        traverse(noQuietNext);
        nestedChain--;

        MoveComparator quietNext = quietComparator.getQuietNext();
        out.println();
        printChainText(" -> QuietComparatorNode");
        nestedChain++;
        traverse(quietNext);
        nestedChain--;
    }

    @Override
    public void visit(DefaultMoveComparator defaultMoveComparator) {
        printChainDownLine();
        printNodeObjectText(defaultMoveComparator);
    }

    /**
     *
     * GroupSorters
     */

    @Override
    public void visit(NoQuietBifurcation noQuietBifurcation) {
        printChainDownLine();
        printNodeObjectText(noQuietBifurcation);

        GroupSorter noQuietGroup = noQuietBifurcation.getNoQuietGroup();
        printChainDownLine();
        printChainText(" -> NoQuietGroup");
        nestedChain++;
        traverse(noQuietGroup);
        nestedChain--;

        GroupSorter quietGroup = noQuietBifurcation.getQuietGroup();
        out.println();
        printChainText(" -> QuietGroup");
        nestedChain++;
        traverse(quietGroup);
        nestedChain--;
    }

    @Override
    public void visit(PrincipalVariationGroup principalVariationGroup) {
        print(principalVariationGroup, principalVariationGroup.getNext());
    }

    @Override
    public void visit(CatchAllSortGroup catchAllGroup) {
        printChainDownLine();
        printNodeObjectText(catchAllGroup);
    }

    @Override
    public void visit(CatchAllNullGroup nullGroup) {
        printChainDownLine();
        printNodeObjectText(nullGroup);
    }

    /**
     *
     * Evaluators
     */

    @Override
    public void visit(AlphaBetaEvaluation alphaBetaEvaluation) {
        printChainDownLine();
        printChainText(String.format("%s [Evaluator: %s]", objectText(alphaBetaEvaluation), printGameEvaluator(alphaBetaEvaluation.getEvaluator())));
    }

    @Override
    public void visit(LoopEvaluation loopEvaluation) {
        printChainDownLine();
        printNodeObjectText(loopEvaluation);
    }

    @Override
    public void visit(EgtbEvaluation egtbEvaluation) {
        printChainDownLine();
        printChainText(String.format("%s [EndGameTableBase: %s]", objectText(egtbEvaluation), printGameEvaluator(egtbEvaluation.getEndGameTableBase())));
    }

    /**
     *
     * Private methods
     */

    private void printChainSmartListenerMediator(ListenerMediator listenerMediator) {
        printChainText("SearchByCycleListeners:");
        nestedChain++;
        listenerMediator
                .getSearchListeners()
                .forEach(this::printNodeObjectText);
        out.println();
        nestedChain--;

        printChainText("SearchByDepthListener:");
        nestedChain++;
        listenerMediator
                .getSearchByDepthListeners()
                .forEach(this::printNodeObjectText);
        out.println();
        nestedChain--;

        printChainText("SearchByWindowsListeners:");
        nestedChain++;
        listenerMediator
                .getSearchByWindowsListeners()
                .forEach(this::printNodeObjectText);
        out.println();
        nestedChain--;

        printChainText("StopSearchingListener:");
        nestedChain++;
        listenerMediator
                .getStopSearchingListeners()
                .forEach(this::printNodeObjectText);
        out.println();
        nestedChain--;

        printChainText("ResetListener:");
        nestedChain++;
        listenerMediator
                .getResetListeners()
                .forEach(this::printNodeObjectText);
        out.println();
        nestedChain--;

        printChainText("Acceptor:");
        nestedChain++;
        listenerMediator
                .getAcceptors()
                .forEach(this::printNodeObjectText);
        out.println();
        nestedChain--;
    }

    private void printGroupSorter(GroupSorter groupSorter) {
        printChainDownLine();
        printChainText(" -> GroupSorter");
        nestedChain++;
        traverse(groupSorter);
        nestedChain--;
    }

    ///
    ///
    /// Traverse methods
    ///
    ///
    ///
    ///
    ///

    private void traverse(SearchByDepth searchByDepth) {
        if (searchByDepth instanceof Acceptor acceptor) {
            acceptor.accept(this);
        } else {
            throw new IllegalArgumentException("Unknown search algorithm: " + searchByDepth.getClass().getSimpleName());
        }
    }

    private void traverse(MoveSorter moveSorter) {
        if (moveSorter instanceof Acceptor acceptor) {
            acceptor.accept(this);
        } else {
            throw new IllegalArgumentException("Unknown move sorter: " + moveSorter.getClass().getSimpleName());
        }
    }

    private void traverse(MoveComparator moveComparator) {
        if (moveComparator instanceof Acceptor acceptor) {
            acceptor.accept(this);
        } else {
            throw new IllegalArgumentException("Unknown move comparator: " + moveComparator.getClass().getSimpleName());
        }
    }

    private void traverse(GroupSorter groupSorter) {
        if (groupSorter instanceof Acceptor acceptor) {
            acceptor.accept(this);
        } else {
            throw new IllegalArgumentException("Unknown group sorter: " + groupSorter.getClass().getSimpleName());
        }
    }

    private void traverse(AlphaBetaFilter filter) {
        if (filter instanceof Acceptor acceptor) {
            acceptor.accept(this);
        } else {
            throw new IllegalArgumentException("Unknown alpha beta filter: " + filter.getClass().getSimpleName());
        }
    }

    ///
    ///
    /// Print methods
    ///
    ///

    private void print(Object object, MoveSorter next) {
        printChainDownLine();
        printNodeObjectText(object);

        if (next instanceof Acceptor acceptor) {
            acceptor.accept(this);
        }
    }

    private void print(Object object, GroupSorter next) {
        printChainDownLine();
        printNodeObjectText(object);

        if (next instanceof Acceptor acceptor) {
            acceptor.accept(this);
        }
    }

    private void print(Object object, MoveComparator next) {
        printChainDownLine();
        printNodeObjectText(object);

        if (next instanceof Acceptor acceptor) {
            acceptor.accept(this);
        }
    }

    private void print(Object object, AlphaBetaFilter next) {
        printChainDownLine();
        printNodeObjectText(object);

        if (next instanceof Acceptor acceptor) {
            acceptor.accept(this);
        }
    }

    private void print(Object object, Acceptor acceptor) {
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

    private String printEvaluatorCacheRead(EvaluatorCacheRead evaluatorCacheRead) {
        if (evaluatorCacheRead instanceof EvaluatorCache evaluatorCache) {
            return objectText(evaluatorCache);
        } else if (evaluatorCacheRead instanceof EvaluatorCacheDebug evaluatorCacheDebug) {
            return String.format("%s -> %s", objectText(evaluatorCacheDebug), printEvaluatorCacheRead(evaluatorCacheDebug.getEvaluatorCacheRead()));
        }

        throw new IllegalArgumentException("Unknown EvaluatorCacheRead: " + evaluatorCacheRead.getClass().getSimpleName());
    }

    private String printGameEvaluator(Evaluator evaluator) {
        if (evaluator instanceof EvaluatorStatisticsCollector gameEvaluatorStatisticsCollector) {
            return String.format("%s -> %s", objectText(gameEvaluatorStatisticsCollector), printGameEvaluator(gameEvaluatorStatisticsCollector.getImp()));
        } else if (evaluator instanceof EvaluatorCache gameEvaluatorCache) {
            return String.format("%s -> %s", objectText(gameEvaluatorCache), printGameEvaluator(gameEvaluatorCache.getImp()));
        } else if (evaluator instanceof EvaluatorDebug evaluatorDebug) {
            return String.format("%s -> %s", objectText(evaluatorDebug), printGameEvaluator(evaluatorDebug.getEvaluator()));
        }

        return objectText(evaluator);
    }

    private String printTTable(TTable ttable) {
        if (ttable instanceof TTableNodeDebug ttableNodeDebug) {
            return String.format("%s -> %s", objectText(ttableNodeDebug), printTTable(ttableNodeDebug.getTTable()));
        } else if (ttable instanceof TTableStatisticsNodeCollector tTableStatisticsNodeCollector) {
            return String.format("%s -> %s", objectText(tTableStatisticsNodeCollector), printTTable(tTableStatisticsNodeCollector.getTTable()));
        } else if (ttable instanceof TTableStatisticsComparatorCollector tTableStatisticsComparatorCollector) {
            return String.format("%s -> %s", objectText(tTableStatisticsComparatorCollector), printTTable(tTableStatisticsComparatorCollector.getTTable()));
        } else if (ttable instanceof TTableStatisticsPVCollector tTableStatisticsPVCollector) {
            return String.format("%s -> %s", objectText(tTableStatisticsPVCollector), printTTable(tTableStatisticsPVCollector.getTTable()));
        } else if (ttable instanceof TTableComparatorHeadDebug tTableComparatorHeadDebug) {
            return String.format("%s -> %s", objectText(tTableComparatorHeadDebug), printTTable(tTableComparatorHeadDebug.getTTable()));
        } else if (ttable instanceof TTableComparatorTailDebug tTableComparatorTailDebug) {
            return String.format("%s -> %s", objectText(tTableComparatorTailDebug), printTTable(tTableComparatorTailDebug.getTTable()));
        }else if (ttable instanceof TTablePVDebug tTablePVDebug) {
            return String.format("%s -> %s", objectText(tTablePVDebug), printTTable(tTablePVDebug.getTTable()));
        } else if (ttable instanceof TTableArrayPrimitives tTableArray) {
            return objectText(tTableArray);
        }

        throw new IllegalArgumentException("Unknown TTable: " + ttable.getClass().getSimpleName());
    }

    private String printPVCalculator(PVCalculator ttPvReader) {
        if (ttPvReader instanceof PVCalculator PVCalculator) {
            return objectText(PVCalculator);
        }
        throw new IllegalArgumentException("Unknown PVCalculator: " + ttPvReader.getClass().getSimpleName());
    }

    private String printPVWalkerFromTT(PVWalkerFromTT pvWalkerFromTT) {
        return String.format("%s [TTable: %s]", objectText(pvWalkerFromTT), printTTable(pvWalkerFromTT.getTTable()));
    }

}
