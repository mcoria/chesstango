package net.chesstango.uci.engine.states;

import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.requests.*;

/**
 * @author Mauricio Coria
 */
public class EndState implements UCIEngine {

    public EndState() {
    }

    @Override
    public void do_uci(CmdUci cmdUci) {
    }

    @Override
    public void do_setOption(CmdSetOption cmdSetOption) {
    }

    @Override

    public void do_newGame(CmdUciNewGame cmdUciNewGame) {

    }

    @Override
    public void do_isReady(CmdIsReady cmdIsReady) {
    }

    @Override
    public void do_go(CmdGo cmdGo) {
    }

    @Override
    public void do_stop(CmdStop cmdStop) {
    }

    @Override
    public void do_quit(CmdQuit cmdQuit) {
    }

    @Override
    public void do_position(CmdPosition cmdPosition) {
    }
}
