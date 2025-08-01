package net.chesstango.uci.engine;

import lombok.Setter;
import net.chesstango.engine.Tango;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENParser;
import net.chesstango.goyeneche.UCIEngine;
import net.chesstango.goyeneche.requests.*;
import net.chesstango.goyeneche.responses.UCIResponse;

import static net.chesstango.uci.engine.Options.POLYGLOT_PATH;
import static net.chesstango.uci.engine.Options.SYZYGY_DIRECTORY;

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
    protected final Tango tango;

    @Setter
    private WaitCmdGoState waitCmdGoState;

    ReadyState(UciTango uciTango, Tango tango) {
        this.uciTango = uciTango;
        this.tango = tango;
    }

    @Override
    public void do_uci(ReqUci cmdUci) {
    }

    @Override
    public void do_setOption(ReqSetOption cmdSetOption) {
        if (cmdSetOption.getId().equals(POLYGLOT_PATH)) {
            tango.setPolyglotBook(cmdSetOption.getValue());
        }
        if (cmdSetOption.getId().equals(SYZYGY_DIRECTORY)) {
            //tango.setSyzygyDirectory(cmdSetOption.getValue());
        }
    }

    @Override

    public void do_newGame(ReqUciNewGame cmdUciNewGame) {
        tango.newGame();
    }

    @Override
    public void do_isReady(ReqIsReady cmdIsReady) {
        uciTango.reply(this, UCIResponse.readyok());
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
        tango.setPosition(ReqPosition.CmdType.STARTPOS == cmdPosition.getType()
                        ? FEN.of(FENParser.INITIAL_FEN)
                        : FEN.of(cmdPosition.getFen())
                , cmdPosition.getMoves());
        uciTango.changeState(waitCmdGoState);
    }
}
