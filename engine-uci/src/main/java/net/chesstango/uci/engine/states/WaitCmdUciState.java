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
    public void do_newGame(CmdUciNewGame cmdUciNewGame) {
    }

    @Override
    public void do_uci(CmdUci cmdUci) {
        uciTango.reply(this, new RspId(RspId.RspIdType.NAME, String.format("%s %s", Tango.ENGINE_NAME, Tango.ENGINE_VERSION)));
        uciTango.reply(this, new RspId(RspId.RspIdType.AUTHOR, Tango.ENGINE_AUTHOR));
        uciTango.reply(readyState, new RspUciOk());
    }

    @Override
    public void do_setOption(CmdSetOption cmdSetOption) {
    }

    @Override
    public void do_isReady(CmdIsReady cmdIsReady) {
        uciTango.reply(this, new RspReadyOk());
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
