package net.chesstango.uci.engine.states;

import lombok.Setter;
import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.engine.Tango;
import net.chesstango.uci.engine.UciTango;
import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.requests.go.CmdGoDepth;
import net.chesstango.uci.protocol.requests.go.CmdGoFast;
import net.chesstango.uci.protocol.requests.go.CmdGoInfinite;
import net.chesstango.uci.protocol.requests.go.CmdGoTime;
import net.chesstango.uci.protocol.responses.RspReadyOk;

/**
 * @author Mauricio Coria
 */
public class WaitCmdGoState extends ReadyState {
    private final CmdGoExecutor cmdGoExecutor;

    @Setter
    private SearchingState searchingState;


    public WaitCmdGoState(UciTango uciTango, Tango tango) {
        super(uciTango, tango);
        this.cmdGoExecutor = new CmdGoExecutor() {
            @Override
            public void go(CmdGoInfinite cmdGoInfinite) {
                tango.goInfinite();
            }

            @Override
            public void go(CmdGoDepth cmdGoDepth) {
                tango.goDepth(cmdGoDepth.getDepth());
            }

            @Override
            public void go(CmdGoTime cmdGoTime) {
                tango.goTime(cmdGoTime.getTimeOut());
            }

            @Override
            public void go(CmdGoFast cmdGoFast) {
                tango.goFast(cmdGoFast.getWTime(), cmdGoFast.getBTime(), cmdGoFast.getWInc(), cmdGoFast.getBInc());
            }
        };
    }

    @Override
    public void do_go(CmdGo cmdGo) {
        cmdGo.go(cmdGoExecutor);
        uciTango.changeState(searchingState);
    }
}
