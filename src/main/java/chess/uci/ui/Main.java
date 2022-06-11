package chess.uci.ui;

import chess.uci.engine.Engine;
import chess.uci.engine.EngineProxy;
import chess.uci.engine.EngineZonda;
import chess.uci.protocol.requests.CmdUci;

public class Main {

    private EngineClient engine1;
    private EngineClient engine2;

    public Main(){
        //engine1 = new EngineZonda();
        //engine2 = new EngineProxy();
    }

    public void compete(){
        startEngines();
    }

    private void startEngines() {
        engine1.startEngine();
    }



}
