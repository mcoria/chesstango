package net.chesstango.uci.arena;

import net.chesstango.uci.gui.EngineController;
import net.chesstango.uci.gui.EngineControllerImp;
import net.chesstango.uci.service.Service;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 */
public class EngineControllerFactory extends BasePooledObjectFactory<EngineController> {
    private final Supplier<EngineController> fnCreateEngineController;

    private final List<EngineController> engineControllers = Collections.synchronizedList(new ArrayList<>()) ;

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

    public List<EngineController> getEngineControllers() {
        return engineControllers;
    }
}
