package net.chesstango.evaluation;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.ChessPositionReader;

import java.util.Iterator;

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
