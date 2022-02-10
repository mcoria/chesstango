package chess.pseudomovesgenerators;

import chess.PiecePositioned;


/**
 * @author Mauricio Coria
 *
 */	
public interface MoveGeneratorByPiecePositioned {

	MoveGeneratorResult generatePseudoMoves(PiecePositioned origen);

}
