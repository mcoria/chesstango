package net.chesstango.uci.engine;

import lombok.Setter;
import net.chesstango.engine.Tango;
import net.chesstango.goyeneche.UCIEngine;
import net.chesstango.goyeneche.requests.*;
import net.chesstango.goyeneche.responses.UCIResponse;

import static net.chesstango.uci.engine.Options.POLYGLOT_PATH;
import static net.chesstango.uci.engine.Options.SYZYGY_DIRECTORY;

/**
 * The WaitCmdUciState class is part of the State design pattern implementation for the UCI engine.
 * It represents the state where the engine is waiting for the initial "uci" command from the client.
 * This state handles the "uci" command to provide engine identification and transitions to the
 * ReadyState upon completion.
 *
 * @author Mauricio Coria
 */
class WaitCmdUciState implements UCIEngine {
    private final UciTango uciTango;

    @Setter
    private ReadyState readyState;

    WaitCmdUciState(UciTango uciTango) {
        this.uciTango = uciTango;
    }

    @Override
    public void do_newGame(ReqUciNewGame cmdUciNewGame) {
    }

    @Override
    public void do_uci(ReqUci cmdUci) {
        uciTango.reply(this, UCIResponse.idName(String.format("%s %s", Tango.ENGINE_NAME, Tango.ENGINE_VERSION)));
        uciTango.reply(this, UCIResponse.idAuthor(Tango.ENGINE_AUTHOR));
        uciTango.reply(this, UCIResponse.createStringOption(POLYGLOT_PATH, null));
        uciTango.reply(this, UCIResponse.createStringOption(SYZYGY_DIRECTORY, null));
        uciTango.reply(readyState, UCIResponse.uciok());
    }

    @Override
    public void do_setOption(ReqSetOption cmdSetOption) {
    }

    @Override
    public void do_isReady(ReqIsReady cmdIsReady) {
    }

    @Override
    public void do_go(ReqGo cmdGo) {
    }

    @Override
    public void do_stop(ReqStop cmdStop) {
    }

    @Override
    public void do_quit(ReqQuit cmdQuit) {
        uciTango.changeState(new EndState());
    }

    @Override
    public void do_position(ReqPosition cmdPosition) {
    }
}
