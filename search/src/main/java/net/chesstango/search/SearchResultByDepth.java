package net.chesstango.search;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.debug.model.DebugNode;

import java.io.Serializable;
import java.util.List;


/**
 * Represents the search result at a specific depth level in the chess engine's search tree.
 * <p>
 * This class encapsulates all information gathered during the search at a particular depth,
 * including the best move found, evaluations of all candidate moves, principal variation,
 * and timing information. It is used to track incremental deepening search results.
 * </p>
 * <p>
 * The class supports debugging capabilities through the storage of debug nodes that
 * represent the search tree structure.
 * </p>
 *
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

    /**
     * Indicates if the search was stopped
     */
    private boolean searchStopped;


    private List<DebugNode> debugNodes;

    /**
     * Constructs a new SearchResultByDepth for the specified depth level.
     *
     * @param depth the depth level at which this search was performed
     */
    public SearchResultByDepth(int depth) {
        this.depth = depth;
    }

    /**
     * Returns the best move found at this depth level.
     *
     * @return the best move, or null if no move has been evaluated yet
     */
    public Move getBestMove() {
        return bestRootMoveEvaluation != null ? bestRootMoveEvaluation.move() : null;
    }

    /**
     * Returns the evaluation score of the best move found at this depth level.
     *
     * @return the evaluation score, or null if no move has been evaluated yet
     */
    public Integer getBestEvaluation() {
        return bestRootMoveEvaluation != null ? bestRootMoveEvaluation.evaluation() : null;
    }
}
