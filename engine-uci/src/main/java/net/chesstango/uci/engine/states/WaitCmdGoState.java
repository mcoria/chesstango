package net.chesstango.uci.engine.states;

import lombok.Setter;
import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.engine.Tango;
import net.chesstango.uci.engine.UciTango;
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
public class WaitCmdGoState implements UCIEngine {
    private final UciTango uciTango;
    private final Tango tango;

    private final CmdGoExecutor cmdGoExecutor;

    @Setter
    private SearchingState searchingState;

    public WaitCmdGoState(UciTango uciTango, Tango tango) {
        this.uciTango = uciTango;
        this.cmdGoExecutor = new CmdGoExecutor() {
            @Override
            public void go(CmdGoInfinite cmdGoInfinite) {
                tango.goInfinite();
            }

            @Override
            public void go(CmdGoDepth cmdGoDepth) {
                tango.goDepth(cmdGoDepth.getDepth());
            }

            @Override
            public void go(CmdGoTime cmdGoTime) {
                tango.goTime(cmdGoTime.getTimeOut());
            }

            @Override
            public void go(CmdGoFast cmdGoFast) {
                tango.goFast(cmdGoFast.getWTime(), cmdGoFast.getBTime(), cmdGoFast.getWInc(), cmdGoFast.getBInc());
            }
        };
        this.tango = tango;
    }

    @Override
    public void do_uci(CmdUci cmdUci) {
    }

    @Override
    public void do_setOption(CmdSetOption cmdSetOption) {
    }

    @Override
    public void do_isReady(CmdIsReady cmdIsReady) {
        uciTango.reply(this, new RspReadyOk());
    }

    @Override
    public void do_newGame(CmdUciNewGame cmdUciNewGame) {
    }


    @Override
    public void do_go(CmdGo cmdGo) {
        cmdGo.go(cmdGoExecutor);
        uciTango.changeState(searchingState);
    }

    @Override
    public void do_stop(CmdStop cmdStop) {
    }

    @Override
    public void do_quit(CmdQuit cmdQuit) {
    }

    @Override
    public void do_position(CmdPosition cmdPosition) {
        tango.setPosition(CmdPosition.CmdType.STARTPOS == cmdPosition.getType()
                        ? FEN.of(FENDecoder.INITIAL_FEN)
                        : FEN.of(cmdPosition.getFen()),
                cmdPosition.getMoves());
    }
}
