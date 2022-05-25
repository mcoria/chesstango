package chess.board.movesgenerators.pseudo;

import chess.board.PiecePositioned;


/**
 * @author Mauricio Coria
 *
 */	
public interface MoveGeneratorByPiecePositioned {

	MoveGeneratorResult generatePseudoMoves(PiecePositioned origen);

}
