package chess.uci.ui;

import chess.uci.protocol.requests.CmdPosition;

public class Main {

    private EngineClient engineClient1;
    private EngineClient engineClient2;

    public Main(){
        //engine1 = new EngineClient();
        //engine2 = new EngineProxy();
    }

    public void compete(){
        startEngines();
    }

    private void startEngines() {
        engineClient1.send_CmdUci();
        engineClient1.send_CmdIsReady();
        engineClient1.send_CmdUciNewGame();
        engineClient1.send_CmdPosition(new CmdPosition());
        //engine1.send_CmdGo();
        engineClient1.send_CmdStop();
        engineClient1.send_CmdQuit();

    }



}
