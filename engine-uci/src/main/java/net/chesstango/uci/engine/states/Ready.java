package net.chesstango.uci.engine.states;

import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.responses.RspReadyOk;

/**
 * @author Mauricio Coria
 */
class Ready implements UCIEngine {

    public static final String POLYGLOT_BOOK = "PolyglotBook";
    private final UciTango uciTango;

    protected Ready(UciTango uciTango) {
        this.uciTango = uciTango;
    }

    @Override
    public void do_uci(CmdUci cmdUci) {
    }

    @Override
    public void do_setOption(CmdSetOption cmdSetOption) {
        switch (cmdSetOption.getId()) {
            case POLYGLOT_BOOK:
                this.uciTango.tango.setPolyglotBook(cmdSetOption.getValue());
        }
    }

    @Override

    public void do_newGame(CmdUciNewGame cmdUciNewGame) {
        uciTango.tango.newGame();
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
        uciTango.tango.setPosition(CmdPosition.CmdType.STARTPOS == cmdPosition.getType()
                        ? FEN.of(FENDecoder.INITIAL_FEN)
                        : FEN.of(cmdPosition.getFen())
                , cmdPosition.getMoves());
        uciTango.currentState = uciTango.waitCmdGoState;
    }
}
