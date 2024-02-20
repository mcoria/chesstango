package net.chesstango.uci.arena.gui;

import net.chesstango.engine.builders.TangoFactoryWithDefaultEvaluator;
import net.chesstango.engine.builders.TangoFactoryWithDefaultSearch;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.builders.SearchBuilder;
import net.chesstango.uci.engine.UciTango;
import net.chesstango.uci.proxy.UciProxy;

import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 */
public class EngineControllerFactory {
    public static EngineController createProxyController(String proxyName, Consumer<UciProxy> fnProxySetup) {
        UciProxy proxy = new UciProxy(ProxyConfigLoader.loadEngineConfig(proxyName));
        if (fnProxySetup != null) {
            fnProxySetup.accept(proxy);
        }
        return new EngineControllerImp(proxy);
    }

    public static <T extends GameEvaluator> EngineController createTangoControllerWithDefaultSearch(Class<T> gameEvaluatorClass) {
        TangoFactoryWithDefaultSearch<T> tangoFactory = new TangoFactoryWithDefaultSearch<>();
        tangoFactory.withGameEvaluatorClass(gameEvaluatorClass);

        return new EngineControllerImp(new UciTango(tangoFactory.build()))
                .overrideEngineName(gameEvaluatorClass.getSimpleName().toString());
    }

    public static <T extends SearchBuilder> EngineController createTangoControllerWithDefaultEvaluator(Class<T> searchBuilderClass, Consumer<T> fnSearchBuilderSetup) {
        TangoFactoryWithDefaultEvaluator<T> tangoFactory = new TangoFactoryWithDefaultEvaluator<>();
        tangoFactory.withSearchBuilderClass(searchBuilderClass);
        tangoFactory.withSearchBuilderCustomizer(fnSearchBuilderSetup);

        return new EngineControllerImp(new UciTango(tangoFactory.build()));
    }

}
