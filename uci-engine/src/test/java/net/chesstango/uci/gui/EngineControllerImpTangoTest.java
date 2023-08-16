package net.chesstango.uci.gui;

import net.chesstango.uci.engine.UciTango;
import net.chesstango.uci.protocol.requests.CmdGo;
import net.chesstango.uci.protocol.requests.CmdPosition;
import net.chesstango.uci.protocol.responses.RspBestMove;
import net.chesstango.uci.service.Service;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * @author Mauricio Coria
 */
public class EngineControllerImpTangoTest {

    @Test
    public void test_Tango() {
        Service service = new UciTango();

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