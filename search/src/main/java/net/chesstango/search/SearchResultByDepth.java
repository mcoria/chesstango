package net.chesstango.search;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.moves.Move;

import java.io.Serializable;
import java.util.List;

/**
 * @author Mauricio Coria
 */
@Accessors(chain = true)
@Getter
@Setter
public class SearchResultByDepth implements Serializable {

    /**
     * The depth level at which the search was requested.
     */
    private final int depth;

    /**
     * Stores the evaluation of the best move found during the search.
     * Contains both the move and its corresponding evaluation score.
     */
    private RootMoveEvaluation bestRootMoveEvaluation;

    /**
     * List of evaluations for all possible moves in the current position.
     * Note: This list might be incomplete as not all moves may have been evaluated.
     */
    private List<RootMoveEvaluation> rootMoveEvaluations;

    /**
     * List of principal variations found during the search.
     * A principal variation is the sequence of moves that are considered best by the search algorithm.
     */
    private List<PrincipalVariation> principalVariation;

    /**
     * Indicates if the Principal Variation (PV) is complete.
     * When true, we reached the same evaluation by executing the moves in the PV.
     */
    private boolean pvComplete;

    /**
     * Time spent searching at the last depth level in milliseconds.
     */
    private long timeSearchingLastDepth;

    /**
     * Total accumulated search time in milliseconds across all depth levels.
     */
    private long timeSearching;

    public SearchResultByDepth(int depth) {
        this.depth = depth;
    }

    public Move getBestMove() {
        return bestRootMoveEvaluation != null ? bestRootMoveEvaluation.move() : null;
    }

    public Integer getBestEvaluation() {
        return bestRootMoveEvaluation != null ? bestRootMoveEvaluation.evaluation() : null;
    }
}
