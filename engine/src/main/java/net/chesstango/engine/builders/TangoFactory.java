package net.chesstango.engine.builders;

import net.chesstango.engine.Tango;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.builders.SearchBuilder;

/**
 * @author Mauricio Coria
 */
public interface TangoFactory {

    Tango build();
}
