package net.chesstango.search;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.statistics.EvaluationStatistics;
import net.chesstango.search.smart.statistics.NodeStatistics;

import java.util.List;

/**
 * @author Mauricio Coria
 */

@Accessors(chain = true)
@Getter
@Setter
public class SearchMoveResult {

    private final int depth;

    private final MoveEvaluation bestMove;

    private final Move ponderMove;

    private List<Move> principalVariation;

    /**
     * possibleCollisions son movimientos que retornaron una evaluacion igual a best move
     * sin embargo este valor es un bound y el valor real puede ser inferior o superior
     * El problema es intrinsico a AB;
     *
     * Suponiendo que la funcion de evaluacion retorna la profundidad en la cual se produjo de la evaluacion entonces. (este cambio se probó)
     *
     * Dado busqueda MAX con DEPTH=5; tenemos que:
     *
     * Busqueda del nodo1 con A=-INF; Beta=+INF da como VALUE = X
     *
     * Busqueda del nodo2 con A=X; Beta=+INF da como VALUE = X (mismo valor)
     *
     * Esto quiere decir que el resultado en nodo1 es EXACTO pero el resultado de la busqueda en nodo2 es un UPPER BOUND: el valor real puede ser X o menor.
     * Por lo tanto en root no podemos asumir que el valor es el mismo y tampoco comparar por la profundidad de evaluacion para decidir.
     *
     * La lista puede estar vacia !!!
     */
    private List<Move> possibleCollisions;

    /**
     * Evaluaciones de las posiciones que resultan de cada movimiento.
     * La lista de evaluaciones puede no estar completa !!!
     */
    private List<MoveEvaluation> moveEvaluations;

    private EvaluationStatistics evaluationStatistics;

    private NodeStatistics regularNodeStatistics;

    private NodeStatistics quiescenceNodeStatistics;

    private int executedMoves;

    private String epdID;

    private long timeSearching;

    private long timeSearchingLastDepth;

    public SearchMoveResult(int depth, int evaluation, Move bestMove, Move ponderMove) {
        this.depth = depth;
        this.bestMove = new MoveEvaluation(bestMove, evaluation, MoveEvaluationType.EXACT);
        this.ponderMove = ponderMove;
    }

    public Move getBestMove() {
        return bestMove.move();
    }

    public int getEvaluation() {
        return bestMove.evaluation();
    }
}
