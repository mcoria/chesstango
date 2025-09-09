package net.chesstango.search.visitors;

import net.chesstango.search.Acceptor;
import net.chesstango.search.Search;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.SearchAlgorithm;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.alphabeta.filters.once.AspirationWindows;
import net.chesstango.search.smart.alphabeta.filters.once.MoveEvaluationTracker;
import net.chesstango.search.smart.features.killermoves.filters.KillerMoveTracker;
import net.chesstango.search.smart.features.pv.filters.TranspositionPV;
import net.chesstango.search.smart.features.statistics.node.filters.AlphaBetaStatisticsExpected;
import net.chesstango.search.smart.features.statistics.node.filters.AlphaBetaStatisticsVisited;
import net.chesstango.search.smart.features.statistics.node.filters.QuiescenceStatisticsExpected;
import net.chesstango.search.smart.features.statistics.node.filters.QuiescenceStatisticsVisited;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTable;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTableQ;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTableRoot;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTableTerminal;

/**
 * @author Mauricio Coria
 */
public class ChainPrinterVisitor implements Visitor {

    int nestedChain = 0;
    boolean alphaBetaFlowControlVisited;
    boolean extensionFlowControlVisited;

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
        print(alphaBeta, alphaBeta.getNext());
    }

    @Override
    public void visit(AlphaBetaStatisticsVisited alphaBetaStatisticsVisited) {
        print(alphaBetaStatisticsVisited, alphaBetaStatisticsVisited.getNext());
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
    public void visit(TranspositionTableQ transpositionTableQ){
        print(transpositionTableQ, transpositionTableQ.getNext());
    }

    @Override
    public void visit(QuiescenceStatisticsExpected quiescenceStatisticsExpected){
        print(quiescenceStatisticsExpected, quiescenceStatisticsExpected.getNext());
    }

    @Override
    public void visit(Quiescence quiescence) {
        print(quiescence, quiescence.getNext());
    }

    @Override
    public  void visit(QuiescenceStatisticsVisited quiescenceStatisticsVisited){
        print(quiescenceStatisticsVisited, quiescenceStatisticsVisited.getNext());
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
        } else {
            System.out.printf("%s%s -> LOOP \n", "\t".repeat(nestedChain), objectText(alphaBetaFlowControl));
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
            System.out.println();
            printChainText(" -> LeafNode");
            nestedChain++;
            leafNode.accept(this);
            nestedChain--;

            AlphaBetaFilter quiescenceNode = extensionFlowControl.getQuiescenceNode();
            System.out.println();
            printChainText(" -> QuiescenceNode");
            nestedChain++;
            quiescenceNode.accept(this);
            nestedChain--;

        }else {
            System.out.printf("%s%s -> LOOP \n", "\t".repeat(nestedChain), objectText(extensionFlowControl));
        }
    }

    @Override
    public void visit(LoopEvaluation loopEvaluation) {
        printChainDownLine();
        printNodeObjectText(loopEvaluation);
    }

    @Override
    public void visit(AlphaBetaEvaluation alphaBetaEvaluation) {
        printChainDownLine();
        printNodeObjectText(alphaBetaEvaluation);
    }

    public void print(Object object, Acceptor acceptor) {
        printChainDownLine();
        printNodeObjectText(object);

        acceptor.accept(this);
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
