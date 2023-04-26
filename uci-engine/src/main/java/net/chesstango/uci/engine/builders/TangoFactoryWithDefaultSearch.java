package net.chesstango.uci.engine.builders;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.DefaultSearchMove;
import net.chesstango.search.SearchMove;
import net.chesstango.search.builders.SearchBuilder;
import net.chesstango.uci.engine.EngineTango;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Mauricio Coria
 */
public class TangoFactoryWithDefaultSearch implements TangoFactory {

    private Class<? extends GameEvaluator> gameEvaluatorClass;

    @Override
    public TangoFactory withGameEvaluatorClass(Class<? extends GameEvaluator> gameEvaluatorClass) {
        this.gameEvaluatorClass = gameEvaluatorClass;
        return this;
    }

    @Override
    public TangoFactory withSearchBuilderClass(Class<? extends SearchBuilder> searchBuilderClass) {
        throw new RuntimeException("This builder uses DefaultSearchMove class");
    }

    @Override
    public EngineTango build() {
        try {

            SearchMove search = new DefaultSearchMove(gameEvaluatorClass.getDeclaredConstructor().newInstance());

            return new EngineTango(search);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
