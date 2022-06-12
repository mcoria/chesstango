package chess.uci.ui;

public interface EngineClientRequestSender {
    void send_CmdUci();

    void send_CmdIsReady();

    void send_CmdUciNewGame();

    void send_CmdPosition();

    void send_CmdGo();

    void send_CmdStop();

    void send_CmdQuit();
}
