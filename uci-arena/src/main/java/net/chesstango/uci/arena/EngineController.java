package net.chesstango.uci.arena;

import net.chesstango.uci.protocol.requests.CmdGo;
import net.chesstango.uci.protocol.requests.CmdPosition;
import net.chesstango.uci.protocol.responses.RspBestMove;

/**
 * @author Mauricio Coria
 *
 */
public interface EngineController {
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
