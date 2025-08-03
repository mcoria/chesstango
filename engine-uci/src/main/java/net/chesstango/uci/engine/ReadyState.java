package net.chesstango.uci.engine;

import lombok.Setter;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENParser;
import net.chesstango.goyeneche.UCIEngine;
import net.chesstango.goyeneche.requests.*;
import net.chesstango.goyeneche.responses.UCIResponse;


/**
 * This class represents one of the possible states in the state design pattern for the UCI engine.
 * In the "ReadyState", the engine is ready to accept commands such as setting options, starting new games,
 * and updating the position on the board. This state also handles transitions to specific states like the
 * "WaitCmdGoState" after setting a position or transitioning to the "EndState" when quitting.
 *
 * @author Mauricio Coria
 */
class ReadyState implements UCIEngine {
    protected final UciTango uciTango;

    @Setter
    private WaitCmdGoState waitCmdGoState;


    volatile private FEN startPosition;

    volatile private boolean reloadTango;

    ReadyState(UciTango uciTango) {
        this.uciTango = uciTango;
        this.reloadTango = false;
    }

    @Override
    public void do_setOption(ReqSetOption cmdSetOption) {
        if ("polyglotFile".equals(cmdSetOption.getId())) {
            uciTango.config.setPolyglotFile(cmdSetOption.getValue());
        } else if ("syzygyDirectory".equals(cmdSetOption.getId())) {
            uciTango.config.setSyzygyDirectory(cmdSetOption.getValue());
        }
        this.reloadTango = true;
    }

    @Override
    public void do_isReady(ReqIsReady cmdIsReady) {
        uciTango.reply(this, UCIResponse.readyok());
    }

    @Override
    public void do_newGame(ReqUciNewGame reqUciNewGame) {
        if (reloadTango) {
            uciTango.reloadTango();
            reloadTango = false;
        }
    }

    @Override
    public void do_quit(ReqQuit cmdQuit) {
        uciTango.changeState(new EndState());
    }

    @Override
    public void do_position(ReqPosition cmdPosition) {
        FEN startPosition = ReqPosition.CmdType.STARTPOS == cmdPosition.getType()
                ? FEN.of(FENParser.INITIAL_FEN)
                : FEN.of(cmdPosition.getFen());

        if (this.startPosition == null || !this.startPosition.equals(startPosition)) {
            this.startPosition = startPosition;
            this.uciTango.session = uciTango.tango.newSession(startPosition);
        }

        uciTango.session.setMoves(cmdPosition.getMoves());

        uciTango.changeState(waitCmdGoState);
    }
}
