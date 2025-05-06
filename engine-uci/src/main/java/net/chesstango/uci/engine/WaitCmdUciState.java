package net.chesstango.uci.engine;

import lombok.Setter;
import net.chesstango.engine.Tango;
import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.responses.RspId;
import net.chesstango.uci.protocol.responses.RspOption;
import net.chesstango.uci.protocol.responses.RspReadyOk;
import net.chesstango.uci.protocol.responses.RspUciOk;

import static net.chesstango.uci.engine.ReadyState.POLYGLOT_PATH;

/**
 * The WaitCmdUciState class is part of the State design pattern implementation for the UCI engine.
 * It represents the state where the engine is waiting for the initial "uci" command from the client.
 * This state handles the "uci" command to provide engine identification and transitions to the
 * ReadyState upon completion.
 *
 * @author Mauricio Coria
 */
class WaitCmdUciState implements UCIEngine {
    private final UciTango uciTango;

    @Setter
    private ReadyState readyState;

    WaitCmdUciState(UciTango uciTango) {
        this.uciTango = uciTango;
    }

    @Override
    public void do_newGame(ReqUciNewGame cmdUciNewGame) {
    }

    @Override
    public void do_uci(ReqUci cmdUci) {
        uciTango.reply(this, new RspId(RspId.RspIdType.NAME, String.format("%s %s", Tango.ENGINE_NAME, Tango.ENGINE_VERSION)));
        uciTango.reply(this, new RspId(RspId.RspIdType.AUTHOR, Tango.ENGINE_AUTHOR));
        uciTango.reply(this, RspOption.createStringOption(POLYGLOT_PATH, null));
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
