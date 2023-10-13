package net.chesstango.uci.arena.matchtypes;

import net.chesstango.uci.arena.gui.EngineController;
import net.chesstango.uci.protocol.requests.CmdGo;
import net.chesstango.uci.protocol.requests.go.CmdGoDepth;
import net.chesstango.uci.protocol.responses.RspBestMove;

/**
 * @author Mauricio Coria
 */
public class MatchByDepth implements MatchType {
    public final CmdGo cmdGo;

    public MatchByDepth(int depth) {
        this.cmdGo = new CmdGoDepth().setDepth(depth);
    }

    @Override
    public RspBestMove retrieveBestMoveFromController(EngineController currentTurn, boolean isWhite) {
        return currentTurn.send_CmdGo(cmdGo);
    }
}
