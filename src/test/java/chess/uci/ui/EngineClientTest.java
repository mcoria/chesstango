package chess.uci.ui;

import chess.uci.engine.Engine;
import chess.uci.engine.EngineZonda;
import org.junit.Assert;
import org.junit.Test;

public class EngineClientTest {

    @Test
    public void test1(){
        Engine engine = null; // new EngineZonda();

        EngineClient client = new EngineClient(engine);

        client.send_CmdUci();

        Assert.assertEquals("Mauricio Coria", client.getEngineAuthor());
        Assert.assertEquals("Zonda", client.getEngineName());

        client.send_CmdIsReady();
        client.send_CmdUciNewGame();


    }
}
