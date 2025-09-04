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
     * The depth level at which the search was performed. A depth of N means the search algorithm
     * analyzed N moves ahead in the game tree.
     */
    private final int depth;


    /**
     * Indicates whether the search should continue to deeper levels.
     * Used to control the iterative deepening process.
     */
    private boolean continueDeepening;

    /**
     * Stores the evaluation of the best move found during the search.
     * Contains both the move and its corresponding evaluation score.
     */
    private MoveEvaluation bestMoveEvaluation;

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
     * List of evaluations for all possible moves in the current position.
     * Note: This list might be incomplete as not all moves may have been evaluated.
     */
    private List<MoveEvaluation> moveEvaluations;

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
        return bestMoveEvaluation != null ? bestMoveEvaluation.move() : null;
    }

    public Integer getBestEvaluation() {
        return bestMoveEvaluation != null ? bestMoveEvaluation.evaluation() : null;
    }
}
