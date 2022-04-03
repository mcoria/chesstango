package chess.board.pseudomovesgenerators.strategies;

import chess.board.Color;
import chess.board.PiecePositioned;
import chess.board.moves.Move;


/**
 * @author Mauricio Coria
 *
 */
public class KnightMoveGenerator extends AbstractJumpMoveGenerator {
	
	public final static int[][] SALTOS_CABALLO = { 
			//Arriba
			{ -1, 2 }, 
			{ 1, 2 },
			
			//Derecha
			{ 2, -1 },
			{ 2, 1 },
			
			//Izquierda
			{ -2, -1 },
			{ -2, 1 },
			
			//Abajo
			{ -1, -2 }, 
			{ 1, -2 },
	};	
	
	public KnightMoveGenerator(Color color) {
		super(color, SALTOS_CABALLO);
	}
	
	@Override
	protected Move createSimpleMove(PiecePositioned origen, PiecePositioned destino) {
		return this.moveFactory.createSimpleMove(origen, destino);
	}


	@Override
	protected Move createCaptureMove(PiecePositioned origen, PiecePositioned destino) {
		return this.moveFactory.createCaptureMove(origen, destino);
	}	
	
}
