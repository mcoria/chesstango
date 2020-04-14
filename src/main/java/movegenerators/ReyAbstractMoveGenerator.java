package movegenerators;

import java.util.Map;
import java.util.Map.Entry;

import chess.Color;
import chess.DummyBoard;
import chess.Pieza;
import chess.Square;
import moveexecutors.CaptureMove;
import moveexecutors.CaptureReyMove;
import moveexecutors.SimpleMove;
import moveexecutors.SimpleReyMove;

public abstract class ReyAbstractMoveGenerator extends SaltoMoveGenerator {
	
	public final static int[][] SALTOS_REY = { { 0, 1 }, // Norte
			{ 1, 1 }, // NE
			{ -1, 1 }, // NO
			{ 0, -1 }, // Sur
			{ 1, -1 }, // SE
			{ -1, -1 }, // SO
			{ 1, 0 }, // Este
			{ -1, 0 }, // Oeste
	};
	
	public ReyAbstractMoveGenerator(Color color) {
		super(color, SALTOS_REY);
	}
	
	
	protected boolean puedeEnroqueReina(
			DummyBoard dummyBoard, 
			Map.Entry<Square, Pieza> origen, 
			Map.Entry<Square, Pieza> rey,
			Map.Entry<Square, Pieza> torre,
			Square casilleroIntermedioTorre,
			Square casilleroDestinoRey, 
			Square casilleroIntermedioRey) {
		if ( rey.equals(origen) ) {           																	//El rey se encuentra en su lugar
			if (torre.getValue().equals(dummyBoard.getPieza(torre.getKey()))) {								  	//La torre se encuentra en su lugar
				if ( dummyBoard.isEmtpy(casilleroIntermedioTorre)												//El casillero intermedio TORRE esta vacio
				  && dummyBoard.isEmtpy(casilleroDestinoRey) 													//El casillero destino REY esta vacio
				  && dummyBoard.isEmtpy(casilleroIntermedioRey)) {										  		//El casillero intermedio REY esta vacio
					if ( !dummyBoard.sepuedeCapturarReyEnSquare(color, rey.getKey()) 							//El rey no esta en jaque
					  && !dummyBoard.sepuedeCapturarReyEnSquare(color, casilleroIntermedioRey)) {				//El rey no puede ser atacado en casillero intermedio
						return true;
					}
				}
			}
		}
		return false;
	}
	
	protected boolean puedeEnroqueRey(
			DummyBoard dummyBoard, 
			Map.Entry<Square, Pieza> origen, 
			Map.Entry<Square, Pieza> rey,
			Map.Entry<Square, Pieza> torre,
			Square casilleroDestinoRey, 
			Square casilleroIntermedioRey) {
		if ( rey.equals(origen) ) {           																	//El rey se encuentra en su lugar
			if (torre.getValue().equals(dummyBoard.getPieza(torre.getKey()))) {								  	//La torre se encuentra en su lugar
				if ( dummyBoard.isEmtpy(casilleroDestinoRey) 													//El casillero destino REY esta vacio
				  && dummyBoard.isEmtpy(casilleroIntermedioRey)) {										  		//El casillero intermedio REY esta vacio
					if ( !dummyBoard.sepuedeCapturarReyEnSquare(color, rey.getKey()) 							//El rey no esta en jaque
					  && !dummyBoard.sepuedeCapturarReyEnSquare(color, casilleroIntermedioRey)) {				//El rey no puede ser atacado en casillero intermedio
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	protected SimpleMove createSimpleMove(Entry<Square, Pieza> origen, Entry<Square, Pieza> destino) {
		return new SimpleReyMove(origen, destino);
	}

	@Override
	protected CaptureMove createCaptureMove(Entry<Square, Pieza> origen, Entry<Square, Pieza> destino) {
		return new CaptureReyMove(origen, destino);
	}	

}
