package net.chesstango.uci.protocol.requests.go;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.uci.protocol.GoExecutor;
import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.requests.CmdGo;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
@Accessors(chain = true)
public class CmdGoByClock extends CmdGo {

    private int wTime;

    private int wInc;

    private int bTime;

    private int bInc;

    @Override
    public void go(GoExecutor goExecutor) {
        goExecutor.go(this);
    }

    @Override
    public String toString() {
        return String.format("go wtime %d btime %d winc %d binc %d", wTime, bTime, wInc, bInc);
    }
}
