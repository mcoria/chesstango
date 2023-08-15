package net.chesstango.uci.engine;

import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.responses.RspId;
import net.chesstango.uci.protocol.responses.RspUciOk;

/**
 * @author Mauricio Coria
 */
public class WaitCmdUci implements UCIEngine {
    public static final String ENGINE_NAME = "Tango";
    public static final String ENGINE_AUTHOR = "Mauricio Coria";
    private final EngineTango engineTango;

    protected  WaitCmdUci(EngineTango engineTango) {
        this.engineTango = engineTango;
    }

    @Override
    public void do_newGame(CmdUciNewGame cmdUciNewGame) {
    }

    @Override
    public void do_uci(CmdUci cmdUci) {
        engineTango.reply(new RspId(RspId.RspIdType.NAME, ENGINE_NAME));
        engineTango.reply(new RspId(RspId.RspIdType.AUTHOR, ENGINE_AUTHOR));
        engineTango.reply(new RspUciOk());
        engineTango.currentState = engineTango.readyState;
    }

    @Override
    public void do_setOption(CmdSetOption cmdSetOption) {

    }

    @Override
    public void do_isReady(CmdIsReady cmdIsReady) {
    }

    @Override
    public void do_go(CmdGo cmdGo) {

    }

    @Override
    public void do_stop(CmdStop cmdStop) {

    }

    @Override
    public void do_quit(CmdQuit cmdQuit) {
        engineTango.close();
    }

    @Override
    public void do_position(CmdPosition cmdPosition) {

    }
}
