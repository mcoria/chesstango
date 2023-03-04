package net.chesstango.uci.arena;

import net.chesstango.uci.gui.EngineController;
import net.chesstango.uci.gui.EngineControllerImp;
import net.chesstango.uci.service.Service;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 */
public class EngineControllerFactory extends BasePooledObjectFactory<EngineController> {
    private final Supplier<Service> fnCreateService;

    private final List<EngineController> engineControllers = new ArrayList<>();

    public EngineControllerFactory(Supplier<Service> fnCreateService) {
        this.fnCreateService = fnCreateService;
    }

    @Override
    public EngineController create() {
        Service coreEngineProxy = fnCreateService.get();

        EngineController controller = new EngineControllerImp(coreEngineProxy);

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

    public List<EngineController> getEngineControllers() {
        return engineControllers;
    }
}
