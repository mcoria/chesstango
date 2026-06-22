package net.chesstango.uci.engine;

import lombok.Setter;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.goyeneche.UCIEngine;
import net.chesstango.goyeneche.requests.*;
import net.chesstango.goyeneche.responses.UCIResponse;

import static net.chesstango.uci.engine.UciOption.*;


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

    @Setter
    private WaitCmdGoState waitCmdGoState;

    ReadyState(UciTango uciTango) {
        this.uciTango = uciTango;
    }

    @Override
    public void do_setOption(ReqSetOption cmdSetOption) {
        if (cmdSetOption.getValue() != null && !cmdSetOption.getValue().isEmpty()) {
            if (POLYGLOT_FILE.getId().equals(cmdSetOption.getId())) {
                uciTango.setPolyglotFile(cmdSetOption.getValue());
            } else if (SYZYGY_PATH.getId().equals(cmdSetOption.getId())) {
                uciTango.setSyzygyPath(cmdSetOption.getValue());
            } else if (HASH_SIZE.getId().equals(cmdSetOption.getId())) {
                uciTango.setHashSize(cmdSetOption.getValue());
            }
        } else {
            uciTango.reply(this, UCIResponse.info(String.format("string Invalid value for option '%s'.", cmdSetOption.getId())));
        }
    }

    @Override
    public void do_isReady(ReqIsReady cmdIsReady) {
        uciTango.reply(this, UCIResponse.readyok());
    }

    @Override
    public void do_newGame(ReqUciNewGame reqUciNewGame) {
        uciTango.newSession();
    }

    @Override
    public void do_quit(ReqQuit cmdQuit) {
        uciTango.changeState(new EndState());
    }

    @Override
    public void do_position(ReqPosition cmdPosition) {
        FEN startPosition = ReqPosition.CmdType.STARTPOS == cmdPosition.getType()
                ? FEN.START_POSITION
                : FEN.from(cmdPosition.getFen());

        uciTango.setSessionFEN(startPosition);

        uciTango.setSessionMoves(cmdPosition.getMoves());

        uciTango.changeState(waitCmdGoState);
    }
}
