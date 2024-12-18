package net.chesstango.uci.engine.engine;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.engine.Tango;
import net.chesstango.uci.engine.Service;
import net.chesstango.uci.engine.ServiceVisitor;
import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.UCIMessage;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.stream.UCIOutputStream;
import net.chesstango.uci.protocol.stream.UCIOutputStreamEngineExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mauricio Coria
 */
public class UciTango implements Service {
    private static final Logger logger = LoggerFactory.getLogger(UciTango.class);

    @Getter
    protected final Tango tango;

    @Setter
    private UCIOutputStream responseOutputStream;

    protected final UCIOutputStreamEngineExecutor engineExecutor;
    protected final Ready readyState;
    protected final WaitCmdUci waitCmdUciState;
    protected final WaitCmdGo waitCmdGoState;
    protected final Searching searchingState;
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
        logger.trace("tango << {}", message);
        synchronized (engineExecutor) {
            engineExecutor.accept(message);
        }
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

    protected void reply(UCIMessage message) {
        logger.trace("tango >> {}", message);
        responseOutputStream.accept(message);
    }
}
