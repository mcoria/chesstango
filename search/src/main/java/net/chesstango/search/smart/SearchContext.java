package net.chesstango.search.smart;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.smart.transposition.TTable;

/**
 * @author Mauricio Coria
 */

@Setter
@Getter
public class SearchContext {

    private final int maxPly;

    private TTable maxMap;

    private TTable minMap;

    private TTable qMaxMap;

    private TTable qMinMap;

    private int[] visitedNodesCounters;
    private int[] expectedNodesCounters;
    private int[] visitedNodesCountersQuiescence;
    private int[] expectedNodesCountersQuiescence;

    public SearchContext(int maxPly) {
        this.maxPly = maxPly;
    }

}
