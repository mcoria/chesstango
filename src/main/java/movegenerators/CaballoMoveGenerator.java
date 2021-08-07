package movegenerators;

import chess.Color;
import chess.PosicionPieza;
import moveexecutors.Move;

public class CaballoMoveGenerator extends SaltoMoveGenerator {
	
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
	public boolean saveMovesInCache() {
		return true;
	}

	@Override
	public boolean hasCapturePeonPasante() {
		return false;
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
