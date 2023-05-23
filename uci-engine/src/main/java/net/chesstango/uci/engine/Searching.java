package net.chesstango.uci.engine;

import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.responses.RspBestMove;

import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 */
class Searching implements UCIEngine, Consumer<String> {
    private final EngineTango engineTango;

    Searching(EngineTango engineTango) {
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
    }

    @Override
    public void do_newGame(CmdUciNewGame cmdUciNewGame) {
    }

    @Override
    public void do_go(CmdGo cmdGo) {
    }

    @Override
    public void do_stop(CmdStop cmdStop) {
        engineTango.tango.stopSearching();
    }

    @Override
    public void do_quit(CmdQuit cmdQuit) {
        engineTango.tango.stopSearching();
        engineTango.close();
    }

    @Override
    public void do_position(CmdPosition cmdPosition) {
    }

    @Override
    public void accept(String selectedMove) {
        engineTango.reply(new RspBestMove(selectedMove));

        engineTango.currentState = engineTango.readyState;
    }
}
