package net.chesstango.uci.protocol;

import net.chesstango.uci.protocol.requests.go.CmdGoByClock;
import net.chesstango.uci.protocol.requests.go.CmdGoByDepth;
import net.chesstango.uci.protocol.requests.go.CmdGoInfinite;
import net.chesstango.uci.protocol.requests.go.CmdGoMoveTime;

/**
 * @author Mauricio Coria
 */
public interface GoExecutor {
    void go(CmdGoInfinite cmdGoInfinite);

    void go(CmdGoByDepth cmdGoByDepth);

    void go(CmdGoMoveTime cmdGoMoveTime);

    void go(CmdGoByClock cmdGoByClock);
}
