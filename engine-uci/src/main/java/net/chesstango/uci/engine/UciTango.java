package net.chesstango.uci.engine;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.engine.Tango;
import net.chesstango.uci.engine.states.*;
import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.UCIMessage;
import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.stream.UCIOutputStream;
import net.chesstango.uci.protocol.stream.UCIOutputStreamEngineExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.chesstango.uci.protocol.UCIService;

/**
 * @author Mauricio Coria
 */
public class UciTango implements UCIService {
    private static final Logger logger = LoggerFactory.getLogger(UciTango.class);

    @Getter
    private final Tango tango;

    private final UCIOutputStreamEngineExecutor engineExecutor;

    @Setter
    private UCIOutputStream responseOutputStream;

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

        this.tango = tango;

        this.engineExecutor = new UCIOutputStreamEngineExecutor(messageExecutor);
    }

    @Override
    public void accept(UCIMessage message) {
        synchronized (engineExecutor) {
            logger.trace("tango << {}", message);
            engineExecutor.accept(message);
        }
    }

    @Override
    public void open() {
        WaitCmdUciState waitCmdUciState = new WaitCmdUciState(this);
        ReadyState readyState = new ReadyState(this, tango);
        WaitCmdGoState waitCmdGoState = new WaitCmdGoState(this, tango);
        SearchingState searchingState = new SearchingState(this, tango);

        waitCmdUciState.setReadyState(readyState);
        readyState.setWaitCmdGoState(waitCmdGoState);
        waitCmdGoState.setSearchingState(searchingState);
        searchingState.setReadyState(readyState);

        currentState = waitCmdUciState;

        tango.open();
    }

    @Override
    public void close() {
        tango.close();
        currentState = null;
    }

    public void reply(UCIEngine newState, UCIMessage message) {
        synchronized (engineExecutor) {
            logger.trace("tango >> {}", message);
            currentState = newState;
            responseOutputStream.accept(message);
        }
    }

    public void changeState(UCIEngine newState) {
        synchronized (engineExecutor) {
            currentState = newState;
        }
    }
}
