package net.chesstango.uci.engine;

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

/**
 * @author Mauricio Coria
 */
public class EngineTango implements Service {
    protected final UCIOutputStreamEngineExecutor engineExecutor;

    protected final Tango tango;
    private UCIOutputStream responseOutputStream;
    private boolean logging;

    UCIEngine currentState;

    final Ready readyState;
    final WaitCmdUci waitCmdUciState;
    final WaitCmdGo waitCmdGoState;
    final Searching searchingState;

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

        this.tango = createTango(searchMove);

        this.engineExecutor = new UCIOutputStreamEngineExecutor(messageExecutor);
    }

    @Override
    public void accept(UCIMessage message) {
        if (logging) {
            System.out.println("tango << " + message);
        }
        engineExecutor.accept(message);
    }

    public void reply(UCIMessage message) {
        if (logging) {
            System.out.println("tango >> " + message);
        }
        responseOutputStream.accept(message);
    }

    @Override
    public void accept(ServiceVisitor serviceVisitor) {
        serviceVisitor.visit(this);
        tango.accept(serviceVisitor);
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

    protected Tango createTango(SearchMove searchMove) {
        return new Tango(searchMove, searchingState);
    }

    public EngineTango setLogging(boolean flag) {
        this.logging = flag;
        return this;
    }
}
