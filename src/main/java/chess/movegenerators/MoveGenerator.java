package chess.movegenerators;

import chess.PosicionPieza;
import chess.Square;


/**
 * @author Mauricio Coria
 *
 */
public interface MoveGenerator {

	MoveGeneratorResult calculatePseudoMoves(PosicionPieza origen);

	boolean puedeCapturarPosicion(PosicionPieza origen, Square square);

}
