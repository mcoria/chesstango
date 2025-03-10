package net.chesstango.uci.engine.states;

import lombok.Setter;
import net.chesstango.engine.Tango;
import net.chesstango.uci.engine.UciTango;
import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.responses.RspId;
import net.chesstango.uci.protocol.responses.RspReadyOk;
import net.chesstango.uci.protocol.responses.RspUciOk;

/**
 * @author Mauricio Coria
 */
public class WaitCmdUciState implements UCIEngine {
    private final UciTango uciTango;

    @Setter
    private ReadyState readyState;

    public WaitCmdUciState(UciTango uciTango) {
        this.uciTango = uciTango;
    }

    @Override
    public void do_newGame(ReqUciNewGame cmdUciNewGame) {
    }

    @Override
    public void do_uci(ReqUci cmdUci) {
        uciTango.reply(this, new RspId(RspId.RspIdType.NAME, String.format("%s %s", Tango.ENGINE_NAME, Tango.ENGINE_VERSION)));
        uciTango.reply(this, new RspId(RspId.RspIdType.AUTHOR, Tango.ENGINE_AUTHOR));
        uciTango.reply(readyState, new RspUciOk());
    }

    @Override
    public void do_setOption(ReqSetOption cmdSetOption) {
    }

    @Override
    public void do_isReady(ReqIsReady cmdIsReady) {
        uciTango.reply(this, new RspReadyOk());
    }

    @Override
    public void do_go(ReqGo cmdGo) {
    }

    @Override
    public void do_stop(ReqStop cmdStop) {
    }

    @Override
    public void do_quit(ReqQuit cmdQuit) {
        uciTango.changeState(new EndState());
    }

    @Override
    public void do_position(ReqPosition cmdPosition) {
    }
}
