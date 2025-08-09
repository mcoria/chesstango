package net.chesstango.uci.engine;

import lombok.Setter;
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


    WaitCmdGoState(UciTango uciTango) {
        super(uciTango);
        this.cmdGoExecutor = new ReqGoExecutor() {
            @Override
            public void go(ReqGoInfinite cmdGoInfinite) {
                uciTango.session.goInfinite();
            }

            @Override
            public void go(ReqGoDepth cmdGoDepth) {
                uciTango.session.goDepth(cmdGoDepth.getDepth());
            }

            @Override
            public void go(ReqGoTime cmdGoTime) {
                uciTango.session.goTime(cmdGoTime.getTimeOut());
            }

            @Override
            public void go(ReqGoFast cmdGoFast) {
                uciTango.session.goFast(cmdGoFast.getWTime(), cmdGoFast.getBTime(), cmdGoFast.getWInc(), cmdGoFast.getBInc());
            }
        };
    }

    @Override
    public void do_go(ReqGo cmdGo) {
        uciTango.session.setSearchListener(searchingState);

        uciTango.changeState(searchingState);

        cmdGo.execute(cmdGoExecutor);
    }
}
