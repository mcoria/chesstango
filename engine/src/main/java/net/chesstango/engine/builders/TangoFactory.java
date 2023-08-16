package net.chesstango.engine.builders;

import net.chesstango.engine.Tango;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.builders.SearchBuilder;

/**
 * @author Mauricio Coria
 */
public interface TangoFactory {

    TangoFactory withGameEvaluatorClass(Class<? extends GameEvaluator> gameEvaluatorClass);

    TangoFactory withSearchBuilderClass(Class<? extends SearchBuilder> searchBuilderClass);

    Tango build();
}
