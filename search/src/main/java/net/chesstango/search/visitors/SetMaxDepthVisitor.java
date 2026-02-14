package net.chesstango.search.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;

/**
 * La maxima profundidad (depth) que debe buscar
 *
 * @author Mauricio Coria
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
