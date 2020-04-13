package movegenerators;

import java.util.Map;

import chess.Color;
import chess.Pieza;
import chess.Square;
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
	
	protected SimpleMove createSimpleMove(Map.Entry<Square, Pieza> origen, Map.Entry<Square, Pieza> destino){
		return new SimpleMove(origen, destino);
	}	
}
