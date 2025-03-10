package net.chesstango.uci.engine;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.engine.Tango;
import net.chesstango.uci.engine.states.*;
import net.chesstango.uci.protocol.UCICommand;
import net.chesstango.uci.protocol.UCIEngine;
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
    private UCIOutputStream outputStream;

    protected volatile UCIEngine currentState;

    public UciTango() {
        this(new Tango());
    }

    public UciTango(Tango tango) {
        UCIEngine messageExecutor = new UCIEngine() {
            @Override
            public void do_uci(ReqUci cmdUci) {
                currentState.do_uci(cmdUci);
            }

            @Override
            public void do_isReady(ReqIsReady cmdIsReady) {
                currentState.do_isReady(cmdIsReady);
            }

            @Override
            public void do_setOption(ReqSetOption cmdSetOption) {
                currentState.do_setOption(cmdSetOption);
            }

            @Override
            public void do_newGame(ReqUciNewGame cmdUciNewGame) {
                currentState.do_newGame(cmdUciNewGame);
            }

            @Override
            public void do_position(ReqPosition cmdPosition) {
                currentState.do_position(cmdPosition);
            }

            @Override
            public void do_go(ReqGo cmdGo) {
                currentState.do_go(cmdGo);
            }

            @Override
            public void do_stop(ReqStop cmdStop) {
                currentState.do_stop(cmdStop);
            }

            @Override
            public void do_quit(ReqQuit cmdQuit) {
                currentState.do_quit(cmdQuit);
            }
        };

        this.tango = tango;

        this.engineExecutor = new UCIOutputStreamEngineExecutor(messageExecutor);
    }

    @Override
    public void accept(UCICommand command) {
        synchronized (engineExecutor) {
            logger.trace("tango << {}", command);
            engineExecutor.accept(command);
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

    public void reply(UCIEngine newState, UCICommand command) {
        synchronized (engineExecutor) {
            logger.trace("tango >> {}", command);
            currentState = newState;
            outputStream.accept(command);
        }
    }

    public void changeState(UCIEngine newState) {
        synchronized (engineExecutor) {
            currentState = newState;
        }
    }
}
