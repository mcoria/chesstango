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
public class EngineControllerImpZondaTest {

    @Test
    public void test_Zonda() {
        Service service = new EngineTango();

        EngineControllerImp client = new EngineControllerImp(service);

        client.send_CmdUci();

        Assert.assertEquals("Mauricio Coria", client.getEngineAuthor());
        Assert.assertEquals("Tango", client.getEngineName());

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
