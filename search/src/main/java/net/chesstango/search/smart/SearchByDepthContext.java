package net.chesstango.search.smart;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.search.MoveEvaluation;

import java.util.List;
import java.util.Queue;

/**
 * @author Mauricio Coria
 */

@Setter
@Getter
public class SearchByDepthContext {

    private final int maxPly;

    private MoveEvaluation lastBestMoveEvaluation;

    private List<MoveEvaluation> lastMoveEvaluations;

    /**
     * El proposito es que comparator consuma los elementos de la lista durante la primer ejecucion
     */
    private Queue<Move> lastPrincipalVariation;

    /**
     * Se utiliza para el calculo de PV
     */
    private short[][] trianglePV;

    public SearchByDepthContext(int maxPly) {
        this.maxPly = maxPly;
    }
}
