package net.chesstango.search.visitors;

import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.debug.listeners.SetDebugOutput;
import net.chesstango.search.smart.alphabeta.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.smart.alphabeta.pv.groupsorters.PrincipalVariationGroup;
import net.chesstango.search.smart.alphabeta.root.filters.AspirationWindows;
import net.chesstango.search.smart.sorters.RootMoveSorter;

/**
 *
 * @author Mauricio Coria
 */
public class DistributeSearchResultByDepthVisitor implements Visitor {
    private final SearchResultByDepth searchResultByDepth;

    public DistributeSearchResultByDepthVisitor(SearchResultByDepth searchResultByDepth) {
        this.searchResultByDepth = searchResultByDepth;
    }

    @Override
    public void visit(AspirationWindows aspirationWindows) {
        aspirationWindows.setLastRootMoveEvaluation(searchResultByDepth.getBestRootMoveEvaluation());
    }

    @Override
    public void visit(SetDebugOutput setDebugOutput) {
        setDebugOutput.searchByDepthCompleted(searchResultByDepth);
    }

    @Override
    public void visit(RootMoveSorter rootMoveSorter) {
        rootMoveSorter.setLastRootMoveEvaluations(searchResultByDepth.getRootMoveEvaluations());
    }

    @Override
    public void visit(PrincipalVariationComparator principalVariationComparator) {
        principalVariationComparator.setLastPrincipalVariations(searchResultByDepth.getPrincipalVariation());
    }
    @Override
    public void visit(PrincipalVariationGroup principalVariationGroup) {
        principalVariationGroup.setLastPrincipalVariations(searchResultByDepth.getPrincipalVariation());
    }

}
