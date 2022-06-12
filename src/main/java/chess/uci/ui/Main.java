package chess.uci.ui;

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
        engine1.send_CmdUci();
        engine1.send_CmdIsReady();
        engine1.send_CmdUciNewGame();
        engine1.send_CmdPosition();
        engine1.send_CmdGo();
        engine1.send_CmdStop();
        engine1.send_CmdQuit();

    }



}
