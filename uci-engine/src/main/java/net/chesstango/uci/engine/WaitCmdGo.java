package net.chesstango.uci.engine;

import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.uci.protocol.GoExecutor;
import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.requests.go.CmdGoByClock;
import net.chesstango.uci.protocol.requests.go.CmdGoByDepth;
import net.chesstango.uci.protocol.requests.go.CmdGoInfinite;
import net.chesstango.uci.protocol.requests.go.CmdGoMoveTime;
import net.chesstango.uci.protocol.responses.RspReadyOk;

/**
 * @author Mauricio Coria
 */
class WaitCmdGo implements UCIEngine {
    private final UciTango uciTango;

    private final GoExecutor goExecutor;

    protected WaitCmdGo(UciTango uciTango) {
        this.uciTango = uciTango;
        this.goExecutor = new GoExecutor() {
            @Override
            public void go(CmdGoInfinite cmdGoInfinite) {
                uciTango.tango.goInfinite();
            }

            @Override
            public void go(CmdGoByDepth cmdGoByDepth) {
                uciTango.tango.goDepth(cmdGoByDepth.getDepth());
            }

            @Override
            public void go(CmdGoMoveTime cmdGoMoveTime) {
                uciTango.tango.goMoveTime(cmdGoMoveTime.getTimeOut());
            }

            @Override
            public void go(CmdGoByClock cmdGoByClock) {
                uciTango.tango.goMoveClock(cmdGoByClock.getWTime(), cmdGoByClock.getBTime(), cmdGoByClock.getWInc(), cmdGoByClock.getBInc());
            }
        };
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
        cmdGo.go(goExecutor);
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
