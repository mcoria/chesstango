package net.chesstango.uci.engine.builders;

import net.chesstango.evaluation.DefaultGameEvaluator;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMove;
import net.chesstango.search.builders.SearchBuilder;
import net.chesstango.uci.engine.EngineTango;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 */
public class TangoFactoryWithDefaultEvaluator<T extends SearchBuilder> implements TangoFactory {

    private Class<T> searchBuilderClass;

    private Consumer<T> fnSearchBuilderSetup;

    @Override
    public TangoFactoryWithDefaultEvaluator withGameEvaluatorClass(Class<? extends GameEvaluator> gameEvaluatorClass) {
        throw new RuntimeException("This builder uses DefaultEvaluator class");
    }

    @Override
    public TangoFactory withSearchBuilderClass(Class<? extends SearchBuilder> searchBuilderClass) {
        this.searchBuilderClass = (Class<T>) searchBuilderClass;
        return this;
    }

    public TangoFactoryWithDefaultEvaluator<T> withSearchBuilderCustomizer(Consumer<T> fnSearchBuilderSetup) {
        this.fnSearchBuilderSetup = fnSearchBuilderSetup;
        return this;
    }

    @Override
    public EngineTango build() {
        try {
            T searchBuilder = searchBuilderClass.getDeclaredConstructor().newInstance();

            if(fnSearchBuilderSetup != null) {
                fnSearchBuilderSetup.accept(searchBuilder);
            }

            searchBuilder.withGameEvaluator(new DefaultGameEvaluator());

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
