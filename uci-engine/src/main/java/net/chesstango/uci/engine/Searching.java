package net.chesstango.uci.engine;

import net.chesstango.uci.protocol.requests.CmdGo;
import net.chesstango.uci.protocol.requests.CmdIsReady;
import net.chesstango.uci.protocol.requests.CmdPosition;
import net.chesstango.uci.protocol.requests.CmdUci;
import net.chesstango.uci.protocol.responses.RspBestMove;

/**
 * @author Mauricio Coria
 */
class Searching implements TangoState {
    private final EngineTango engineTango;

    Searching(EngineTango engineTango) {
        this.engineTango = engineTango;
    }

    @Override
    public void do_uci(CmdUci cmdUci) {
    }

    @Override
    public void do_isReady(CmdIsReady cmdIsReady) {
    }

    @Override
    public void do_go(CmdGo cmdGo) {
    }

    @Override
    public void do_stop() {
        engineTango.tango.stopSearching();
    }

    @Override
    public void do_position(CmdPosition cmdPosition) {
    }

    public void search(CmdGo cmdGo) {
        if (CmdGo.GoType.INFINITE.equals(cmdGo.getGoType())) {
            engineTango.tango.goInfinite();
        } else if (CmdGo.GoType.DEPTH.equals(cmdGo.getGoType())) {
            engineTango.tango.goDepth(cmdGo.getDepth());
        } else if (CmdGo.GoType.MOVE_TIME.equals(cmdGo.getGoType())) {
            engineTango.tango.goMoveTime(cmdGo.getTimeOut());
        } else {
            throw new RuntimeException("go subtype not implemented yet");
        }
    }
}
