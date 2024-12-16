package net.chesstango.uci.engine.states;

import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.requests.go.CmdGoDepth;
import net.chesstango.uci.protocol.requests.go.CmdGoFast;
import net.chesstango.uci.protocol.requests.go.CmdGoInfinite;
import net.chesstango.uci.protocol.requests.go.CmdGoTime;
import net.chesstango.uci.protocol.responses.RspReadyOk;

/**
 * @author Mauricio Coria
 */
class WaitCmdGo implements UCIEngine {
    private final UciTango uciTango;

    private final CmdGoExecutor cmdGoExecutor;

    protected WaitCmdGo(UciTango uciTango) {
        this.uciTango = uciTango;
        this.cmdGoExecutor = new CmdGoExecutor() {
            @Override
            public void go(CmdGoInfinite cmdGoInfinite) {
                uciTango.tango.goInfinite();
            }

            @Override
            public void go(CmdGoDepth cmdGoDepth) {
                uciTango.tango.goDepth(cmdGoDepth.getDepth());
            }

            @Override
            public void go(CmdGoTime cmdGoTime) {
                uciTango.tango.goTime(cmdGoTime.getTimeOut());
            }

            @Override
            public void go(CmdGoFast cmdGoFast) {
                uciTango.tango.goFast(cmdGoFast.getWTime(), cmdGoFast.getBTime(), cmdGoFast.getWInc(), cmdGoFast.getBInc());
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
        cmdGo.go(cmdGoExecutor);
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
        uciTango.tango.setPosition(CmdPosition.CmdType.STARTPOS == cmdPosition.getType()
                        ? FEN.of(FENDecoder.INITIAL_FEN)
                        : FEN.of(cmdPosition.getFen())
                , cmdPosition.getMoves());
    }
}
