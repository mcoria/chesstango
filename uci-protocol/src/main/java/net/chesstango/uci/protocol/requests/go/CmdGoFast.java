package net.chesstango.uci.protocol.requests.go;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.uci.protocol.requests.CmdGoExecutor;
import net.chesstango.uci.protocol.requests.CmdGo;

/**
 * Fast chess: https://en.wikipedia.org/wiki/Fast_chess
 *
 * @author Mauricio Coria
 */
@Getter
@Setter
@Accessors(chain = true)
public class CmdGoFast extends CmdGo {

    private int wTime;

    private int wInc;

    private int bTime;

    private int bInc;

    @Override
    public void go(CmdGoExecutor cmdGoExecutor) {
        cmdGoExecutor.go(this);
    }

    @Override
    public String toString() {
        return String.format("go wtime %d btime %d winc %d binc %d", wTime, bTime, wInc, bInc);
    }
}
