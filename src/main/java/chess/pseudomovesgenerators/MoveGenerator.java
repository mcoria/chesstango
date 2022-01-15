package chess.pseudomovesgenerators;

import chess.PiecePositioned;


/**
 * @author Mauricio Coria
 *
 */
public interface MoveGenerator {

	MoveGeneratorResult calculatePseudoMoves(PiecePositioned origen);

}
