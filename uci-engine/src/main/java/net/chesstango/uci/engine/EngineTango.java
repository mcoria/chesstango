package net.chesstango.uci.engine;

import net.chesstango.board.Game;
import net.chesstango.search.DefaultSearchMove;
import net.chesstango.search.SearchMove;
import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.UCIMessage;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.stream.UCIOutputStream;
import net.chesstango.uci.protocol.stream.UCIOutputStreamEngineExecutor;
import net.chesstango.uci.service.UCIService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Mauricio Coria
 */
public class EngineTango implements UCIService {
    protected final SearchMove searchMove;
    protected final UCIOutputStreamEngineExecutor engineExecutor;
    protected UCIOutputStream responseOutputStream;
    protected Game game;
    protected ExecutorService executor;

    ZondaState currentState;

    public EngineTango() {
        this(new DefaultSearchMove());
    }

    public EngineTango(SearchMove searchMove) {
        UCIEngine messageExecutor = new UCIEngine() {
            @Override
            public void do_uci(CmdUci cmdUci) {
                currentState.do_uci(cmdUci);
            }

            @Override
            public void do_isReady(CmdIsReady cmdIsReady) {
                currentState.do_isReady(cmdIsReady);
            }

            @Override
            public void do_setOption(CmdSetOption cmdSetOption) {
            }

            @Override
            public void do_newGame(CmdUciNewGame cmdUciNewGame) {
            }

            @Override
            public void do_position(CmdPosition cmdPosition) {
                currentState.do_position(cmdPosition);
            }

            @Override
            public void do_go(CmdGo cmdGo) {
                currentState.do_go(cmdGo);
            }

            @Override
            public void do_stop(CmdStop cmdStop) {
                currentState.do_stop();
            }

            @Override
            public void do_quit(CmdQuit cmdQuit) {
                currentState.do_stop();
                close();
            }
        };

        this.searchMove = searchMove;
        this.engineExecutor = new UCIOutputStreamEngineExecutor(messageExecutor);
    }

    @Override
    public void accept(UCIMessage message) {
        engineExecutor.accept(message);
    }

    public EngineTango enableAsync() {
        executor = Executors.newSingleThreadExecutor();
        return this;
    }

    @Override
    public void open() {
        currentState = new WaitCmdUci(this);
    }

    @Override
    public void close() {
        if (executor != null) {
            try {
                executor.shutdown();
                while (!executor.awaitTermination(500, TimeUnit.MILLISECONDS)) {
                }
                executor = null;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        currentState = null;
    }

    public Game getGame() {
        return game;
    }

    public void setResponseOutputStream(UCIOutputStream output) {
        this.responseOutputStream = output;
    }

}
