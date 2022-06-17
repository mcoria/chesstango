package chess.uci.ui;

import chess.uci.protocol.requests.CmdGo;
import chess.uci.protocol.requests.CmdPosition;
import chess.uci.protocol.responses.RspBestMove;

public interface EngineClient {
    void send_CmdUci();

    void send_CmdIsReady();

    void send_CmdUciNewGame();

    void send_CmdPosition(CmdPosition cmdPosition);

    RspBestMove send_CmdGo(CmdGo cmdGo);

    void send_CmdStop();

    void send_CmdQuit();

    String getEngineName();

    String getEngineAuthor();
}
