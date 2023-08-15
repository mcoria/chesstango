package net.chesstango.uci.engine;

import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.responses.RspReadyOk;

/**
 * @author Mauricio Coria
 */
class Ready implements UCIEngine {

    private final EngineTango engineTango;

    protected Ready(EngineTango engineTango) {
        this.engineTango = engineTango;
    }

    @Override
    public void do_uci(CmdUci cmdUci) {
    }

    @Override
    public void do_setOption(CmdSetOption cmdSetOption) {

    }

    @Override
    public void do_newGame(CmdUciNewGame cmdUciNewGame) {
        engineTango.tango.newGame();
    }

    @Override
    public void do_isReady(CmdIsReady cmdIsReady) {
        engineTango.reply(new RspReadyOk());
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
        engineTango.close();
    }

    @Override
    public void do_position(CmdPosition cmdPosition) {
        engineTango.tango.setPosition(CmdPosition.CmdType.STARTPOS == cmdPosition.getType() ? FENDecoder.INITIAL_FEN : cmdPosition.getFen(), cmdPosition.getMoves());
        engineTango.currentState = engineTango.waitCmdGoState;
    }
}
