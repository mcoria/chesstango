package net.chesstango.uci.engine;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.engine.Tango;
import net.chesstango.goyeneche.UCICommand;
import net.chesstango.goyeneche.UCIEngine;
import net.chesstango.goyeneche.requests.*;
import net.chesstango.goyeneche.stream.UCIOutputStream;
import net.chesstango.goyeneche.stream.UCIOutputStreamEngineExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.chesstango.goyeneche.UCIService;

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
public class UciTango implements UCIService {
    private static final Logger logger = LoggerFactory.getLogger(UciTango.class);

    @Getter
    private final Tango tango;

    private final UCIOutputStreamEngineExecutor engineExecutor;

    @Setter
    private UCIOutputStream outputStream;

    // Represents the current state of the engine. This is the key variable where the state pattern is applied,
    // allowing behavior to change dynamically at runtime based on the current state.
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
        // State pattern initialization: different state instances are created and linked with one another to 
        // represent the allowable transitions within the state lifecycle of the engine.
        WaitCmdUciState waitCmdUciState = new WaitCmdUciState(this);
        ReadyState readyState = new ReadyState(this, tango);
        WaitCmdGoState waitCmdGoState = new WaitCmdGoState(this, tango);
        SearchingState searchingState = new SearchingState(this, tango);

        waitCmdUciState.setReadyState(readyState);
        readyState.setWaitCmdGoState(waitCmdGoState);
        waitCmdGoState.setSearchingState(searchingState);
        searchingState.setReadyState(readyState);

        // Initialize the chess engine by opening the underlying Tango instance
        tango.open();

        // set the initial state to wait for the UCI command
        changeState(waitCmdUciState);
    }

    @Override
    public void close() {
        changeState(null);

        tango.close();
    }

    
    // Package visibility is used here because this method is intended to be accessed only by other engine-state classes
    // within the same package (e.g., state classes like ReadyState or EndState) to enable state transitions and responses.
    void reply(UCIEngine newState, UCICommand command) {
        synchronized (engineExecutor) {
            logger.trace("tango >> {}", command);
            currentState = newState;
            outputStream.accept(command);
        }
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
    void changeState(UCIEngine newState) {
        synchronized (engineExecutor) {
            currentState = newState;
        }
    }
}
