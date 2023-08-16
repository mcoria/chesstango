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
    private final UciTango uciTango;

    protected  WaitCmdUci(UciTango uciTango) {
        this.uciTango = uciTango;
    }

    @Override
    public void do_newGame(CmdUciNewGame cmdUciNewGame) {
    }

    @Override
    public void do_uci(CmdUci cmdUci) {
        uciTango.reply(new RspId(RspId.RspIdType.NAME, ENGINE_NAME));
        uciTango.reply(new RspId(RspId.RspIdType.AUTHOR, ENGINE_AUTHOR));
        uciTango.reply(new RspUciOk());
        uciTango.currentState = uciTango.readyState;
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
        uciTango.close();
    }

    @Override
    public void do_position(CmdPosition cmdPosition) {

    }
}
