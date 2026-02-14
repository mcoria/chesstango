package net.chesstango.search.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;

/**
 * The maximum search depth (depth) that the search algorithm should explore.
 * This visitor is responsible for setting the upper limit on how deep the search will go
 * during the entire search process. In iterative deepening scenarios, this represents the
 * final target depth that the algorithm will reach, with each iteration progressively
 * searching deeper until this maximum is achieved or time runs out.
 *
 * @author Mauricio Coria
 * @see SetDepthVisitor
 */
public class SetMaxDepthVisitor implements Visitor {

    private final int maxDepth;

    public SetMaxDepthVisitor(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    @Override
    public void visit(IterativeDeepening iterativeDeepening) {
        iterativeDeepening.setMaxDepth(maxDepth);
    }

    @Override
    public void visit(NoIterativeDeepening noIterativeDeepening) {
        noIterativeDeepening.setMaxDepth(maxDepth);
    }
}
