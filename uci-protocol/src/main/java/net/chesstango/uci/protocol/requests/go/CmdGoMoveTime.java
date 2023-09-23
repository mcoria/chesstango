package net.chesstango.uci.protocol.requests.go;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.uci.protocol.GoExecutor;
import net.chesstango.uci.protocol.requests.CmdGo;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
@Accessors(chain = true)
public class CmdGoMoveTime extends CmdGo {

    private int timeOut;

    @Override
    public void go(GoExecutor goExecutor) {
        goExecutor.go(this);
    }

    @Override
    public String toString() {
        return String.format("go movetime %d", timeOut);
    }
}
