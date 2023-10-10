package net.chesstango.search.smart;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.smart.transposition.TTable;

import java.util.List;

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

    private Move lastBestMove;
    private Integer lastBestEvaluation;
    private List<MoveEvaluation> lastMoveEvaluations;

    public SearchContext(int maxPly) {
        this.maxPly = maxPly;
    }
}
