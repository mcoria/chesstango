package chess.uci.ui;

import chess.uci.engine.Engine;
import chess.uci.engine.EngineProxy;
import chess.uci.engine.EngineZonda;
import chess.uci.protocol.requests.CmdUci;

public class Main {

    private Engine engine1;
    private Engine engine2;

    public Main(){
        engine1 = new EngineZonda();
        engine2 = new EngineProxy();
    }

    public void compete(){
        startEngines();
    }

    private void startEngines() {
        startEngine(engine1);
        startEngine(engine2);
    }

    private void startEngine(Engine engine) {
        CmdUci cmdUci = new CmdUci();
        cmdUci.execute(engine);

        //whit for engine response
    }

}
