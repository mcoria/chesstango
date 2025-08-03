package net.chesstango.search;

import net.chesstango.board.Game;
import net.chesstango.evaluation.DefaultEvaluator;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.builders.AlphaBetaBuilder;

/**
 * @author Mauricio Coria
 */
public class DefaultSearch implements Search {

    private final Search imp;

    public DefaultSearch() {
        this(new DefaultEvaluator());
    }

    public DefaultSearch(final Evaluator evaluator) {
        this.imp = AlphaBetaBuilder.createDefaultBuilderInstance(evaluator).build();
    }

    @Override
    public SearchResult search(Game game) {
        return imp.search(game);
    }

    @Override
    public void stopSearching() {
        imp.stopSearching();
    }

    @Override
    public void reset() {
        imp.reset();
    }

    @Override
    public void setSearchParameter(SearchParameter parameter, Object value) {
        imp.setSearchParameter(parameter, value);
    }

}
