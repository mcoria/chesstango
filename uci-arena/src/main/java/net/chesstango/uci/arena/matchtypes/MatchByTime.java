package net.chesstango.uci.arena.matchtypes;

import net.chesstango.uci.arena.gui.EngineController;
import net.chesstango.uci.protocol.requests.CmdGo;
import net.chesstango.uci.protocol.requests.go.CmdGoTime;
import net.chesstango.uci.protocol.responses.RspBestMove;

/**
 * @author Mauricio Coria
 */
public class MatchByTime implements MatchType {
    public final CmdGo cmdGo;

    public MatchByTime(int timeOut) {
        this.cmdGo = new CmdGoTime().setTimeOut(timeOut);
    }

    @Override
    public RspBestMove retrieveBestMoveFromController(EngineController currentTurn, boolean isWhite) {
        return currentTurn.send_CmdGo(cmdGo);
    }
}
