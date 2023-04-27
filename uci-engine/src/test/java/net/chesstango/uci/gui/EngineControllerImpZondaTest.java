package net.chesstango.uci.gui;

import net.chesstango.uci.engine.EngineTango;
import net.chesstango.uci.protocol.requests.CmdGo;
import net.chesstango.uci.protocol.requests.CmdPosition;
import net.chesstango.uci.protocol.responses.RspBestMove;
import net.chesstango.uci.proxy.EngineProxy;
import net.chesstango.uci.proxy.ProxyConfig;
import net.chesstango.uci.service.Service;




/**
 * @author Mauricio Coria
 */
public class EngineControllerImpZondaTest {

    @Test
    public void test_Zonda() {
        Service service = new EngineTango();

        EngineControllerImp client = new EngineControllerImp(service);

        client.send_CmdUci();

        assertEquals("Mauricio Coria", client.getEngineAuthor());
        assertEquals("Tango", client.getEngineName());

        client.send_CmdIsReady();

        client.send_CmdUciNewGame();

        client.send_CmdPosition(new CmdPosition());

        RspBestMove bestmove = client.send_CmdGo(new CmdGo()
                .setGoType(CmdGo.GoType.DEPTH)
                .setDepth(1));

        assertNotNull(bestmove);

        client.send_CmdQuit();
    }



}
