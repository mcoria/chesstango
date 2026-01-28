package net.chesstango.uci.engine;

import lombok.Setter;
import net.chesstango.goyeneche.UCIEngine;
import net.chesstango.goyeneche.requests.*;

/**
 * The WaitCmdGoState class represents a specific state in the State design pattern implementation for the UCI engine workflow in a chess engine.
 * It handles the "go" command and transitions the UCI engine to the SearchingState after processing the command.
 *
 * @author Mauricio Coria
 */
class WaitCmdGoState implements UCIEngine {
    private final ReqGoExecutor cmdGoExecutor;

    private final UciTango uciTango;

    @Setter
    private SearchingState searchingState;


    WaitCmdGoState(UciTango uciTango) {
        this.cmdGoExecutor = new ReqGoExecutor() {
            @Override
            public void go(ReqGoInfinite cmdGoInfinite) {
                uciTango.goInfinite();
            }

            @Override
            public void go(ReqGoDepth cmdGoDepth) {
                uciTango.goDepth(cmdGoDepth.getDepth());
            }

            @Override
            public void go(ReqGoTime cmdGoTime) {
                uciTango.goTime(cmdGoTime.getTimeOut());
            }

            @Override
            public void go(ReqGoFast cmdGoFast) {
                uciTango.goFast(cmdGoFast.getWTime(), cmdGoFast.getBTime(), cmdGoFast.getWInc(), cmdGoFast.getBInc());
            }
        };

        this.uciTango = uciTango;
    }

    @Override
    public void do_go(ReqGo cmdGo) {
        uciTango.setSessionSearchListener(searchingState);

        uciTango.changeState(searchingState);

        cmdGo.execute(cmdGoExecutor);
    }

    @Override
    public void do_quit(ReqQuit cmdQuit) {
        uciTango.changeState(new EndState());
    }
}
