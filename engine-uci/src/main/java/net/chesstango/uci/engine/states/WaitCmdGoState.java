package net.chesstango.uci.engine.states;

import lombok.Setter;
import net.chesstango.engine.Tango;
import net.chesstango.uci.engine.UciTango;
import net.chesstango.uci.protocol.requests.ReqGo;
import net.chesstango.uci.protocol.requests.ReqGoExecutor;
import net.chesstango.uci.protocol.requests.go.ReqGoDepth;
import net.chesstango.uci.protocol.requests.go.ReqGoFast;
import net.chesstango.uci.protocol.requests.go.ReqGoInfinite;
import net.chesstango.uci.protocol.requests.go.ReqGoTime;

/**
 * @author Mauricio Coria
 */
public class WaitCmdGoState extends ReadyState {
    private final ReqGoExecutor cmdGoExecutor;

    @Setter
    private SearchingState searchingState;


    public WaitCmdGoState(UciTango uciTango, Tango tango) {
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
