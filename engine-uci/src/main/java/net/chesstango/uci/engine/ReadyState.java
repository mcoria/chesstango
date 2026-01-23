package net.chesstango.uci.engine;

import lombok.Setter;
import net.chesstango.engine.Config;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENParser;
import net.chesstango.goyeneche.UCIEngine;
import net.chesstango.goyeneche.requests.*;
import net.chesstango.goyeneche.responses.UCIResponse;

import static net.chesstango.uci.engine.UciOption.POLYGLOT_FILE;
import static net.chesstango.uci.engine.UciOption.SYZYGY_PATH;


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

    private volatile boolean loadTango;

    ReadyState(UciTango uciTango, Config tangoConfig) {
        this.uciTango = uciTango;
        this.tangoConfig = tangoConfig;
        this.loadTango = true;
    }

    @Override
    public void do_setOption(ReqSetOption cmdSetOption) {
        if (POLYGLOT_FILE.getId().equals(cmdSetOption.getId())) {
            tangoConfig.setPolyglotFile(cmdSetOption.getValue());
        } else if (SYZYGY_PATH.getId().equals(cmdSetOption.getId())) {
            tangoConfig.setSyzygyPath(cmdSetOption.getValue());
        }
        this.loadTango = true;
    }

    @Override
    public void do_isReady(ReqIsReady cmdIsReady) {
        uciTango.reply(this, UCIResponse.readyok());
    }

    @Override
    public void do_newGame(ReqUciNewGame reqUciNewGame) {
        loadTango();
        uciTango.newSession();
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

        uciTango.setSessionFEN(startPosition);

        uciTango.setSessionMoves(cmdPosition.getMoves());

        uciTango.changeState(waitCmdGoState);
    }

    private void loadTango() {
        if (loadTango) {
            uciTango.loadTango();
            loadTango = false;
        }
    }
}
