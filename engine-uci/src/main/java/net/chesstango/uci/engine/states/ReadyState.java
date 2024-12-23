package net.chesstango.uci.engine.states;

import lombok.Setter;
import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.uci.engine.UciTango;
import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.responses.RspReadyOk;

/**
 * @author Mauricio Coria
 */
public class ReadyState implements UCIEngine {

    public static final String POLYGLOT_BOOK = "PolyglotBook";

    private final UciTango uciTango;

    @Setter
    private WaitCmdGoState waitCmdGoState;

    public ReadyState(UciTango uciTango) {
        this.uciTango = uciTango;
    }

    @Override
    public void do_uci(CmdUci cmdUci) {
    }

    @Override
    public void do_setOption(CmdSetOption cmdSetOption) {
        switch (cmdSetOption.getId()) {
            case POLYGLOT_BOOK:
                this.uciTango.getTango().setPolyglotBook(cmdSetOption.getValue());
        }
    }

    @Override

    public void do_newGame(CmdUciNewGame cmdUciNewGame) {
        uciTango.getTango().newGame();
    }

    @Override
    public void do_isReady(CmdIsReady cmdIsReady) {
        uciTango.reply(new RspReadyOk());
    }

    @Override
    public void do_go(CmdGo cmdGo) {
        throw new RuntimeException("Unable to process go command. Waiting position command.");
    }

    @Override
    public void do_stop(CmdStop cmdStop) {
        throw new RuntimeException("Unable to process stop command. Waiting position command.");
    }

    @Override
    public void do_quit(CmdQuit cmdQuit) {
        uciTango.close();
    }

    @Override
    public void do_position(CmdPosition cmdPosition) {
        uciTango.getTango().setPosition(CmdPosition.CmdType.STARTPOS == cmdPosition.getType()
                        ? FEN.of(FENDecoder.INITIAL_FEN)
                        : FEN.of(cmdPosition.getFen())
                , cmdPosition.getMoves());
        uciTango.setCurrentState(waitCmdGoState);;
    }
}
