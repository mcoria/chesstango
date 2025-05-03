package net.chesstango.uci.engine;

import lombok.Setter;
import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENParser;
import net.chesstango.engine.Tango;
import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.responses.RspReadyOk;

/**
 * This class represents one of the possible states in the state design pattern for the UCI engine.
 * In the "ReadyState", the engine is ready to accept commands such as setting options, starting new games,
 * and updating the position on the board. This state also handles transitions to specific states like the
 * "WaitCmdGoState" after setting a position or transitioning to the "EndState" when quitting.
 *
 * @author Mauricio Coria
 */
class ReadyState implements UCIEngine {
    public static final String POLYGLOT_BOOK = "PolyglotBook";

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
        if (cmdSetOption.getId().equals(POLYGLOT_BOOK)) {
            tango.setPolyglotBook(cmdSetOption.getValue());
        }
    }

    @Override

    public void do_newGame(ReqUciNewGame cmdUciNewGame) {
        tango.newGame();
    }

    @Override
    public void do_isReady(ReqIsReady cmdIsReady) {
        uciTango.reply(this, new RspReadyOk());
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
