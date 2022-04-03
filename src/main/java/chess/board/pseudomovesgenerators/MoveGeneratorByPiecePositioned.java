package chess.board.pseudomovesgenerators;

import chess.board.PiecePositioned;


/**
 * @author Mauricio Coria
 *
 */	
public interface MoveGeneratorByPiecePositioned {

	MoveGeneratorResult generatePseudoMoves(PiecePositioned origen);

}
