package net.chesstango.evaluation;

import net.chesstango.board.Game;
import net.chesstango.evaluation.evaluators.EvaluatorImp05;

/**
 * @author Mauricio Coria
 * <p>
 * TODO: Que pasa si deshabilitamos el filtrado de movimientos validos?
 * De cualquier forma durante la busqueda encontrariamos que el rey puede ser capturado
 * dado que la ulltima busqueda Quiescence explara todos los movimientos de captura posible.
 */
public interface Evaluator {
    int INFINITE_POSITIVE = Integer.MAX_VALUE;
    int INFINITE_NEGATIVE = -INFINITE_POSITIVE;

    int WHITE_WON = INFINITE_POSITIVE - 1;
    int BLACK_WON = INFINITE_NEGATIVE + 1;

    int WHITE_LOST = BLACK_WON;
    int BLACK_LOST = WHITE_WON;


    int evaluate();

    void setGame(Game game);

    static Evaluator getInstance() {
        return new EvaluatorImp05();
    }
}
