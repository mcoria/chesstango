package net.chesstango.uci.arena;

import net.chesstango.uci.arena.gui.EngineController;
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
public class EngineControllerPoolFactory extends BasePooledObjectFactory<EngineController> {
    private final Supplier<EngineController> fnCreateEngineController;

    private final List<EngineController> engineControllerInstances = Collections.synchronizedList(new ArrayList<>());

    public EngineControllerPoolFactory(Supplier<EngineController> fnCreateEngineController) {
        this.fnCreateEngineController = fnCreateEngineController;
    }

    @Override
    public EngineController create() {

        EngineController controller = fnCreateEngineController.get();

        controller.startEngine();

        engineControllerInstances.add(controller);

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

    /**
     * Devuleve la coleccion de instancias EngineController creadas.
     * @return
     */
    public List<EngineController> getEngineControllerInstances() {
        return engineControllerInstances;
    }
}
