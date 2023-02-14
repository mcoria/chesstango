package net.chesstango.evaluation.tunning;

import net.chesstango.uci.arena.EngineController;
import net.chesstango.uci.arena.imp.EngineControllerImp;
import net.chesstango.uci.engine.EngineProxy;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * @author Mauricio Coria
 */
public class EngineControllerProxyFactory extends BasePooledObjectFactory<EngineController> {

    @Override
    public EngineController create() {
        EngineProxy coreEngineProxy = new EngineProxy();
        //coreEngineProxy.setLogging(true);
        EngineController engineProxy = new EngineControllerImp(coreEngineProxy);
        engineProxy.send_CmdUci();
        engineProxy.send_CmdIsReady();

        return engineProxy;
    }

    @Override
    public PooledObject<EngineController> wrap(EngineController engineController) {
        return new DefaultPooledObject<EngineController>(engineController);
    }

    @Override
    public void destroyObject(PooledObject<EngineController> pooledController) {
        pooledController.getObject().send_CmdQuit();
    }

}
