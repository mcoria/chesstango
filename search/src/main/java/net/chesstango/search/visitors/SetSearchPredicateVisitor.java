package net.chesstango.search.visitors;

import lombok.Setter;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.IterativeDeepening;

import java.util.function.Predicate;

/**
 * Esta clase recorre ella misma toda la estructura
 *
 * @author Mauricio Coria
 */
@Setter
public class SetSearchPredicateVisitor implements Visitor {

    private final Predicate<SearchResultByDepth> searchPredicate;

    public SetSearchPredicateVisitor(Predicate<SearchResultByDepth> searchPredicate) {
        this.searchPredicate = searchPredicate;
    }

    @Override
    public void visit(IterativeDeepening iterativeDeepening) {
        iterativeDeepening.setSearchPredicateParameter(searchPredicate);
    }

}
