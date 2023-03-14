package net.chesstango.search.smart;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mauricio Coria
 */
public class MoveSelector {
    /**
     * La idea es seleccionar siempre la misma posicion en caso de que exista más de una opcion.
     * La seleccion es simetrica respecto al color.
     *
     * Observar la cantidad de veces que existe mas de un movimiento posible optimimo.
     *
     * Positions: Balsa_Top10.pgn  (Match)
     * Depth: 4
     * Time taken: 140897 ms
     *  ___________________________________________________________________________________________________________________________________________________
     * |ENGINE NAME                        |WHITE WON|BLACK WON|WHITE LOST|BLACK LOST|WHITE DRAW|BLACK DRAW|WHITE POINTS|BLACK POINTS|TOTAL POINTS|   WIN %|
     * |            GameEvaluatorSEandImp02|       5 |       6 |        1 |        2 |        4 |        2 |        7.0 |        7.0 |  14.0 / 20 |   70.0 |
     * |                 GameEvaluatorImp02|       2 |       1 |        6 |        5 |        2 |        4 |        3.0 |        3.0 |   6.0 / 20 |   30.0 |
     *  ---------------------------------------------------------------------------------------------------------------------------------------------------
     *  __________________________________________________________________________________________
     * |ENGINE NAME                        | SEARCHES | wo/COLLISIONS | w/COLLISIONS | COLLISIONS |
     * |            GameEvaluatorSEandImp02|      885 |           757 |          128 |        212 |
     * |                 GameEvaluatorImp02|      882 |           604 |          278 |        563 |
     *  ------------------------------------------------------------------------------------------
     */
    public Move selectMove(Color currentTurn, List<Move> moves) {
        if (moves.size() == 0) {
            throw new RuntimeException("There is no move to select");
        } else if (moves.size() == 1) {
            return moves.get(0);
        }

        //TODO: esta situacion indica que dos movimientos distintos obtuvieron la misma evaluacion.
        // a ciencia cierta no sabemos si se deben a que se alcanza la misma posicion o son posicione distintas
        // en el caso de ser posiciones iguales las coliciones deberian disminuir si aumentamos la profundidad de busqueda
        // en el caso de ser posiciones distintas estamos en presencia de una mala funcion de evaluacion estatica

        List<Move> movesToSquare = null;

        if (Color.WHITE.equals(currentTurn)) {
            int maxFromRank = moves.stream().map(Move::getFrom).map(PiecePositioned::getSquare).mapToInt(Square::getRank).max().getAsInt();
            int minFromFile = moves.stream().map(Move::getFrom).map(PiecePositioned::getSquare).filter(square -> square.getRank() == maxFromRank).mapToInt(Square::getFile).min().getAsInt();

            // Aca seleccionamos todos los movimientos que parten del mismo Square
            List<Move> movesFromSquare = moves.stream().filter(move -> move.getFrom().getSquare().getRank() == maxFromRank && move.getFrom().getSquare().getFile() == minFromFile).collect(Collectors.toList());

            int maxToRank = movesFromSquare.stream().map(Move::getTo).map(PiecePositioned::getSquare).mapToInt(Square::getRank).max().getAsInt();
            int minToFile = movesFromSquare.stream().map(Move::getTo).map(PiecePositioned::getSquare).filter(square -> square.getRank() == maxToRank).mapToInt(Square::getFile).min().getAsInt();

            // Aca seleccionamos todos los movimientos que llegan al mismo Square
            movesToSquare = movesFromSquare.stream().filter(move -> move.getTo().getSquare().getRank() == maxToRank && move.getTo().getSquare().getFile() == minToFile).collect(Collectors.toList());
        } else {
            int minFromRank = moves.stream().map(Move::getFrom).map(PiecePositioned::getSquare).mapToInt(Square::getRank).min().getAsInt();
            int minFromFile = moves.stream().map(Move::getFrom).map(PiecePositioned::getSquare).filter(square -> square.getRank() == minFromRank).mapToInt(Square::getFile).min().getAsInt();

            // Aca seleccionamos todos los movimientos que parten del mismo Square
            List<Move> movesFromSquare = moves.stream().filter(move -> move.getFrom().getSquare().getRank() == minFromRank && move.getFrom().getSquare().getFile() == minFromFile).collect(Collectors.toList());

            int minToRank = movesFromSquare.stream().map(Move::getTo).map(PiecePositioned::getSquare).mapToInt(Square::getRank).min().getAsInt();
            int minToFile = movesFromSquare.stream().map(Move::getTo).map(PiecePositioned::getSquare).filter(square -> square.getRank() == minToRank).mapToInt(Square::getFile).min().getAsInt();

            // Aca seleccionamos todos los movimientos que llegan al mismo Square
            movesToSquare = movesFromSquare.stream().filter(move -> move.getTo().getSquare().getRank() == minToRank && move.getTo().getSquare().getFile() == minToFile).collect(Collectors.toList());
        }

        //TODO: que pasa cuando son promociones ?!?!
        return movesToSquare.size() == 1 ?  movesToSquare.get(0) : movesToSquare.stream().map(move -> (MovePromotion) move).filter(movePromotion -> Piece.QUEEN_WHITE.equals(movePromotion.getPromotion()) || Piece.QUEEN_BLACK.equals(movePromotion.getPromotion())).findAny().get();
    }
}
