package net.chesstango.board.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 * <p>
 * TODO: todos los movimientos deberian tener Cardinal o no
 * TODO: tener movimientos para cada pieza
 */
public interface MoveFactory extends PawnMoveFactory, RookMoveFactory, KnightMoveFactory, KingMoveFactory {

    Move createSimpleMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal);

    Move createCaptureMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal);
}