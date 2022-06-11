package chess.uci.ui;

import chess.uci.engine.Engine;
import chess.uci.protocol.requests.CmdUci;

public class EngineClient {
    private final Engine engine;

    private EngineClientState currentState;

    public EngineClient(Engine engine) {
        this.engine = engine;
    }

    public void startEngine() {
        CmdUci cmdUci = new CmdUci();
        cmdUci.execute(engine);

        currentState = new WaitRspUciOk();
        //wait for engine response
    }

    private interface EngineClientState {
    }

    private class WaitRspUciOk implements EngineClientState {
    }

}
