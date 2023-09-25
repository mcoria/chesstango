package net.chesstango.uci.arena.mathtypes;

import net.chesstango.uci.arena.gui.EngineController;
import net.chesstango.uci.protocol.requests.CmdGo;
import net.chesstango.uci.protocol.requests.go.CmdGoMoveTime;
import net.chesstango.uci.protocol.responses.RspBestMove;

/**
 * @author Mauricio Coria
 */
public class MatchByMoveTime implements MatchType {
    public final CmdGo cmdGo;

    public MatchByMoveTime(int timeOut) {
        this.cmdGo = new CmdGoMoveTime().setTimeOut(timeOut);
    }

    @Override
    public RspBestMove retrieveBestMoveFromController(EngineController currentTurn, boolean isWhite) {
        return currentTurn.send_CmdGo(cmdGo);
    }
}
