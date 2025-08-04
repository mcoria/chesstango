package net.chesstango.uci.engine;

import net.chesstango.goyeneche.UCIEngine;

/**
 * Represents a terminal state in the UCIEngine's state lifecycle.
 * In the context of the state design pattern, this class defines the behavior of the engine
 * when it has reached an end state, where further commands are either ignored or have no effect.
 * <p>
 * This ensures the engine adheres to a controlled and predictable state transition logic.
 *
 * @author Mauricio Coria
 */
class EndState implements UCIEngine {
    EndState() {
    }
}
