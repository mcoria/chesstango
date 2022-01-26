package chess.pseudomovesgenerators;

import chess.PiecePositioned;


/**
 * @author Mauricio Coria
 *
 */	
public interface MoveGeneratorStrategy {

	MoveGeneratorResult generatePseudoMoves(PiecePositioned origen);

}
