package net.chesstango.uci.protocol.requests.go;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.uci.protocol.requests.CmdGoExecutor;
import net.chesstango.uci.protocol.requests.CmdGo;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
@Accessors(chain = true)
public class CmdGoTime extends CmdGo {

    private int timeOut;

    @Override
    public void go(CmdGoExecutor cmdGoExecutor) {
        cmdGoExecutor.go(this);
    }

    @Override
    public String toString() {
        return String.format("go movetime %d", timeOut);
    }
}
