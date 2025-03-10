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
    public void do_uci(ReqUci cmdUci) {
    }

    @Override
    public void do_setOption(ReqSetOption cmdSetOption) {
    }

    @Override

    public void do_newGame(ReqUciNewGame cmdUciNewGame) {

    }

    @Override
    public void do_isReady(ReqIsReady cmdIsReady) {
    }

    @Override
    public void do_go(ReqGo cmdGo) {
    }

    @Override
    public void do_stop(ReqStop cmdStop) {
    }

    @Override
    public void do_quit(ReqQuit cmdQuit) {
    }

    @Override
    public void do_position(ReqPosition cmdPosition) {
    }
}
