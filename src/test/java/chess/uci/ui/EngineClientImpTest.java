package chess.uci.ui;

import chess.uci.engine.Engine;
import chess.uci.engine.EngineProxy;
import chess.uci.engine.EngineZonda;
import chess.uci.protocol.requests.CmdGo;
import chess.uci.protocol.requests.CmdPosition;
import chess.uci.protocol.responses.RspBestMove;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EngineClientImpTest {

    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Test
    public void test_Zonda(){
        Engine engine = new EngineZonda();

        EngineClientImp client = new EngineClientImp(engine);

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

        EngineClientImp client = new EngineClientImp(engine);

        executorService.execute(engine);

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

        executorServiceShutdown();

    }

    private void executorServiceShutdown() {
        executorService.shutdown();
        try {
            while(!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                //System.out.println("Engine still executing");
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }


}
