package chess.pseudomovesgenerators;

import chess.PiecePositioned;
import chess.Square;


/**
 * @author Mauricio Coria
 *
 */
public interface MoveGenerator {

	MoveGeneratorResult calculatePseudoMoves(PiecePositioned origen);

	boolean puedeCapturarPosicion(PiecePositioned origen, Square square);

}
