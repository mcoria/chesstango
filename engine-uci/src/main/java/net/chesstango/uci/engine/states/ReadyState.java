package net.chesstango.uci.engine.states;

import lombok.Setter;
import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.engine.Tango;
import net.chesstango.uci.engine.UciTango;
import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.responses.RspReadyOk;

/**
 * @author Mauricio Coria
 */
public class ReadyState implements UCIEngine {
    public static final String POLYGLOT_BOOK = "PolyglotBook";

    protected final UciTango uciTango;
    protected final Tango tango;

    @Setter
    private WaitCmdGoState waitCmdGoState;

    public ReadyState(UciTango uciTango, Tango tango) {
        this.uciTango = uciTango;
        this.tango = tango;
    }

    @Override
    public void do_uci(CmdUci cmdUci) {
    }

    @Override
    public void do_setOption(CmdSetOption cmdSetOption) {
        switch (cmdSetOption.getId()) {
            case POLYGLOT_BOOK:
                tango.setPolyglotBook(cmdSetOption.getValue());
        }
    }

    @Override

    public void do_newGame(CmdUciNewGame cmdUciNewGame) {
        tango.newGame();
    }

    @Override
    public void do_isReady(CmdIsReady cmdIsReady) {
        uciTango.reply(this, new RspReadyOk());
    }

    @Override
    public void do_go(CmdGo cmdGo) {
    }

    @Override
    public void do_stop(CmdStop cmdStop) {
    }

    @Override
    public void do_quit(CmdQuit cmdQuit) {
        uciTango.changeState(new EndState());
    }

    @Override
    public void do_position(CmdPosition cmdPosition) {
        tango.setPosition(CmdPosition.CmdType.STARTPOS == cmdPosition.getType()
                        ? FEN.of(FENDecoder.INITIAL_FEN)
                        : FEN.of(cmdPosition.getFen())
                , cmdPosition.getMoves());
        uciTango.changeState(waitCmdGoState);;
    }
}
