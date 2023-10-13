package net.chesstango.engine.timemgmt;

import net.chesstango.board.Game;
import net.chesstango.search.SearchInfo;
import net.chesstango.search.SearchMoveResult;

import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
public interface TimeMgmt {
    int getTimeOut(Game game, int wTime, int bTime, int wInc, int bInc);


    boolean timePredicate(SearchInfo searchInfo, int timeOut);
}
