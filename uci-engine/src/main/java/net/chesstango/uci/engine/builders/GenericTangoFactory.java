package net.chesstango.uci.engine.builders;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMove;
import net.chesstango.search.builders.SearchBuilder;
import net.chesstango.uci.engine.EngineTango;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Mauricio Coria
 */
public class GenericTangoFactory implements TangoFactory {

    private Class<? extends GameEvaluator> gameEvaluatorClass;

    private Class<? extends SearchBuilder> searchBuilderClass;

    @Override
    public GenericTangoFactory withGameEvaluatorClass(Class<? extends GameEvaluator> gameEvaluatorClass) {
        this.gameEvaluatorClass = gameEvaluatorClass;
        return this;
    }

    @Override
    public GenericTangoFactory withSearchBuilderClass(Class<? extends SearchBuilder> searchBuilderClass) {
        this.searchBuilderClass = searchBuilderClass;
        return this;
    }

    @Override
    public EngineTango build() {
        try {
            SearchBuilder searchBuilder = searchBuilderClass.getDeclaredConstructor().newInstance();

            searchBuilder.withGameEvaluator(gameEvaluatorClass.getDeclaredConstructor().newInstance());

            SearchMove search = searchBuilder.build();

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
