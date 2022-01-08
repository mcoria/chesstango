package chess.movesgenerators;

import chess.Color;
import chess.PosicionPieza;
import chess.moves.Move;


/**
 * @author Mauricio Coria
 *
 */
public class CaballoMoveGenerator extends AbstractJumpMoveGenerator {
	
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
	
	public CaballoMoveGenerator(Color color) {
		super(color, SALTOS_CABALLO);
	}
	
	@Override
	protected Move createSimpleMove(PosicionPieza origen, PosicionPieza destino) {
		return this.moveFactory.createSimpleMove(origen, destino);
	}


	@Override
	protected Move createCaptureMove(PosicionPieza origen, PosicionPieza destino) {
		return this.moveFactory.createCaptureMove(origen, destino);
	}	
	
}
