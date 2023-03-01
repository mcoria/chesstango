package net.chesstango.search.smart;

import net.chesstango.board.Color;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMove;

import java.util.Comparator;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public abstract class AbstractSmart implements SearchMove {
    protected boolean keepProcessing = true;

    @Override
    public void stopSearching() {
        keepProcessing = false;
    }

    /**
     * La idea es seleccionar siempre la misma posicion en caso de que exista más de una opcion.
     * La seleccion es simetrica respecto al color.
     */
    protected Move selectMove(Color currentTurn, List<Move> moves) {
        if (moves.size() == 0) {
            throw new RuntimeException("There is no move to select");
        } else if (moves.size() == 1) {
            return moves.get(0);
        }

        //TODO: esta situacion indica que dos movimientos distintos obtuvieron la misma evaluacion.
        // a ciencia cierta no sabemos si se deben a que se alcanza la misma posicion o son posicione distintas
        // en el caso de ser posiciones iguales las coliciones deberian disminuir si aumentamos la profundidad de busqueda
        // en el caso de ser posiciones distintas estamos en presencia de una mala funcion de evaluacion estatica


        Comparator<Integer> fromFn = Color.WHITE.equals(currentTurn) ? Integer::max : Integer::min;
        Comparator<Integer> toFn = Color.WHITE.equals(currentTurn) ? Integer::min : Integer::max;

        final int fromIdx = moves.stream().mapToInt(move -> move.getFrom().getSquare().toIdx()).reduce(fromFn::compare).getAsInt();
        final int toIdx = moves.stream().filter(move -> move.getFrom().getSquare().toIdx() == fromIdx).mapToInt(move -> move.getTo().getSquare().toIdx()).reduce(toFn::compare).getAsInt();

        //TODO: que pasa cuando son promociones ?!?!
        return moves.stream().filter(move -> move.getFrom().getSquare().toIdx() == fromIdx && move.getTo().getSquare().toIdx() == toIdx).findAny().get();
    }
}
