package net.chesstango.uci.engine;

import lombok.Setter;
import net.chesstango.engine.Tango;
import net.chesstango.goyeneche.UCIEngine;
import net.chesstango.goyeneche.requests.ReqUci;
import net.chesstango.goyeneche.responses.UCIResponse;


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
    public void do_uci(ReqUci cmdUci) {
        uciTango.reply(this, UCIResponse.idName(String.format("%s %s", Tango.ENGINE_NAME, Tango.ENGINE_VERSION)));
        uciTango.reply(this, UCIResponse.idAuthor(Tango.ENGINE_AUTHOR));
        uciTango.reply(this, UCIResponse.createStringOption("PolyglotFile", uciTango.tangoConfig.getPolyglotFile()));
        uciTango.reply(this, UCIResponse.createStringOption("SyzygyDirectory", uciTango.tangoConfig.getSyzygyDirectory()));
        uciTango.reply(readyState, UCIResponse.uciok());
    }
}
