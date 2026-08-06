package net.chesstango.search.smart.alphabeta.root.visitors;

import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.root.RootMoveEvaluationBest;
import net.chesstango.search.smart.alphabeta.root.RootMoveEvaluationCache;
import net.chesstango.search.smart.alphabeta.root.RootMoveEvaluationCollection;
import net.chesstango.search.smart.alphabeta.root.filters.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.statistics.game.DepthCollector;
import net.chesstango.search.smart.sorters.RootMoveSorter;

import java.util.List;

/**
 *
 * @author Mauricio Coria
 */
public class LinkRootMoveEvaluationObjectsVisitor implements Visitor {
    private final RootMoveEvaluationCache rootMoveEvaluationCache;
    private final RootMoveEvaluationBest rootMoveEvaluationBest;
    private final List<RootMoveEvaluation> rootMoveEvaluationList;

    public LinkRootMoveEvaluationObjectsVisitor(RootMoveEvaluationCache rootMoveEvaluationCache,
                                                RootMoveEvaluationBest rootMoveEvaluationBest,
                                                List<RootMoveEvaluation> rootMoveEvaluationList) {
        this.rootMoveEvaluationCache = rootMoveEvaluationCache;
        this.rootMoveEvaluationBest = rootMoveEvaluationBest;
        this.rootMoveEvaluationList = rootMoveEvaluationList;
    }


    @Override
    public void visit(AlphaBetaFacade alphaBetaFacade) {
        alphaBetaFacade.setRootMoveEvaluationBest(rootMoveEvaluationBest);
    }

    @Override
    public void visit(RootMoveEvaluationCollection rootMoveEvaluationCollection) {
        rootMoveEvaluationCollection.setRootMoveEvaluationList(rootMoveEvaluationList);
    }

    @Override
    public void visit(DepthCollector depthCollector) {
        depthCollector.setRootMoveEvaluationCache(rootMoveEvaluationCache);
    }

    @Override
    public void visit(RootMoveSorter rootMoveSorter) {
        rootMoveSorter.setRootMoveEvaluationList(rootMoveEvaluationList);
    }
}
