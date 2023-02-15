package net.chesstango.uci.arena.imp;

import net.chesstango.uci.engine.Engine;
import net.chesstango.uci.engine.EngineProxy;
import net.chesstango.uci.engine.EngineTango;
import net.chesstango.uci.protocol.requests.CmdGo;
import net.chesstango.uci.protocol.requests.CmdPosition;
import net.chesstango.uci.protocol.responses.RspBestMove;
import org.junit.Assert;
import org.junit.Test;


public class EngineControllerImpTest {

    @Test
    public void test_Zonda(){
        Engine engine = new EngineTango();

        EngineControllerImp client = new EngineControllerImp(engine);

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

    @Test
    public void test_Proxy(){
        EngineProxy engine = new EngineProxy();

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

        System.out.println(bestmove.toString());

        client.send_CmdQuit();

    }


}
