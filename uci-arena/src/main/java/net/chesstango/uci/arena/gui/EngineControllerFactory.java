package net.chesstango.uci.arena.gui;

import net.chesstango.engine.Tango;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.DefaultSearchMove;
import net.chesstango.search.SearchMove;
import net.chesstango.uci.engine.UciTango;
import net.chesstango.uci.proxy.UciProxy;

import java.util.function.Consumer;
import java.util.function.Supplier;

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

    public static EngineController createTangoController(Supplier<SearchMove> searchMoveSupplier) {
        SearchMove search = searchMoveSupplier.get();

        return new EngineControllerImp(new UciTango(new Tango(searchMoveSupplier.get())))
                .overrideEngineName(search.getClass().getSimpleName());
    }

    public static EngineController createTangoControllerWithDefaultSearch(Supplier<GameEvaluator> gameEvaluatorSupplier) {
        GameEvaluator gameEvaluator = gameEvaluatorSupplier.get();

        SearchMove search = new DefaultSearchMove(gameEvaluator);

        return new EngineControllerImp(new UciTango(new Tango(search)))
                .overrideEngineName(gameEvaluator.getClass().getSimpleName());
    }

}
