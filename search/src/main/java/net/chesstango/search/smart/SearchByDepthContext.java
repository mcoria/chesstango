package net.chesstango.search.smart;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.MoveEvaluation;

import java.util.List;

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
     * Se utiliza para el calculo de PV
     */
    private short[][] trianglePV;

    public SearchByDepthContext(int maxPly) {
        this.maxPly = maxPly;
    }
}
