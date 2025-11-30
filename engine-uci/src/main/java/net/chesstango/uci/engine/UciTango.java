package net.chesstango.uci.engine;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.chesstango.engine.Config;
import net.chesstango.engine.SearchListener;
import net.chesstango.engine.Session;
import net.chesstango.engine.Tango;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.goyeneche.UCICommand;
import net.chesstango.goyeneche.UCIEngine;
import net.chesstango.goyeneche.UCIService;
import net.chesstango.goyeneche.requests.*;
import net.chesstango.goyeneche.stream.UCIOutputStream;
import net.chesstango.goyeneche.stream.UCIOutputStreamEngineExecutor;

import java.util.List;
import java.util.function.Function;

/**
 * The UciTango class operates as a context within the state design pattern, encapsulating the state-dependent behavior
 * of a UCI-based chess engine. Each state (e.g., WaitCmdUciState, ReadyState) represents specific engine behavior
 * and transitions dynamically at runtime, allowing for flexible and extensible state management.
 * <p>
 * This design ensures each state manages its own behavior and transitions, decoupling the logic for each stage
 * of the engine's lifecycle while maintaining a centralized context within the UciTango class.
 *
 * @author Mauricio Coria
 */
@Slf4j
public class UciTango implements UCIService {
    private final UCIOutputStreamEngineExecutor engineExecutor;

    private final Config tangoConfig;

    private final Function<Config, Tango> tangoFactory;

    @Setter
    private UCIOutputStream outputStream;


    // Represents the current state of the engine. This is the key variable where the state pattern is applied,
    // allowing behavior to change dynamically at runtime based on the current state.
    @Getter(AccessLevel.PACKAGE)
    private volatile UCIEngine currentState;

    private volatile Tango tango;

    @Getter
    private volatile Session session;

    public UciTango() {
        this(new Config().setSyncSearch(false), Tango::open);
    }

    public UciTango(Config tangoConfig) {
        this(tangoConfig, Tango::open);
    }

    UciTango(Config tangoConfig, Function<Config, Tango> tangoFactory) {
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

        this.engineExecutor = new UCIOutputStreamEngineExecutor(messageExecutor);

        this.tangoConfig = tangoConfig;

        this.tangoFactory = tangoFactory;
    }

    @Override
    public synchronized void accept(UCICommand command) {
        log.trace("tango << {}", command);
        engineExecutor.accept(command);
    }

    @Override
    public void open() {
        // State pattern initialization: different state instances are created and linked with one another to 
        // represent the allowable transitions within the state lifecycle of the engine.
        WaitCmdUciState waitCmdUciState = new WaitCmdUciState(this, tangoConfig);
        ReadyState readyState = new ReadyState(this, tangoConfig);
        WaitCmdGoState waitCmdGoState = new WaitCmdGoState(this);
        SearchingState searchingState = new SearchingState(this);

        waitCmdUciState.setReadyState(readyState);
        readyState.setWaitCmdGoState(waitCmdGoState);
        waitCmdGoState.setSearchingState(searchingState);
        searchingState.setReadyState(readyState);

        // set the initial state to wait for the UCI command
        changeState(waitCmdUciState);
    }


    @Override
    public void close() {
        try {
            tango.close();
        } catch (Exception e) {
            log.error("Failed to close tango", e);
        }
    }

    // Package visibility is used here because this method is intended to be accessed only by other engine-state classes
    // within the same package (e.g., state classes like ReadyState or EndState) to enable state transitions and responses.
    synchronized void reply(UCIEngine newState, UCICommand command) {
        log.trace("tango >> {}", command);
        outputStream.accept(command);
        changeState(newState);
    }


    /**
     * This method has package visibility to allow specific state classes within the same package to modify the
     * engine's current state in a controlled manner. For example:
     * <p>
     * ReadyState readyState = new ReadyState(this, tango);
     * readyState.setWaitCmdGoState(waitCmdGoState);
     * uciTango.changeState(readyState);
     * <p>
     * External classes cannot call this method, ensuring the state transitions are limited to valid contexts
     * within the same package.
     */
    synchronized void changeState(UCIEngine newState) {
        currentState = newState;
    }

    void loadTango() {
        log.debug("Loading tango");
        try {
            if (tango != null) {
                tango.close();
            }
            tango = tangoFactory.apply(tangoConfig);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void newSession() {
        session = tango.newSession();
    }

    void setSessionFEN(FEN fen) {
        session.setFen(fen);
    }

    void setSessionMoves(List<String> moves) {
        session.setMoves(moves);
    }

    public void stopSearching() {
        session.stopSearching();
    }

    void goInfinite() {
        session.goInfinite();
    }

    void goDepth(int depth) {
        session.goDepth(depth);
    }

    void goTime(int timeOut) {
        session.goTime(timeOut);
    }

    void goFast(int wTime, int bTime, int wInc, int bInc) {
        session.goFast(wTime, bTime, wInc, bInc);
    }

    void setSessionSearchListener(SearchListener searchListener) {
        session.setSearchListener(searchListener);
    }
}
