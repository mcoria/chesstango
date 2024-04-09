package net.chesstango.search;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.features.statistics.EvaluationStatistics;
import net.chesstango.search.smart.features.statistics.NodeStatistics;

import java.util.List;

/**
 * @author Mauricio Coria
 */

@Accessors(chain = true)
@Getter
@Setter
public class SearchMoveResult {

    private final int depth;

    private final MoveEvaluation bestMoveEvaluation;

    private final Move ponderMove;

    private List<Move> principalVariation;

    private boolean pvComplete;

    /**
     * possibleCollisions son movimientos que retornaron una evaluacion igual a best move
     * sin embargo este valor es un bound y el valor real puede ser inferior o superior
     * El problema es intrinsico a AB;
     * <p>
     * Suponiendo que la funcion de evaluacion retorna la profundidad en la cual se produjo de la evaluacion entonces. (este cambio se probó)
     * <p>
     * Dado busqueda MAX con DEPTH=5; tenemos que:
     * <p>
     * Busqueda del nodo1 con A=-INF; Beta=+INF da como VALUE = X
     * <p>
     * Busqueda del nodo2 con A=X; Beta=+INF da como VALUE = X (mismo valor)
     * <p>
     * Esto quiere decir que el resultado en nodo1 es EXACTO pero el resultado de la busqueda en nodo2 es un UPPER BOUND: el valor real puede ser X o menor.
     * Por lo tanto en root no podemos asumir que el valor es el mismo y tampoco comparar por la profundidad de evaluacion para decidir.
     * <p>
     * La lista puede estar vacia !!!
     */
    private List<Move> possibleCollisions;

    private EvaluationStatistics evaluationStatistics;

    private NodeStatistics regularNodeStatistics;

    private NodeStatistics quiescenceNodeStatistics;

    private int executedMoves;

    private String epdID;

    private long timeSearching;

    private int searchByDepthPvCompleteCounter;

    private int searchByDepthCounter;

    public SearchMoveResult(int depth, MoveEvaluation bestMoveEvaluation, Move ponderMove) {
        this.depth = depth;
        this.bestMoveEvaluation = bestMoveEvaluation;
        this.ponderMove = ponderMove;
    }

    public Move getBestMove() {
        return bestMoveEvaluation.move();
    }

    public int getBestEvaluation() {
        return bestMoveEvaluation.evaluation();
    }
}
