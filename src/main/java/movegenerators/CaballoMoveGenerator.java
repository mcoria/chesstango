package movegenerators;

import chess.Color;
import chess.PosicionPieza;
import moveexecutors.CaptureMove;
import moveexecutors.SimpleMove;

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
	protected SimpleMove createSimpleMove(PosicionPieza origen, PosicionPieza destino){
		return new SimpleMove(origen, destino);
	}

	@Override
	protected CaptureMove createCaptureMove(PosicionPieza origen, PosicionPieza destino) {
		return new CaptureMove(origen, destino);
	}

	@Override
	public boolean saveMovesInCache() {
		return true;
	}
	
}
