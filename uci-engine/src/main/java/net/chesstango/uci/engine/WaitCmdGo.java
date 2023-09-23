package net.chesstango.uci.engine;

import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.responses.RspReadyOk;

/**
 * @author Mauricio Coria
 */
class WaitCmdGo implements UCIEngine {
    private final UciTango uciTango;

    protected WaitCmdGo(UciTango uciTango) {
        this.uciTango = uciTango;
    }

    @Override
    public void do_uci(CmdUci cmdUci) {
    }

    @Override
    public void do_setOption(CmdSetOption cmdSetOption) {
    }

    @Override
    public void do_isReady(CmdIsReady cmdIsReady) {
        uciTango.reply(new RspReadyOk());
    }

    @Override
    public void do_newGame(CmdUciNewGame cmdUciNewGame) {
    }


    @Override
    public void do_go(CmdGo cmdGo) {
        if (CmdGo.GoType.INFINITE.equals(cmdGo.getType())) {
            uciTango.tango.goInfinite();
        } else if (CmdGo.GoType.DEPTH.equals(cmdGo.getType())) {
            uciTango.tango.goDepth(cmdGo.getDepth());
        } else if (CmdGo.GoType.MOVE_TIME.equals(cmdGo.getType())) {
            uciTango.tango.goMoveTime(cmdGo.getTimeOut());
        } else {
            throw new RuntimeException("go subtype not implemented yet");
        }
        uciTango.currentState = uciTango.searchingState;
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
        uciTango.tango.setPosition(CmdPosition.CmdType.STARTPOS == cmdPosition.getType() ? FENDecoder.INITIAL_FEN : cmdPosition.getFen(), cmdPosition.getMoves());
    }
}
