package net.chesstango.uci.engine;

import lombok.Getter;
import net.chesstango.engine.Tango;
import net.chesstango.search.SearchMove;
import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.UCIMessage;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.stream.UCIOutputStream;
import net.chesstango.uci.protocol.stream.UCIOutputStreamEngineExecutor;
import net.chesstango.uci.service.Service;
import net.chesstango.uci.service.ServiceVisitor;

/**
 * @author Mauricio Coria
 */
public class UciTango implements Service {
    protected final UCIOutputStreamEngineExecutor engineExecutor;

    @Getter
    protected final Tango tango;
    protected final Ready readyState;
    protected final WaitCmdUci waitCmdUciState;
    protected final WaitCmdGo waitCmdGoState;
    protected final Searching searchingState;

    private UCIOutputStream responseOutputStream;
    private boolean logging;
    protected volatile UCIEngine currentState;

    public UciTango() {
        this(new Tango());
    }

    public UciTango(Tango tango) {
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
                currentState.do_setOption(cmdSetOption);
            }

            @Override
            public void do_newGame(CmdUciNewGame cmdUciNewGame) {
                currentState.do_newGame(cmdUciNewGame);
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
                currentState.do_stop(cmdStop);
            }

            @Override
            public void do_quit(CmdQuit cmdQuit) {
                currentState.do_quit(cmdQuit);
            }
        };

        this.readyState = new Ready(this);
        this.waitCmdUciState = new WaitCmdUci(this);
        this.waitCmdGoState = new WaitCmdGo(this);
        this.searchingState = new Searching(this);

        this.tango = tango;
        this.tango.setListenerClient(this.searchingState);

        this.engineExecutor = new UCIOutputStreamEngineExecutor(messageExecutor);
    }

    @Override
    public void accept(UCIMessage message) {
        if (logging) {
            System.out.println("tango << " + message);
        }
        synchronized (engineExecutor) {
            engineExecutor.accept(message);
        }
    }

    protected void reply(UCIMessage message) {
        if (logging) {
            System.out.println("tango >> " + message);
        }
        responseOutputStream.accept(message);
    }

    @Override
    public void accept(ServiceVisitor serviceVisitor) {
        serviceVisitor.visit(this);
    }

    @Override
    public void open() {
        currentState = waitCmdUciState;
        tango.open();
    }

    @Override
    public void close() {
        tango.close();
        currentState = null;
    }

    public void setResponseOutputStream(UCIOutputStream output) {
        this.responseOutputStream = output;
    }

    public UciTango setLogging(boolean flag) {
        this.logging = flag;
        return this;
    }

    protected Tango createTango(SearchMove searchMove, Searching searchingState) {
        return new Tango(searchMove);
    }
}
