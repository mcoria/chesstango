package net.chesstango.uci.protocol.requests;

import net.chesstango.uci.protocol.requests.go.CmdGoFast;
import net.chesstango.uci.protocol.requests.go.CmdGoDepth;
import net.chesstango.uci.protocol.requests.go.CmdGoInfinite;
import net.chesstango.uci.protocol.requests.go.CmdGoTime;

/**
 * @author Mauricio Coria
 */
public interface CmdGoExecutor {
    void go(CmdGoInfinite cmdGoInfinite);

    void go(CmdGoDepth cmdGoDepth);

    void go(CmdGoTime cmdGoTime);

    void go(CmdGoFast cmdGoFast);
}
