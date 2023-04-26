package net.chesstango.uci.gui;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.builders.SearchBuilder;
import net.chesstango.uci.engine.builders.TangoFactory;
import net.chesstango.uci.engine.builders.TangoFactoryWithDefaultEvaluator;
import net.chesstango.uci.engine.builders.TangoFactoryWithDefaultSearch;
import net.chesstango.uci.proxy.EngineProxy;
import net.chesstango.uci.proxy.ProxyConfig;

import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 */
public class EngineControllerFactory {
    public static EngineControllerImp createProxyController(String proxyName, Consumer<EngineProxy> fnProxySetup) {
        EngineProxy proxy = new EngineProxy(ProxyConfig.loadEngineConfig(proxyName));
        if (fnProxySetup != null) {
            fnProxySetup.accept(proxy);
        }
        return new EngineControllerImp(proxy);
    }

    public static EngineControllerImp createTangoController(TangoFactory tangoFactory) {
        return new EngineControllerImp(tangoFactory.build());
    }

    public static EngineControllerImp createTangoControllerWithDefaultSearch(Class<? extends GameEvaluator> gameEvaluatorClass) {
        TangoFactoryWithDefaultSearch tangoFactory = new TangoFactoryWithDefaultSearch();
        tangoFactory.withGameEvaluatorClass(gameEvaluatorClass);

        return new EngineControllerImp(tangoFactory.build())
                    .overrideEngineName(gameEvaluatorClass.getSimpleName().toString());
    }

    public static <T extends SearchBuilder> EngineControllerImp createTangoControllerWithDefaultEvaluator(Class<T> searchBuilderClass, Consumer<T> fnSearchBuilderSetup) {
        TangoFactoryWithDefaultEvaluator tangoFactory = new TangoFactoryWithDefaultEvaluator();
        tangoFactory.withSearchBuilderClass(searchBuilderClass);
        tangoFactory.withSearchBuilderCustomizer(fnSearchBuilderSetup);

        return new EngineControllerImp(tangoFactory.build());
    }

}
