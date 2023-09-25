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
public class CmdGoInfinite extends CmdGo {

    @Override
    public void go(CmdGoExecutor cmdGoExecutor) {
        cmdGoExecutor.go(this);
    }

    @Override
    public String toString() {
        return "go infinite";
    }
}
