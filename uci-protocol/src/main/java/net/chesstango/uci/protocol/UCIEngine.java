package net.chesstango.uci.protocol;

import net.chesstango.uci.protocol.requests.*;

/**
 * @author Mauricio Coria
 */
public interface UCIEngine {
    void do_uci(CmdUci cmdUci);

    void do_setOption(CmdSetOption cmdSetOption);

    void do_isReady(CmdIsReady cmdIsReady);

    void do_newGame(CmdUciNewGame cmdUciNewGame);

    void do_position(CmdPosition cmdPosition);

    void do_go(CmdGo cmdGo);

    void do_stop(CmdStop cmdStop);

    void do_quit(CmdQuit cmdQuit);
}
