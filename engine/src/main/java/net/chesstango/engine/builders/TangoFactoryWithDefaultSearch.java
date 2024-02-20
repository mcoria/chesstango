package net.chesstango.engine.builders;

import net.chesstango.engine.Tango;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.DefaultSearchMove;
import net.chesstango.search.SearchMove;
import net.chesstango.search.builders.SearchBuilder;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Mauricio Coria
 */
public class TangoFactoryWithDefaultSearch<T extends GameEvaluator> implements TangoFactory {

    private Class<T> gameEvaluatorClass;


    public TangoFactory withGameEvaluatorClass(Class<T> gameEvaluatorClass) {
        this.gameEvaluatorClass = gameEvaluatorClass;
        return this;
    }

    @Override
    public Tango build() {
        try {

            SearchMove search = new DefaultSearchMove(gameEvaluatorClass.getDeclaredConstructor().newInstance());

            return new Tango(search);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
