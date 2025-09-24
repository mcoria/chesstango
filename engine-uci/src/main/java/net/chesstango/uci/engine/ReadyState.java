package net.chesstango.uci.engine;

import lombok.Setter;
import net.chesstango.engine.Config;
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
    private final UciTango uciTango;

    private final Config tangoConfig;

    @Setter
    private WaitCmdGoState waitCmdGoState;

    volatile private FEN startPosition;

    volatile private boolean reloadTango;

    ReadyState(UciTango uciTango, Config tangoConfig) {
        this.uciTango = uciTango;
        this.tangoConfig = tangoConfig;
        this.reloadTango = true;
    }

    @Override
    public void do_setOption(ReqSetOption cmdSetOption) {
        if ("PolyglotFile".equals(cmdSetOption.getId())) {
            tangoConfig.setPolyglotFile(cmdSetOption.getValue());
        } else if ("SyzygyDirectory".equals(cmdSetOption.getId())) {
            tangoConfig.setSyzygyDirectory(cmdSetOption.getValue());
        }
        this.reloadTango = true;
    }

    @Override
    public void do_isReady(ReqIsReady cmdIsReady) {
        uciTango.reply(this, UCIResponse.readyok());
    }

    @Override
    public void do_newGame(ReqUciNewGame reqUciNewGame) {
        loadTango();
    }

    @Override
    public void do_quit(ReqQuit cmdQuit) {
        uciTango.changeState(new EndState());
    }

    @Override
    public void do_position(ReqPosition cmdPosition) {
        loadTango();

        FEN startPosition = ReqPosition.CmdType.STARTPOS == cmdPosition.getType()
                ? FEN.of(FENParser.INITIAL_FEN)
                : FEN.of(cmdPosition.getFen());

        if (this.startPosition == null || !this.startPosition.equals(startPosition)) {
            this.startPosition = startPosition;
            this.uciTango.newSession(startPosition);
        }

        uciTango.setSessionMoves(cmdPosition.getMoves());

        uciTango.changeState(waitCmdGoState);
    }

    private void loadTango() {
        if (reloadTango) {
            uciTango.loadTango();
            reloadTango = false;
        }
    }
}
