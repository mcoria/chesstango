package net.chesstango.search.smart;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.smart.features.debug.SearchTracker;
import net.chesstango.search.smart.features.killermoves.KillerMoves;
import net.chesstango.search.smart.features.transposition.TTable;

import java.util.List;
import java.util.Map;

/**
 * @author Mauricio Coria
 */

@Setter
@Getter
public class SearchByCycleContext {

    private final Game game;

    private TTable maxMap;
    private TTable minMap;
    private TTable qMaxMap;
    private TTable qMinMap;

    private KillerMoves killerMoves;

    private int[] visitedNodesCounters;
    private int[] expectedNodesCounters;
    private int[] visitedNodesCountersQuiescence;
    private int[] expectedNodesCountersQuiescence;

    private Map<Long, String> zobristMaxMap;
    private Map<Long, String> zobristMinMap;
    private List<String> zobristCollisions;

    private SearchTracker searchTracker;

    private Map<SearchParameter, Object> searchParameters;

    public SearchByCycleContext(Game game) {
        this.game = game;
    }
}
