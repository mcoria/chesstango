package net.chesstango.evaluation;

import net.chesstango.board.Game;

/**
 * @author Mauricio Coria
 *
 * TODO: Que pasa si deshabilitamos el filtrado de movimientos validos?
 * De cualquier forma durante la busqueda encontrariamos que el rey puede ser capturado
 * dado que la ulltima busqueda Quiescence explara todos los movimientos de captura posible.
 */
public interface GameEvaluator {
    int INFINITE_POSITIVE = Integer.MAX_VALUE;
    int INFINITE_NEGATIVE = -INFINITE_POSITIVE;
    int WHITE_LOST = INFINITE_NEGATIVE;
    int BLACK_WON = WHITE_LOST;
    int BLACK_LOST = INFINITE_POSITIVE;
    int WHITE_WON = BLACK_LOST;

    int evaluate(final Game game);
}
