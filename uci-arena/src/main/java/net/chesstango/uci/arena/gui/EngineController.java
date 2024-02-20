package net.chesstango.uci.arena.gui;

import net.chesstango.uci.protocol.requests.CmdGo;
import net.chesstango.uci.protocol.requests.CmdPosition;
import net.chesstango.uci.protocol.responses.RspBestMove;
import net.chesstango.uci.ServiceElement;

/**
 * @author Mauricio Coria
 */
public interface EngineController extends ServiceElement {

    void send_CmdUci();

    void send_CmdIsReady();

    void send_CmdUciNewGame();

    void send_CmdPosition(CmdPosition cmdPosition);

    RspBestMove send_CmdGo(CmdGo cmdGo);

    void send_CmdStop();

    void send_CmdQuit();

    default void startEngine() {
        send_CmdUci();
        send_CmdIsReady();
    }

    default void startNewGame() {
        send_CmdUciNewGame();
        send_CmdIsReady();
    }

    String getEngineName();

    String getEngineAuthor();

    EngineControllerImp overrideEngineName(String name);

    EngineController overrideCmdGo(CmdGo cmdGo);
}
