package net.chesstango.uci.engine;

import net.chesstango.engine.Session;
import net.chesstango.engine.Tango;
import net.chesstango.search.DefaultSearchMove;
import net.chesstango.search.SearchMove;
import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.UCIMessage;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.stream.UCIOutputStream;
import net.chesstango.uci.protocol.stream.UCIOutputStreamEngineExecutor;
import net.chesstango.uci.service.Service;
import net.chesstango.uci.service.ServiceVisitor;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Mauricio Coria
 */
public class EngineTango implements Service {
    protected final UCIOutputStreamEngineExecutor engineExecutor;

    protected final Tango tango;
    protected UCIOutputStream responseOutputStream;
    protected ExecutorService executor;

    TangoState currentState;

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
                tango.newGame();
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

        this.tango = createTango(searchMove);
        this.engineExecutor = new UCIOutputStreamEngineExecutor(messageExecutor);
    }

    @Override
    public void accept(UCIMessage message) {
        engineExecutor.accept(message);
    }

    @Override
    public void accept(ServiceVisitor serviceVisitor) {
        serviceVisitor.visit(this);
        tango.accept(serviceVisitor);
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

    public void setResponseOutputStream(UCIOutputStream output) {
        this.responseOutputStream = output;
    }

    protected Tango createTango(SearchMove searchMove) {
        return new Tango(searchMove);
    }

    public List<Session> getSessions() {
        return tango.getSessions();
    }
}
