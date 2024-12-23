package net.chesstango.uci.engine;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.engine.Tango;
import net.chesstango.uci.engine.states.ReadyState;
import net.chesstango.uci.engine.states.SearchingState;
import net.chesstango.uci.engine.states.WaitCmdGoState;
import net.chesstango.uci.engine.states.WaitCmdUciState;
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
    protected final Tango tango;

    @Getter
    protected final UCIOutputStreamEngineExecutor engineExecutor;

    @Setter
    private UCIOutputStream responseOutputStream;

    @Setter
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
        logger.trace("tango << {}", message);
        synchronized (engineExecutor) {
            engineExecutor.accept(message);
        }
    }

    @Override
    public void open() {
        ReadyState readyState = new ReadyState(this);
        WaitCmdUciState waitCmdUciState = new WaitCmdUciState(this);
        WaitCmdGoState waitCmdGoState = new WaitCmdGoState(this);
        SearchingState searchingState = new SearchingState(this);

        readyState.setWaitCmdGoState(waitCmdGoState);
        waitCmdUciState.setReadyState(readyState);
        waitCmdGoState.setSearchingState(searchingState);
        searchingState.setReadyState(readyState);

        tango.setListenerClient(searchingState);

        currentState = waitCmdUciState;

        tango.open();
    }

    @Override
    public void close() {
        tango.close();
        currentState = null;
    }

    public void reply(UCIMessage message) {
        logger.trace("tango >> {}", message);
        responseOutputStream.accept(message);
    }
}
