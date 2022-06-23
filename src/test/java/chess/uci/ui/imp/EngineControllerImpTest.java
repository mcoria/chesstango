package chess.uci.ui.imp;

import chess.uci.engine.Engine;
import chess.uci.engine.imp.EngineProxy;
import chess.uci.engine.imp.EngineZonda;
import chess.uci.protocol.requests.CmdGo;
import chess.uci.protocol.requests.CmdPosition;
import chess.uci.protocol.responses.RspBestMove;
import org.junit.Assert;
import org.junit.Test;


public class EngineControllerImpTest {

    @Test
    public void test_Zonda(){
        Engine engine = new EngineZonda();

        EngineControllerImp client = new EngineControllerImp(engine);

        client.send_CmdUci();

        Assert.assertEquals("Mauricio Coria", client.getEngineAuthor());
        Assert.assertEquals("Zonda", client.getEngineName());

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
