package net.chesstango.uci.engine;

import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.responses.RspReadyOk;

/**
 * @author Mauricio Coria
 */
class WaitCmdGo implements UCIEngine {
    private final EngineTango engineTango;

    WaitCmdGo(EngineTango engineTango) {
        this.engineTango = engineTango;
    }

    @Override
    public void do_uci(CmdUci cmdUci) {
    }

    @Override
    public void do_setOption(CmdSetOption cmdSetOption) {

    }

    @Override
    public void do_isReady(CmdIsReady cmdIsReady) {
        engineTango.reply(new RspReadyOk());
    }

    @Override
    public void do_newGame(CmdUciNewGame cmdUciNewGame) {
    }


    @Override
    public void do_go(CmdGo cmdGo) {
        if (CmdGo.GoType.INFINITE.equals(cmdGo.getGoType())) {
            engineTango.tango.goInfinite();
        } else if (CmdGo.GoType.DEPTH.equals(cmdGo.getGoType())) {
            engineTango.tango.goDepth(cmdGo.getDepth());
        } else if (CmdGo.GoType.MOVE_TIME.equals(cmdGo.getGoType())) {
            engineTango.tango.goMoveTime(cmdGo.getTimeOut());
        } else {
            throw new RuntimeException("go subtype not implemented yet");
        }
        engineTango.currentState = engineTango.searchingState;
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
        engineTango.tango.setPosition(CmdPosition.CmdType.STARTPOS == cmdPosition.getType() ? FENDecoder.INITIAL_FEN : cmdPosition.getFen(), cmdPosition.getMoves());
    }
}
