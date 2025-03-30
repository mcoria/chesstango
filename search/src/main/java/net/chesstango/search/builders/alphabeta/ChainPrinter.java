package net.chesstango.search.builders.alphabeta;

import net.chesstango.evaluation.DefaultEvaluator;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.EvaluatorCache;
import net.chesstango.evaluation.EvaluatorCacheRead;
import net.chesstango.search.Search;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SearchAlgorithm;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.BottomMoveCounterFacade;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.alphabeta.filters.once.AspirationWindows;
import net.chesstango.search.smart.alphabeta.filters.once.MoveEvaluationTracker;
import net.chesstango.search.smart.alphabeta.filters.once.StopProcessingCatch;
import net.chesstango.search.smart.features.debug.filters.DebugFilter;
import net.chesstango.search.smart.features.evaluator.EvaluatorCacheDebug;
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
import net.chesstango.search.smart.features.transposition.filters.*;
import net.chesstango.search.smart.features.zobrist.filters.ZobristTracker;
import net.chesstango.search.smart.sorters.*;
import net.chesstango.search.smart.sorters.comparators.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author Mauricio Corias
 */
public class ChainPrinter {
    public void printChain(Search search) {
        if (search instanceof NoIterativeDeepening noIterativeDeepening) {
            printChainNoIterativeDeepening(noIterativeDeepening);
        } else if (search instanceof IterativeDeepening iterativeDeepening) {
            printChainIterativeDeepening(iterativeDeepening);
        } else {
            throw new RuntimeException(String.format("Unknown SearchMove class: %s", search.getClass()));
        }
    }

    private void printChainNoIterativeDeepening(NoIterativeDeepening noIterativeDeepening) {
        printNodeObjectText(noIterativeDeepening, 0);
        printChainDownLine(0);
        printChainSmartAlgorithm(noIterativeDeepening.getSearchAlgorithm());
    }

    private void printChainIterativeDeepening(IterativeDeepening iterativeDeepening) {
        printNodeObjectText(iterativeDeepening, 0);
        printChainDownLine(0);
        printChainSmartAlgorithm(iterativeDeepening.getSearchAlgorithm());

        printChainText("", 0);
        printChainText("", 0);
        printChainSmartListenerMediator(iterativeDeepening.getSearchListenerMediator());
    }

    private void printChainSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        System.out.print("SearchByCycleListeners:\n");
        searchListenerMediator.getSearchByCycleListeners()
                .forEach(listener -> printNodeObjectText(listener, 1));
        System.out.print("\n");

        System.out.print("SearchByDepthListener:\n");
        searchListenerMediator.getSearchByDepthListeners()
                .forEach(listener -> printNodeObjectText(listener, 1));
        System.out.print("\n");

        System.out.print("SearchByWindowsListeners:\n");
        searchListenerMediator.getSearchByWindowsListeners()
                .forEach(listener -> printNodeObjectText(listener, 1));
        System.out.print("\n");

        System.out.print("StopSearchingListener:\n");
        searchListenerMediator.getStopSearchingListeners()
                .forEach(listener -> printNodeObjectText(listener, 1));
        System.out.print("\n");

        System.out.print("ResetListener:\n");
        searchListenerMediator.getResetListeners()
                .forEach(listener -> printNodeObjectText(listener, 1));
        System.out.print("\n");
    }

    private void printChainSmartAlgorithm(SearchAlgorithm searchAlgorithm) {
        if (searchAlgorithm instanceof AlphaBetaFacade alphaBetaFacade) {
            printChainAlphaBetaFacade(alphaBetaFacade);
        } if (searchAlgorithm instanceof BottomMoveCounterFacade bottomMoveCounterFacade) {
            printChainBottomMoveCounterFacade(bottomMoveCounterFacade);
        }else {
            throw new RuntimeException(String.format("Unknown SmartAlgorithm class: %s", searchAlgorithm.getClass()));
        }
    }

    private void printChainAlphaBetaFacade(AlphaBetaFacade alphaBetaFacade) {
        printNodeObjectText(alphaBetaFacade, 0);
        printChainDownLine(0);
        printChainAlphaBetaFilter(alphaBetaFacade.getAlphaBetaFilter(), 0);
    }

    private void printChainBottomMoveCounterFacade(BottomMoveCounterFacade bottomMoveCounterFacade) {
        printNodeObjectText(bottomMoveCounterFacade, 0);
        printChainDownLine(0);
        printChainAlphaBetaFilter(bottomMoveCounterFacade.getAlphaBetaFilter(), 0);
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
            } else if (alphaBetaFilter instanceof StopProcessingCatch stopProcessingCatch) {
                printChainStopProcessingCatch(stopProcessingCatch, nestedChain);
            } else if (alphaBetaFilter instanceof KillerMoveTracker killerMoveTracker) {
                printChainKillerMoveTracker(killerMoveTracker, nestedChain);
            } else if (alphaBetaFilter instanceof TranspositionTableTerminal transpositionTableTerminal) {
                printChainTranspositionTableTerminal(transpositionTableTerminal, nestedChain);
            } else if (alphaBetaFilter instanceof TranspositionPV transpositionPV) {
                printChainTranspositionPV(transpositionPV, nestedChain);
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

    private void printChainKillerMoveTracker(KillerMoveTracker killerMoveTracker, int nestedChain) {
        printChainText(String.format("%s", objectText(killerMoveTracker)), nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(killerMoveTracker.getNext(), nestedChain);
    }

    private void printChainTranspositionTableTerminal(TranspositionTableTerminal transpositionTableTerminal, int nestedChain) {
        printChainText(String.format("%s", objectText(transpositionTableTerminal)), nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(transpositionTableTerminal.getNext(), nestedChain);
    }

    private void printChainTranspositionPV(TranspositionPV transpositionPV, int nestedChain) {
        printChainText(String.format("%s", objectText(transpositionPV)), nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(transpositionPV.getNext(), nestedChain);
    }

    private void printChainLoopEvaluation(LoopEvaluation loopEvaluation, int nestedChain) {
        printNodeObjectText(loopEvaluation, nestedChain);
        printChainText("", nestedChain);
    }

    private void printChainStopProcessingCatch(StopProcessingCatch stopProcessingCatch, int nestedChain) {
        printChainText(String.format("%s", objectText(stopProcessingCatch)), nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(stopProcessingCatch.getNext(), nestedChain);
    }

    private void printChainZobristTracker(ZobristTracker zobristTracker, int nestedChain) {
        printNodeObjectText(zobristTracker, nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(zobristTracker.getNext(), nestedChain);
    }

    private void printChainQuiescence(Quiescence quiescence, int nestedChain) {
        printChainText(String.format("%s [%s, %s]", objectText(quiescence), printGameEvaluator(quiescence.getEvaluator()), printMoveSorterText(quiescence.getMoveSorter())), nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(quiescence.getNext(), nestedChain);
    }

    private void printChainQuiescenceStatisticsExpected(QuiescenceStatisticsExpected quiescenceStatisticsExpected, int nestedChain) {
        printNodeObjectText(quiescenceStatisticsExpected, nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(quiescenceStatisticsExpected.getNext(), nestedChain);
    }

    private void printChainAlphaBetaTerminal(AlphaBetaEvaluation alphaBetaEvaluation, int nestedChain) {
        printChainText(String.format("%s [%s]", objectText(alphaBetaEvaluation), printGameEvaluator(alphaBetaEvaluation.getEvaluator())), nestedChain);
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
        } else if (moveSorter instanceof MoveSorterDebug moveSorterDebug) {
            return String.format("%s -> %s", objectText(moveSorterDebug), printMoveSorterText(moveSorterDebug.getMoveSorterImp()));
        }
        throw new RuntimeException(String.format("Unknown sorter %s", objectText(moveSorter)));
    }


    private String printGameEvaluator(Evaluator evaluator) {
        if (evaluator instanceof EvaluatorStatisticsWrapper gameEvaluatorStatisticsWrapper) {
            return String.format("%s -> %s", objectText(gameEvaluatorStatisticsWrapper), printGameEvaluator(gameEvaluatorStatisticsWrapper.getImp()));
        } else if (evaluator instanceof EvaluatorCache gameEvaluatorCache) {
            return String.format("%s -> %s", objectText(gameEvaluatorCache), printGameEvaluator(gameEvaluatorCache.getImp()));
        } else if (evaluator instanceof DefaultEvaluator defaultEvaluator) {
            return String.format("%s -> %s", objectText(defaultEvaluator), printGameEvaluator(defaultEvaluator.getImp()));
        }

        return objectText(evaluator);
    }

    private String printGameEvaluatorCacheRead(EvaluatorCacheRead evaluatorCacheRead) {
        if (evaluatorCacheRead instanceof EvaluatorCacheDebug gameEvaluatorCacheDebug) {
            return String.format("%s -> %s", objectText(gameEvaluatorCacheDebug), printGameEvaluatorCacheRead(gameEvaluatorCacheDebug.getEvaluatorCacheRead()));
        } else if (evaluatorCacheRead instanceof EvaluatorCache gameEvaluatorCache) {
            return printGameEvaluator(gameEvaluatorCache);
        }

        throw new RuntimeException(String.format("Unknown GameEvaluatorCacheRead %s", objectText(evaluatorCacheRead)));
    }


    private String printMoveComparatorText(MoveComparator moveComparator) {
        if (moveComparator instanceof DefaultMoveComparator defaultMoveComparator) {
            return String.format("%s", objectText(defaultMoveComparator));

        } else if (moveComparator instanceof PrincipalVariationComparator principalVariationComparator) {
            return String.format("%s -> %s",
                    objectText(moveComparator),
                    printMoveComparatorText(principalVariationComparator.getNext()));

        } else if (moveComparator instanceof TranspositionHeadMoveComparator transpositionHeadMoveComparator) {
            return String.format("%s -> %s",
                    objectText(moveComparator),
                    printMoveComparatorText(transpositionHeadMoveComparator.getNext()));

        } else if (moveComparator instanceof TranspositionTailMoveComparator transpositionTailMoveComparator) {
            return String.format("%s -> %s",
                    objectText(moveComparator),
                    printMoveComparatorText(transpositionTailMoveComparator.getNext()));

        } else if (moveComparator instanceof GameEvaluatorCacheComparator gameEvaluatorCacheComparator) {
            return String.format("%s [%s] -> %s",
                    objectText(moveComparator),
                    printGameEvaluatorCacheRead(gameEvaluatorCacheComparator.getEvaluatorCacheRead()),
                    printMoveComparatorText(gameEvaluatorCacheComparator.getNext()));

        } else if (moveComparator instanceof RecaptureMoveComparator recaptureMoveComparator) {
            return String.format("%s -> %s",
                    objectText(moveComparator),
                    printMoveComparatorText(recaptureMoveComparator.getNext()));

        } else if (moveComparator instanceof KillerMoveComparator killerMoveComparator) {
            return String.format("%s -> %s",
                    objectText(moveComparator),
                    printMoveComparatorText(killerMoveComparator.getNext()));

        } else if (moveComparator instanceof MvvLvaComparator mvvLvaComparator) {
            return String.format("%s -> %s",
                    objectText(moveComparator),
                    printMoveComparatorText(mvvLvaComparator.getNext()));

        } else if (moveComparator instanceof PromotionComparator promotionComparator) {
            return String.format("%s -> %s",
                    objectText(moveComparator),
                    printMoveComparatorText(promotionComparator.getNext()));

        } else if (moveComparator instanceof QuietComparator quietComparator) {
            return String.format("%s -> [NoQuiet: %s, Quiet: %s]",
                    objectText(moveComparator),
                    printMoveComparatorText(quietComparator.getNoQuietNext()),
                    printMoveComparatorText(quietComparator.getQuietNext()));

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
