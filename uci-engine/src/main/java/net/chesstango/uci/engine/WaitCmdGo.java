package net.chesstango.uci.engine;

import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.uci.protocol.requests.CmdGo;
import net.chesstango.uci.protocol.requests.CmdIsReady;
import net.chesstango.uci.protocol.requests.CmdPosition;
import net.chesstango.uci.protocol.requests.CmdUci;
import net.chesstango.uci.protocol.responses.RspReadyOk;

/**
 * @author Mauricio Coria
 */
class WaitCmdGo implements TangoState {
    private final EngineTango engineTango;

    WaitCmdGo(EngineTango engineTango) {
        this.engineTango = engineTango;
    }

    @Override
    public void do_uci(CmdUci cmdUci) {
    }

    @Override
    public void do_isReady(CmdIsReady cmdIsReady) {
        engineTango.responseOutputStream.accept(new RspReadyOk());
    }

    @Override
    public void do_go(CmdGo cmdGo) {
        Searching searching = new Searching(engineTango);
        engineTango.currentState = searching;
        searching.search(cmdGo);
    }

    @Override
    public void do_stop() {
    }

    @Override
    public void do_position(CmdPosition cmdPosition) {
        engineTango.tango.setPosition(CmdPosition.CmdType.STARTPOS == cmdPosition.getType() ? FENDecoder.INITIAL_FEN : cmdPosition.getFen(), cmdPosition.getMoves());
    }
}
