package net.chesstango.search.smart.alphabeta.root.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.root.RootMoveEvaluationCollection;
import net.chesstango.search.smart.alphabeta.root.filters.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.statistics.game.DepthCollector;

/**
 *
 * @author Mauricio Coria
 */
public class LinkRootMoveEvaluationCollectionVisitor implements Visitor {
    private final RootMoveEvaluationCollection rootMoveEvaluationCollection;

    public LinkRootMoveEvaluationCollectionVisitor(RootMoveEvaluationCollection rootMoveEvaluationCollection) {
        this.rootMoveEvaluationCollection = rootMoveEvaluationCollection;
    }


    @Override
    public void visit(AlphaBetaFacade alphaBetaFacade) {
        alphaBetaFacade.setRootMoveEvaluationCollection(rootMoveEvaluationCollection);
    }

    @Override
    public void visit(DepthCollector depthCollector) {
        depthCollector.setRootMoveEvaluationCollection(rootMoveEvaluationCollection);
    }
}
