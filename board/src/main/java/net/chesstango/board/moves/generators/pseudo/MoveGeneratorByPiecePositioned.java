package net.chesstango.board.moves.generators.pseudo;

import net.chesstango.board.PiecePositioned;


/**
 * @author Mauricio Coria
 *
 */	
public interface MoveGeneratorByPiecePositioned {

	MoveGeneratorResult generatePseudoMoves(PiecePositioned origen);

}
