package net.chesstango.search.visitors;

import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.smart.pv.groupsorters.PrincipalVariationGroup;
import net.chesstango.search.smart.root.filters.AspirationWindows;

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
        aspirationWindows.addRootMoveEvaluation(searchResultByDepth.getBestRootMoveEvaluation());
    }

    @Override
    public void visit(PrincipalVariationComparator principalVariationComparator) {
        principalVariationComparator.setLastPVMoves(searchResultByDepth.getPrincipalVariation().pvMoves());
    }
    @Override
    public void visit(PrincipalVariationGroup principalVariationGroup) {
        principalVariationGroup.setLastPVMoves(searchResultByDepth.getPrincipalVariation().pvMoves());
    }

}
