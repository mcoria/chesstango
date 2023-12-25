package net.chesstango.search.builders;

import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveGameWrapper;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SmartAlgorithm;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.alphabeta.filters.once.AlphaBetaRoot;
import net.chesstango.search.smart.alphabeta.filters.once.AspirationWindows;
import net.chesstango.search.smart.alphabeta.filters.once.MoveEvaluationTracker;
import net.chesstango.search.smart.alphabeta.filters.once.TranspositionTableRoot;

import java.util.LinkedList;
import java.util.List;

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
            if (alphaBetaFilter instanceof AlphaBetaRoot alphaBetaRoot) {
                printChainAlphaBetaRoot(alphaBetaRoot, nestedChain);
            } else if (alphaBetaFilter instanceof TranspositionTableRoot transpositionTableRoot) {
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
            } else if (alphaBetaFilter instanceof AlphaBetaHorizon alphaBetaHorizon) {
                printChainAlphaBetaHorizon(alphaBetaHorizon, nestedChain);
            } else if (alphaBetaFilter instanceof TranspositionTableQ transpositionTableQ) {
                printChainTranspositionTable(transpositionTableQ, nestedChain);
            } else if (alphaBetaFilter instanceof QuiescenceStatisticsExpected quiescenceStatisticsExpected) {
                printChainQuiescenceStatisticsExpected(quiescenceStatisticsExpected, nestedChain);
            } else if (alphaBetaFilter instanceof Quiescence quiescence) {
                printChainQuiescence(quiescence, nestedChain);
            } else if (alphaBetaFilter instanceof QuiescenceStatisticsVisited quiescenceStatisticsVisited) {
                printChainQuiescenceStatisticsVisited(quiescenceStatisticsVisited, nestedChain);
            } else if (alphaBetaFilter instanceof QuiescenceFlowControl quiescenceFlowControl) {
                printChainQuiescenceFlowControl(quiescenceFlowControl, nestedChain);
            } else if (alphaBetaFilter instanceof ZobristTracker zobristTracker) {
                printChainZobristTracker(zobristTracker, nestedChain);
            } else if (alphaBetaFilter instanceof DebugTree debugTree) {
                printChainDebugTree(debugTree, nestedChain);
            } else if (alphaBetaFilter instanceof DebugStandingPat debugStandingPat) {
                printChainDebugStandingPat(debugStandingPat, nestedChain);
            } else {
                throw new RuntimeException(String.format("Unknown AlphaBetaFilter class: %s", alphaBetaFilter.getClass()));
            }
            //QuiescenceFlowControl
        } else {
            if (alphaBetaFilter instanceof AlphaBetaFlowControl alphaBetaFlowControl) {
                printChainAlphaBetaFlowControlLoop(alphaBetaFlowControl, nestedChain);
            } else {
                printChainText(String.format("%s -> LOOP", alphaBetaFilter.getClass().getSimpleName()), nestedChain);
                printChainText("", nestedChain);
            }
        }
    }


    private void printChainQuiescenceFlowControl(QuiescenceFlowControl quiescenceFlowControl, int nestedChain) {
        printNodeObjectText(quiescenceFlowControl, nestedChain);
        printChainDownLink(nestedChain);

        int nestedChainLevelDown = nestedChain + 1;

        AlphaBetaFilter interiorNode = quiescenceFlowControl.getInteriorNode();
        printChainText(String.format(" -> InteriorNode -> %s", objectText(interiorNode)), nestedChain);
        printChainText("", nestedChainLevelDown);

        AlphaBetaFilter leafNode = quiescenceFlowControl.getLeafNode();
        printChainText(" -> LeafNode", nestedChain);
        printChainDownLine(nestedChainLevelDown);
        printChainAlphaBetaFilter(leafNode, nestedChainLevelDown);

    }

    private void printChainQuiescenceStatisticsVisited(QuiescenceStatisticsVisited quiescenceStatisticsVisited, int nestedChain) {
        printNodeObjectText(quiescenceStatisticsVisited, nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(quiescenceStatisticsVisited.getNext(), nestedChain);
    }

    private void printChainDebugTree(DebugTree debugTree, int nestedChain) {
        printNodeObjectText(debugTree, nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(debugTree.getNext(), nestedChain);
    }

    private void printChainDebugStandingPat(DebugStandingPat debugStandingPat, int nestedChain) {
        printNodeObjectText(debugStandingPat, nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(debugStandingPat.getNext(), nestedChain);
    }

    private void printChainZobristTracker(ZobristTracker zobristTracker, int nestedChain) {
        printNodeObjectText(zobristTracker, nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(zobristTracker.getNext(), nestedChain);
    }

    private void printChainQuiescence(Quiescence quiescence, int nestedChain) {
        printChainText(String.format("%s [%s, %s]", objectText(quiescence), objectText(quiescence.getGameEvaluator()), objectText(quiescence.getMoveSorter())), nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(quiescence.getNext(), nestedChain);
    }

    private void printChainQuiescenceStatisticsExpected(QuiescenceStatisticsExpected quiescenceStatisticsExpected, int nestedChain) {
        printNodeObjectText(quiescenceStatisticsExpected, nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(quiescenceStatisticsExpected.getNext(), nestedChain);
    }

    private void printChainAlphaBetaHorizon(AlphaBetaHorizon alphaBetaHorizon, int nestedChain) {
        printNodeObjectText(alphaBetaHorizon, nestedChain);
        printChainDownLink(nestedChain);

        int nestedChainLevelDown = nestedChain + 1;
        printChainText(" -> QuiescenceNode", nestedChain);
        printChainDownLine(nestedChainLevelDown);
        printChainAlphaBetaFilter(alphaBetaHorizon.getQuiescence(), nestedChainLevelDown);
    }

    private void printChainAlphaBetaTerminal(AlphaBetaEvaluation alphaBetaEvaluation, int nestedChain) {
        printChainText(String.format("%s [%s]", objectText(alphaBetaEvaluation), objectText(alphaBetaEvaluation.getGameEvaluator())), nestedChain);
        printChainText("", nestedChain);
    }

    private void printChainAlphaBeta(AlphaBeta alphaBeta, int nestedChain) {
        printChainText(String.format("%s [%s]", objectText(alphaBeta), objectText(alphaBeta.getMoveSorter())), nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(alphaBeta.getNext(), nestedChain);
    }

    private void printChainAlphaBetaRoot(AlphaBetaRoot alphaBetaRoot, int nestedChain) {
        printNodeObjectText(alphaBetaRoot, nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(alphaBetaRoot.getNext(), nestedChain);
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
        printChainDownLink(nestedChain);

        int nestedChainLevelDown = nestedChain + 1;

        AlphaBetaFilter interiorNode = alphaBetaFlowControl.getInteriorNode();
        printChainText(" -> InteriorNode", nestedChain);
        printChainDownLine(nestedChainLevelDown);
        printChainAlphaBetaFilter(interiorNode, nestedChainLevelDown);

        AlphaBetaFilter terminalNode = alphaBetaFlowControl.getTerminalNode();
        printChainText(" -> TerminalNode", nestedChain);
        printChainDownLine(nestedChainLevelDown);
        printChainAlphaBetaFilter(terminalNode, nestedChainLevelDown);


        AlphaBetaFilter horizonNode = alphaBetaFlowControl.getHorizonNode();
        printChainText(" -> HorizonNode", nestedChain);
        printChainDownLine(nestedChainLevelDown);
        printChainAlphaBetaFilter(horizonNode, nestedChainLevelDown);
    }

    private void printChainAlphaBetaFlowControlLoop(AlphaBetaFlowControl alphaBetaFlowControl, int nestedChain) {
        printNodeObjectText(alphaBetaFlowControl, nestedChain);
        printChainDownLink(nestedChain);

        int nestedChainLevelDown = nestedChain + 1;

        AlphaBetaFilter interiorNode = alphaBetaFlowControl.getInteriorNode();
        printChainText(String.format(" -> InteriorNode -> %s", objectText(interiorNode)), nestedChain);
        printChainText("", nestedChainLevelDown);

        AlphaBetaFilter terminalNode = alphaBetaFlowControl.getTerminalNode();
        printChainText(String.format(" -> TerminalNode -> %s ", objectText(terminalNode)), nestedChain);
        printChainText("", nestedChainLevelDown);


        AlphaBetaFilter horizonNode = alphaBetaFlowControl.getHorizonNode();
        printChainText(String.format(" -> HorizonNode -> %s ", objectText(horizonNode)), nestedChain);
        printChainText("", nestedChainLevelDown);
    }


    private void printChainTranspositionTable(TranspositionTableAbstract transpositionTable, int nestedChain) {
        printNodeObjectText(transpositionTable, nestedChain);
        printChainDownLine(nestedChain);
        printChainAlphaBetaFilter(transpositionTable.getNext(), nestedChain);
    }

    private void printChainDownLine(int nestedChain) {
        System.out.printf("%s|\n", "\t".repeat(nestedChain));
    }

    private void printChainDownLink(int nestedChain) {
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