package net.chesstango.uci.engine;

import net.chesstango.goyeneche.UCIEngine;
import net.chesstango.goyeneche.requests.*;

/**
 * Represents a terminal state in the UCIEngine's state lifecycle.
 * In the context of the state design pattern, this class defines the behavior of the engine
 * when it has reached an end state, where further commands are either ignored or have no effect.
 * <p>
 * This ensures the engine adheres to a controlled and predictable state transition logic.
 *
 * @author Mauricio Coria
 */
class EndState implements UCIEngine {

    EndState() {
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
