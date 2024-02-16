package net.chesstango.search.builders;

import net.chesstango.evaluation.DefaultEvaluator;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.GameEvaluatorCache;
import net.chesstango.evaluation.GameEvaluatorCacheRead;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveGameWrapper;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SmartAlgorithm;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.debug.DebugEvaluation;
import net.chesstango.search.smart.alphabeta.debug.DebugFilter;
import net.chesstango.search.smart.alphabeta.debug.DebugSorter;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.alphabeta.filters.once.AspirationWindows;
import net.chesstango.search.smart.alphabeta.filters.once.MoveEvaluationTracker;
import net.chesstango.search.smart.alphabeta.filters.once.TranspositionTableRoot;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.NodeMoveSorter;
import net.chesstango.search.smart.sorters.RootMoveSorter;
import net.chesstango.search.smart.sorters.comparators.*;
import net.chesstango.search.smart.statistics.GameEvaluatorStatisticsWrapper;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author Mauricio Corias
 */
public class ChainPrinter {
    public void printChain(SearchMove searchMove) {
        if (searchMove instanceof SearchMoveGameWrapper searchMoveGameWrapper) {
            printChainSearchMoveWrapper(searchMoveGameWrapper);
        } else if (searchMove instanceof NoIterativeDeepening noIterativeDeepening) {
            printChainNoIterativeDeepening(noIterativeDeepening);
        } else if (searchMove instanceof IterativeDeepening iterativeDeepening) {
            printChainIterativeDeepening(iterativeDeepening);
        } else {
            throw new RuntimeException(String.format("Unknown SearchMove class: %s", searchMove.getClass()));
        }
    }

    private void printChainSearchMoveWrapper(SearchMoveGameWrapper searchMoveGameWrapper) {
        printNodeObjectText(searchMoveGameWrapper, 0);
        printChainDownLine(0);
        printChain(searchMoveGameWrapper.getImp());
    }

    private void printChainNoIterativeDeepening(NoIterativeDeepening noIterativeDeepening) {
        printNodeObjectText(noIterativeDeepening, 0);
        printChainDownLine(0);
        printChainSmartAlgorithm(noIterativeDeepening.getSmartAlgorithm());
    }

    private void printChainIterativeDeepening(IterativeDeepening iterativeDeepening) {
        printNodeObjectText(iterativeDeepening, 0);
        printChainDownLine(0);
        printChainSmartAlgorithm(iterativeDeepening.getSmartAlgorithm());

        printChainText("", 0);
        printChainText("", 0);
        printChainSmartListenerMediator(iterativeDeepening.getSmartListenerMediator());
    }

    private void printChainSmartListenerMediator(SmartListenerMediator smartListenerMediator) {
        System.out.print("SearchByCycleListeners:\n");
        smartListenerMediator.getSearchByCycleListeners()
                .forEach(listener -> printNodeObjectText(listener, 1));
        System.out.print("\n");

        System.out.print("SearchByDepthListener:\n");
        smartListenerMediator.getSearchByDepthListeners()
                .forEach(listener -> printNodeObjectText(listener, 1));
        System.out.print("\n");

        System.out.print("SearchByWindowsListeners:\n");
        smartListenerMediator.getSearchByWindowsListeners()
                .forEach(listener -> printNodeObjectText(listener, 1));
        System.out.print("\n");

        System.out.print("StopSearchingListener:\n");
        smartListenerMediator.getStopSearchingListeners()
                .forEach(listener -> printNodeObjectText(listener, 1));
        System.out.print("\n");

        System.out.print("ResetListener:\n");
        smartListenerMediator.getResetListeners()
                .forEach(listener -> printNodeObjectText(listener, 1));
        System.out.print("\n");
    }

    private void printChainSmartAlgorithm(SmartAlgorithm smartAlgorithm) {
        if (smartAlgorithm instanceof AlphaBetaFacade alphaBetaFacade) {
            printChainAlphaBetaFacade(alphaBetaFacade);
        } else {
            throw new RuntimeException(String.format("Unknown SmartAlgorithm class: %s", smartAlgorithm.getClass()));
        }
    }

    private void printChainAlphaBetaFacade(AlphaBetaFacade alphaBetaFacade) {
        printNodeObjectText(alphaBetaFacade, 0);
        printChainDownLine(0);
        printChainAlphaBetaFilter(alphaBetaFacade.getAlphaBetaFilter(), 0);
    }

    private List<AlphaBetaFilter> printedAlphaBetaFilter = new LinkedList<>();


    private void printChainAlphaBetaFilter(AlphaBetaFilter alphaBetaFilter, int nestedChain) {

        if (!printedAlphaBetaFilter.contains(alphaBetaFilter)) {
            printedAlphaBetaFilter.add(alphaBetaFilter);
            if (alphaBetaFilter instanceof TranspositionTableRoot transpositionTableRoot) {
                printChainTranspositionTableRoot(transpositionTableRoot, nestedChain);
            } else if (alphaBetaFilter instanceof AspirationWindows aspirationWindows) {
                printChainAspirationWindows(aspirationWindows, nestedChain);
            } else if (alphaBetaFilter instanceof AlphaBetaStatisticsExpected alphaBetaStatisticsExpected) {
                printChainAlphaBetaStatisticsExpected(alphaBetaStatisticsExpected, nestedChain);
            } else if (alphaBetaFilter instanceof MoveEvaluationTracker moveEvaluationTracker) {
                printChainMoveEvaluationTracker(moveEvaluationTracker, nestedChain);
            } else if (alphaBetaFilter instanceof AlphaBetaStatisticsVisited alphaBetaStatisticsVisited) {
                printChainAlphaBetaStatisticsVisited(alphaBetaStatisticsVisited, nestedChain);
            } else if (alphaBetaFilter instanceof TranspositionTable transpositionTable) {
                printChainTranspositionTable(transpositionTable, nestedChain);
            } else if (alphaBetaFilter instanceof AlphaBeta alphaBeta) {
                printChainAlphaBeta(alphaBeta, nestedChain);
            } else if (alphaBetaFilter instanceof AlphaBetaFlowControl alphaBetaFlowControl) {
                printChainAlphaBetaFlowControl(alphaBetaFlowControl, nestedChain);
            } else if (alphaBetaFilter instanceof AlphaBetaEvaluation alphaBetaEvaluation) {
                printChainAlphaBetaTerminal(alphaBetaEvaluation, nestedChain);
            } else if (alphaBetaFilter instanceof TranspositionTableQ transpositionTableQ) {
                printChainTranspositionTable(transpositionTableQ, nestedChain);
            } else if (alphaBetaFilter instanceof QuiescenceStatisticsExpected quiescenceStatisticsExpected) {
                printChainQuiescenceStatisticsExpected(quiescenceStatisticsExpected, nestedChain);
            } else if (alphaBetaFilter instanceof Quiescence quiescence) {
                printChainQuiescence(quiescence, nestedChain);
            } else if (alphaBetaFilter instanceof QuiescenceStatisticsVisited quiescenceStatisticsVisited) {
                printChainQuiescenceStatisticsVisited(quiescenceStatisticsVisited, nestedChain);
            } else if (alphaBetaFilter instanceof TriangularPV triangularPV) {
                printTriangularPV(triangularPV, nestedChain);
            } else if (alphaBetaFilter instanceof ExtensionFlowControl extensionFlowControl) {
                printChainQuiescenceFlowControl(extensionFlowControl, nestedChain);
            } else if (alphaBetaFilter instanceof ZobristTracker zobristTracker) {
                printChainZobristTracker(zobristTracker, nestedChain);
            } else if (alphaBetaFilter instanceof DebugFilter debugFilter) {
                printChainDebugTree(debugFilter, nestedChain);
            } else if (alphaBetaFilter instanceof LoopEvaluation loopEvaluation) {
                printChainLoopEvaluation(loopEvaluation, nestedChain);
            } else {
                throw new RuntimeException(String.format("Unknown AlphaBetaFilter class: %s", alphaBetaFilter.getClass()));
            }
        } else {
            printChainText(String.format("%s @%s -> LOOP", alphaBetaFilter.getClass().getSimpleName(), Integer.toHexString(alphaBetaFilter.hashCode())), nestedChain);
            printChainText("", nestedChain);
        }
    }


    private void printChainQuiescenceStatisticsVisited(QuiescenceStatisticsVisited quiescenceStatisticsVisited, int nestedChain) {
        printNodeObjectText(quiescenceStatisticsVisited, nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(quiescenceStatisticsVisited.getNext(), nestedChain);
    }

    private void printTriangularPV(TriangularPV triangularPV, int nestedChain) {
        printNodeObjectText(triangularPV, nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(triangularPV.getNext(), nestedChain);
    }

    private void printChainDebugTree(DebugFilter debugFilter, int nestedChain) {
        printChainText(String.format("%s", objectText(debugFilter)), nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(debugFilter.getNext(), nestedChain);
    }

    private void printChainLoopEvaluation(LoopEvaluation loopEvaluation, int nestedChain) {
        printNodeObjectText(loopEvaluation, nestedChain);
        printChainText("", nestedChain);
    }

    private void printChainZobristTracker(ZobristTracker zobristTracker, int nestedChain) {
        printNodeObjectText(zobristTracker, nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(zobristTracker.getNext(), nestedChain);
    }

    private void printChainQuiescence(Quiescence quiescence, int nestedChain) {
        printChainText(String.format("%s [%s, %s]", objectText(quiescence), printGameEvaluator(quiescence.getGameEvaluator()), printMoveSorterText(quiescence.getMoveSorter())), nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(quiescence.getNext(), nestedChain);
    }

    private void printChainQuiescenceStatisticsExpected(QuiescenceStatisticsExpected quiescenceStatisticsExpected, int nestedChain) {
        printNodeObjectText(quiescenceStatisticsExpected, nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(quiescenceStatisticsExpected.getNext(), nestedChain);
    }

    private void printChainAlphaBetaTerminal(AlphaBetaEvaluation alphaBetaEvaluation, int nestedChain) {
        printChainText(String.format("%s [%s]", objectText(alphaBetaEvaluation), printGameEvaluator(alphaBetaEvaluation.getGameEvaluator())), nestedChain);
        printChainText("", nestedChain);
    }

    private void printChainAlphaBeta(AlphaBeta alphaBeta, int nestedChain) {
        printChainText(String.format("%s [%s]", objectText(alphaBeta), printMoveSorterText(alphaBeta.getMoveSorter())), nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(alphaBeta.getNext(), nestedChain);
    }

    private String printMoveSorterText(MoveSorter moveSorter) {
        if (moveSorter instanceof RootMoveSorter rootMoveSorter) {
            return String.format("%s", objectText(rootMoveSorter));
        } else if (moveSorter instanceof NodeMoveSorter nodeMoveSorter) {
            return String.format("%s -> %s", objectText(nodeMoveSorter), printMoveComparatorText(nodeMoveSorter.getMoveComparator()));
        } else if (moveSorter instanceof DebugSorter debugSorter) {
            return String.format("%s -> %s", objectText(debugSorter), printMoveSorterText(debugSorter.getMoveSorterImp()));
        }
        throw new RuntimeException(String.format("Unknown sorter %s", objectText(moveSorter)));
    }


    private String printGameEvaluator(GameEvaluator gameEvaluator) {
        if (gameEvaluator instanceof GameEvaluatorStatisticsWrapper gameEvaluatorStatisticsWrapper) {
            return String.format("%s -> %s", objectText(gameEvaluatorStatisticsWrapper), printGameEvaluator(gameEvaluatorStatisticsWrapper.getImp()));
        } else if (gameEvaluator instanceof GameEvaluatorCache gameEvaluatorCache) {
            return String.format("%s -> %s", objectText(gameEvaluatorCache), printGameEvaluator(gameEvaluatorCache.getImp()));
        } else if (gameEvaluator instanceof DefaultEvaluator defaultEvaluator) {
            return String.format("%s -> %s", objectText(defaultEvaluator), printGameEvaluator(defaultEvaluator.getImp()));
        }

        return objectText(gameEvaluator);
    }

    private String printGameEvaluatorCacheRead(GameEvaluatorCacheRead gameEvaluatorCacheRead) {
        if (gameEvaluatorCacheRead instanceof DebugEvaluation debugEvaluation) {
            return String.format("%s -> %s", objectText(debugEvaluation), printGameEvaluatorCacheRead(debugEvaluation.getGameEvaluatorCacheRead()));
        } else if (gameEvaluatorCacheRead instanceof GameEvaluatorCache gameEvaluatorCache) {
            return printGameEvaluator(gameEvaluatorCache);
        }

        throw new RuntimeException(String.format("Unknown GameEvaluatorCacheRead %s", objectText(gameEvaluatorCacheRead)));
    }


    private String printMoveComparatorText(MoveComparator moveComparator) {
        if (moveComparator instanceof DefaultMoveComparator defaultMoveComparator) {
            return String.format("%s", objectText(defaultMoveComparator));

        } else if (moveComparator instanceof TranspositionHeadMoveComparator transpositionHeadMoveComparator) {
            return String.format("%s -> %s",
                    objectText(moveComparator),
                    printMoveComparatorText(transpositionHeadMoveComparator.getNext()));

        } else if (moveComparator instanceof TranspositionTailMoveComparator transpositionTailMoveComparator) {
            return String.format("%s -> %s",
                    objectText(moveComparator),
                    printMoveComparatorText(transpositionTailMoveComparator.getNext()));

        } else if (moveComparator instanceof GameEvaluatorComparator gameEvaluatorComparator) {
            return String.format("%s [%s] -> %s",
                    objectText(moveComparator),
                    printGameEvaluatorCacheRead(gameEvaluatorComparator.getGameEvaluatorCacheRead()),
                    printMoveComparatorText(gameEvaluatorComparator.getNext()));

        } else if (moveComparator instanceof RecaptureMoveComparator recaptureMoveComparator) {
            return String.format("%s -> %s",
                    objectText(moveComparator),
                    printMoveComparatorText(recaptureMoveComparator.getNext()));

        }

        throw new RuntimeException(String.format("Unknown MoveComparator %s", objectText(moveComparator)));
    }

    private void printChainTranspositionTableRoot(TranspositionTableRoot transpositionTableRoot, int nestedChain) {
        printNodeObjectText(transpositionTableRoot, nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(transpositionTableRoot.getNext(), nestedChain);
    }

    private void printChainAspirationWindows(AspirationWindows aspirationWindows, int nestedChain) {
        printNodeObjectText(aspirationWindows, nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(aspirationWindows.getNext(), nestedChain);
    }

    private void printChainAlphaBetaStatisticsExpected(AlphaBetaStatisticsExpected alphaBetaStatisticsExpected, int nestedChain) {
        printNodeObjectText(alphaBetaStatisticsExpected, nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(alphaBetaStatisticsExpected.getNext(), nestedChain);
    }

    private void printChainMoveEvaluationTracker(MoveEvaluationTracker moveEvaluationTracker, int nestedChain) {
        printNodeObjectText(moveEvaluationTracker, nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(moveEvaluationTracker.getNext(), nestedChain);
    }

    private void printChainAlphaBetaStatisticsVisited(AlphaBetaStatisticsVisited alphaBetaStatisticsVisited, int nestedChain) {
        printNodeObjectText(alphaBetaStatisticsVisited, nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(alphaBetaStatisticsVisited.getNext(), nestedChain);
    }

    private void printChainAlphaBetaFlowControl(AlphaBetaFlowControl alphaBetaFlowControl, int nestedChain) {
        printNodeObjectText(alphaBetaFlowControl, nestedChain);
        printChainDownLine(nestedChain);

        int nestedChainLevelDown = nestedChain + 1;

        AlphaBetaFilter terminalNode = alphaBetaFlowControl.getTerminalNode();
        printChainText(" -> TerminalNode", nestedChain);
        printChainDownLine(nestedChainLevelDown);
        printChainAlphaBetaFilter(terminalNode, nestedChainLevelDown);

        AlphaBetaFilter loopNode = alphaBetaFlowControl.getLoopNode();
        printChainText(" -> LoopNode", nestedChain);
        printChainDownLine(nestedChainLevelDown);
        printChainAlphaBetaFilter(loopNode, nestedChainLevelDown);

        AlphaBetaFilter leafNode = alphaBetaFlowControl.getLeafNode();
        printChainText(" -> LeafNode", nestedChain);
        printChainDownLine(nestedChainLevelDown);
        printChainAlphaBetaFilter(leafNode, nestedChainLevelDown);

        AlphaBetaFilter horizonNode = alphaBetaFlowControl.getHorizonNode();
        printChainText(" -> HorizonNode", nestedChain);
        printChainDownLine(nestedChainLevelDown);
        printChainAlphaBetaFilter(horizonNode, nestedChainLevelDown);

        AlphaBetaFilter interiorNode = alphaBetaFlowControl.getInteriorNode();
        printChainText(" -> InteriorNode", nestedChain);
        printChainDownLine(nestedChainLevelDown);
        printChainAlphaBetaFilter(interiorNode, nestedChainLevelDown);
    }

    private void printChainQuiescenceFlowControl(ExtensionFlowControl extensionFlowControl, int nestedChain) {
        printNodeObjectText(extensionFlowControl, nestedChain);
        printChainDownLine(nestedChain);

        int nestedChainLevelDown = nestedChain + 1;

        AlphaBetaFilter terminalNode = extensionFlowControl.getTerminalNode();
        printChainText(" -> TerminalNode", nestedChain);
        printChainDownLine(nestedChainLevelDown);
        printChainAlphaBetaFilter(terminalNode, nestedChainLevelDown);

        AlphaBetaFilter checkResolverNode = extensionFlowControl.getCheckResolverNode();
        if (Objects.nonNull(checkResolverNode)) {
            AlphaBetaFilter loopNode = extensionFlowControl.getLoopNode();
            printChainText(" -> LoopNode", nestedChain);
            printChainDownLine(nestedChainLevelDown);
            printChainAlphaBetaFilter(loopNode, nestedChainLevelDown);

            printChainText(" -> CheckResolverNode", nestedChain);
            printChainDownLine(nestedChainLevelDown);
            printChainAlphaBetaFilter(checkResolverNode, nestedChainLevelDown);
        }

        AlphaBetaFilter leafNode = extensionFlowControl.getLeafNode();
        printChainText(" -> LeafNode", nestedChain);
        printChainDownLine(nestedChainLevelDown);
        printChainAlphaBetaFilter(leafNode, nestedChainLevelDown);

        AlphaBetaFilter quiescenceNode = extensionFlowControl.getQuiescenceNode();
        printChainText(" -> QuiescenceNode", nestedChain);
        printChainDownLine(nestedChainLevelDown);
        printChainAlphaBetaFilter(quiescenceNode, nestedChainLevelDown);
    }


    private void printChainTranspositionTable(TranspositionTableAbstract transpositionTable, int nestedChain) {
        printNodeObjectText(transpositionTable, nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(transpositionTable.getNext(), nestedChain);
    }

    private void printChainDownLine(int nestedChain) {
        System.out.printf("%s|\n", "\t".repeat(nestedChain));
    }

    private void printChainText(String text, int nestedChain) {
        System.out.printf("%s%s\n", "\t".repeat(nestedChain), text);
    }

    private void printNodeObjectText(Object object, int nestedChain) {
        System.out.printf("%s%s\n", "\t".repeat(nestedChain), objectText(object));
    }

    private String objectText(Object object) {
        return String.format("%s @%s", object.getClass().getSimpleName(), Integer.toHexString(object.hashCode()));
    }

}
