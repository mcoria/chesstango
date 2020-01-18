package movegenerators;

import java.util.Map;

import chess.Color;
import chess.DummyBoard;
import chess.Pieza;
import chess.Square;
import iterators.SaltoSquareIterator;

public class ReyAbstractMoveGenerator extends SaltoMoveGenerator {
	
	public ReyAbstractMoveGenerator(Color color) {
		super(color, SaltoSquareIterator.SALTOS_REY);
	}

	protected boolean puedeEnroque(DummyBoard dummyBoard, Map.Entry<Square, Pieza> origen, Square casilleroRey,
			Square casilleroIntermedio, Square casilleroDestino, Map.Entry<Square, Pieza> torre) {
		if ( color.equals(origen.getValue().getColor()) && casilleroRey.equals(origen.getKey()) ) {           	//El rey se encuentra en su lugar
			if (torre.getValue().equals(dummyBoard.getPieza(torre.getKey()))) {								  	//La torre se encuentra en su lugar
				if ( dummyBoard.isEmtpy(casilleroIntermedio)												  	//El casillero intermedio esta vacio
				  && dummyBoard.isEmtpy(casilleroDestino) ) {										  			//El casillero intermedio esta vacio
					if ( !dummyBoard.sepuedeCapturarReyEnSquare(color, casilleroRey) 							//El rey no esta en jaque
					  && !dummyBoard.sepuedeCapturarReyEnSquare(color, casilleroIntermedio)) {					//El rey no puede ser atacado en casillero intermedio
						return true;
					}
				}
			}
		}
		return false;
	}

}
