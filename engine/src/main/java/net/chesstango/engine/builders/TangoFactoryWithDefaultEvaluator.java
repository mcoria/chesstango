package net.chesstango.engine.builders;

import net.chesstango.engine.Tango;
import net.chesstango.evaluation.DefaultEvaluator;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMove;
import net.chesstango.search.builders.SearchBuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 */
public class TangoFactoryWithDefaultEvaluator<T extends SearchBuilder> implements TangoFactory {

    private Class<T> searchBuilderClass;

    private Consumer<T> fnSearchBuilderSetup;

    public TangoFactory withSearchBuilderClass(Class<T> searchBuilderClass) {
        this.searchBuilderClass = searchBuilderClass;
        return this;
    }

    public TangoFactoryWithDefaultEvaluator<T> withSearchBuilderCustomizer(Consumer<T> fnSearchBuilderSetup) {
        this.fnSearchBuilderSetup = fnSearchBuilderSetup;
        return this;
    }

    @Override
    public Tango build() {
        try {
            T searchBuilder = (T) searchBuilderClass.getDeclaredConstructor().newInstance();

            if (fnSearchBuilderSetup != null) {
                fnSearchBuilderSetup.accept(searchBuilder);
            }

            searchBuilder.withGameEvaluator(new DefaultEvaluator());

            SearchMove search = searchBuilder.build();

            return new Tango(search);

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
