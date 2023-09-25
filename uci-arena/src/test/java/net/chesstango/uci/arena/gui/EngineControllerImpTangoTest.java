package net.chesstango.uci.arena.gui;

import net.chesstango.uci.Service;
import net.chesstango.uci.engine.UciTango;
import net.chesstango.uci.protocol.requests.CmdPosition;
import net.chesstango.uci.protocol.requests.go.CmdGoDepth;
import net.chesstango.uci.protocol.responses.RspBestMove;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


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
        assertTrue(client.getEngineName().startsWith("Tango"));

        client.send_CmdIsReady();

        client.send_CmdUciNewGame();

        client.send_CmdPosition(new CmdPosition());

        RspBestMove bestmove = client.send_CmdGo(new CmdGoDepth().setDepth(1));

        assertNotNull(bestmove);

        client.send_CmdQuit();
    }


}
