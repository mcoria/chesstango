package net.chesstango.ai.imp.smart.evaluation.tunning;

import net.chesstango.uci.arbiter.EngineController;
import net.chesstango.uci.arbiter.imp.EngineControllerImp;
import net.chesstango.uci.engine.imp.EngineProxy;
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
