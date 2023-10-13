package net.chesstango.uci.arena.matchtypes;

import net.chesstango.uci.arena.gui.EngineController;
import net.chesstango.uci.protocol.responses.RspBestMove;

/**
 * @author Mauricio Coria
 */
public interface MatchType {
    
    RspBestMove retrieveBestMoveFromController(EngineController currentTurn, boolean isWhite);
}
