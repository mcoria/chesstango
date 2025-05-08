package net.chesstango.uci.engine;

import lombok.Setter;
import net.chesstango.engine.Tango;
import net.chesstango.goyeneche.requests.*;

/**
 * The WaitCmdGoState class represents a specific state in the State design pattern implementation for the UCI engine workflow in a chess engine.
 * It handles the "go" command and transitions the UCI engine to the SearchingState after processing the command.
 *
 * @author Mauricio Coria
 */
class WaitCmdGoState extends ReadyState {
    private final ReqGoExecutor cmdGoExecutor;

    @Setter
    private SearchingState searchingState;


    WaitCmdGoState(UciTango uciTango, Tango tango) {
        super(uciTango, tango);
        this.cmdGoExecutor = new ReqGoExecutor() {
            @Override
            public void go(ReqGoInfinite cmdGoInfinite) {
                tango.goInfinite();
            }

            @Override
            public void go(ReqGoDepth cmdGoDepth) {
                tango.goDepth(cmdGoDepth.getDepth());
            }

            @Override
            public void go(ReqGoTime cmdGoTime) {
                tango.goTime(cmdGoTime.getTimeOut());
            }

            @Override
            public void go(ReqGoFast cmdGoFast) {
                tango.goFast(cmdGoFast.getWTime(), cmdGoFast.getBTime(), cmdGoFast.getWInc(), cmdGoFast.getBInc());
            }
        };
    }

    @Override
    public void do_go(ReqGo cmdGo) {
        cmdGo.execute(cmdGoExecutor);
        uciTango.changeState(searchingState);
    }
}
