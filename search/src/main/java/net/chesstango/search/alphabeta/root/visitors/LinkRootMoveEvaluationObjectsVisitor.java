package net.chesstango.search.alphabeta.root.visitors;

import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.root.RootMoveEvaluationBest;
import net.chesstango.search.alphabeta.root.RootMoveEvaluationCollection;
import net.chesstango.search.alphabeta.SearchByDepthImp;
import net.chesstango.search.alphabeta.statistics.game.DepthCollector;
import net.chesstango.search.sorters.RootMoveSorter;

import java.util.List;

/**
 *
 * @author Mauricio Coria
 */
public class LinkRootMoveEvaluationObjectsVisitor implements Visitor {
    private final RootMoveEvaluationBest rootMoveEvaluationBest;
    private final List<RootMoveEvaluation> rootMoveEvaluationList;

    public LinkRootMoveEvaluationObjectsVisitor(RootMoveEvaluationBest rootMoveEvaluationBest,
                                                List<RootMoveEvaluation> rootMoveEvaluationList) {
        this.rootMoveEvaluationBest = rootMoveEvaluationBest;
        this.rootMoveEvaluationList = rootMoveEvaluationList;
    }


    @Override
    public void visit(SearchByDepthImp searchByDepthImp) {
        searchByDepthImp.setRootMoveEvaluationBest(rootMoveEvaluationBest);
    }

    @Override
    public void visit(RootMoveEvaluationCollection rootMoveEvaluationCollection) {
        rootMoveEvaluationCollection.setRootMoveEvaluationList(rootMoveEvaluationList);
    }

    @Override
    public void visit(DepthCollector depthCollector) {
        depthCollector.setRootMoveEvaluationList(rootMoveEvaluationList);
    }

    @Override
    public void visit(RootMoveSorter rootMoveSorter) {
        rootMoveSorter.setRootMoveEvaluationList(rootMoveEvaluationList);
    }
}
