package net.chesstango.uci.gui;

import net.chesstango.uci.engine.EngineTango;
import net.chesstango.uci.protocol.requests.CmdGo;
import net.chesstango.uci.protocol.requests.CmdPosition;
import net.chesstango.uci.protocol.responses.RspBestMove;
import net.chesstango.uci.proxy.EngineProxy;
import net.chesstango.uci.proxy.ProxyConfig;
import net.chesstango.uci.service.Service;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author Mauricio Coria
 */
public class EngineControllerImpProxyTest {

    @Test
    public void test_Proxy() {
        EngineProxy engine = new EngineProxy(ProxyConfig.loadEngineConfig("Spike")).setLogging(true);

        EngineControllerImp client = new EngineControllerImp(engine);

        client.send_CmdUci();

        //Assert.assertEquals("Ralf Schäfer und Volker Böhm", client.getEngineAuthor());
        Assert.assertEquals("Spike 1.4", client.getEngineName());

        client.send_CmdIsReady();

        client.send_CmdUciNewGame();

        client.send_CmdPosition(new CmdPosition());

        RspBestMove bestmove = client.send_CmdGo(new CmdGo()
                .setGoType(CmdGo.GoType.DEPTH)
                .setDepth(1));

        Assert.assertNotNull(bestmove);

        client.send_CmdQuit();
    }


}
