package net.chesstango.uci.engine.builders;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.builders.SearchBuilder;
import net.chesstango.uci.engine.EngineTango;

/**
 * @author Mauricio Coria
 */
public interface TangoFactory {

    TangoFactory withGameEvaluatorClass(Class<? extends GameEvaluator> gameEvaluatorClass);

    TangoFactory withSearchBuilderClass(Class<? extends SearchBuilder> searchBuilderClass);

    EngineTango build();
}
