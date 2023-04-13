package net.chesstango.uci.arena;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.DefaultSearchMove;
import net.chesstango.uci.engine.EngineTango;
import net.chesstango.uci.gui.EngineController;
import net.chesstango.uci.gui.EngineControllerImp;
import net.chesstango.uci.proxy.EngineProxy;
import net.chesstango.uci.proxy.ProxyConfig;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 */
public class EngineControllerFactory extends BasePooledObjectFactory<EngineController> {
    private final Supplier<EngineController> fnCreateEngineController;

    private final List<EngineController> engineControllers = Collections.synchronizedList(new ArrayList<>());

    public EngineControllerFactory(Supplier<EngineController> fnCreateEngineController) {
        this.fnCreateEngineController = fnCreateEngineController;
    }

    @Override
    public EngineController create() {

        EngineController controller = fnCreateEngineController.get();

        controller.startEngine();

        engineControllers.add(controller);

        return controller;
    }

    @Override
    public PooledObject<EngineController> wrap(EngineController engineController) {
        return new DefaultPooledObject<EngineController>(engineController);
    }

    @Override
    public void destroyObject(PooledObject<EngineController> pooledController) {
        pooledController.getObject().send_CmdQuit();
    }

    @Override
    public void activateObject(PooledObject<EngineController> pooledController) throws Exception {
        pooledController.getObject().send_CmdIsReady();
    }

    public List<EngineController> getCreatedEngineControllers() {
        return engineControllers;
    }

    public static EngineControllerImp createProxyController(String proxyName, Consumer<EngineProxy> fnProxySetup) {
        EngineProxy proxy = new EngineProxy(ProxyConfig.loadEngineConfig(proxyName));
        if (fnProxySetup != null) {
            fnProxySetup.accept(proxy);
        }
        return new EngineControllerImp(proxy);
    }

    public static EngineControllerImp createTangoController(Class<? extends GameEvaluator> gameEvaluatorClass) {
        EngineTango tango = createEngineTango(gameEvaluatorClass);

        return new EngineControllerImp(tango)
                .overrideEngineName(gameEvaluatorClass.getSimpleName());
    }

    private static EngineTango createEngineTango(Class<? extends GameEvaluator> gameEvaluatorClass) {
        DefaultSearchMove search = new DefaultSearchMove();
        try {
            search.setGameEvaluator(gameEvaluatorClass.getDeclaredConstructor().newInstance());
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return new EngineTango(search);
    }
}
