package net.chesstango.uci.engine;

import net.chesstango.uci.protocol.requests.CmdGo;
import net.chesstango.uci.protocol.requests.CmdIsReady;
import net.chesstango.uci.protocol.requests.CmdPosition;
import net.chesstango.uci.protocol.requests.CmdUci;
import net.chesstango.uci.protocol.responses.RspId;
import net.chesstango.uci.protocol.responses.RspUciOk;

/**
 * @author Mauricio Coria
 */
public class WaitCmdUci implements TangoState {
    public static final String ENGINE_NAME = "Tango";
    public static final String ENGINE_AUTHOR = "Mauricio Coria";
    private final EngineTango engineTango;

    public WaitCmdUci(EngineTango engineTango) {
        this.engineTango = engineTango;
    }

    @Override
    public void do_uci(CmdUci cmdUci) {
        engineTango.responseOutputStream.accept(new RspId(RspId.RspIdType.NAME, ENGINE_NAME));
        engineTango.responseOutputStream.accept(new RspId(RspId.RspIdType.AUTHOR, ENGINE_AUTHOR));
        engineTango.responseOutputStream.accept(new RspUciOk());
        engineTango.currentState = new Ready(engineTango);
    }

    @Override
    public void do_isReady(CmdIsReady cmdIsReady) {
    }

    @Override
    public void do_go(CmdGo cmdGo) {

    }

    @Override
    public void do_stop() {
    }

    @Override
    public void do_position(CmdPosition cmdPosition) {

    }
}
