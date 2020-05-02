package movegenerators;

import chess.BoardState;
import chess.Color;
import chess.DummyBoard;
import chess.PosicionPieza;
import chess.PositionCaptured;
import chess.Square;
import moveexecutors.CaptureMove;
import moveexecutors.CaptureReyMove;
import moveexecutors.SimpleMove;
import moveexecutors.SimpleReyMove;

public abstract class ReyAbstractMoveGenerator extends SaltoMoveGenerator {
	
	protected PositionCaptured positionCaptured = (Color color, Square square) -> false;
	
	protected BoardState boardState;
	
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
			PosicionPieza origen, 
			PosicionPieza rey,
			PosicionPieza torre,
			Square casilleroIntermedioTorre,
			Square casilleroDestinoRey, 
			Square casilleroIntermedioRey) {
		if ( rey.equals(origen) ) {           																	//El rey se encuentra en su lugar
			if (torre.getValue().equals(dummyBoard.getPieza(torre.getKey()))) {								  	//La torre se encuentra en su lugar
				if ( dummyBoard.isEmtpy(casilleroIntermedioTorre)												//El casillero intermedio TORRE esta vacio
				  && dummyBoard.isEmtpy(casilleroDestinoRey) 													//El casillero destino REY esta vacio
				  && dummyBoard.isEmtpy(casilleroIntermedioRey)) {										  		//El casillero intermedio REY esta vacio
					if ( !positionCaptured.check(color.opositeColor(), rey.getKey()) 							//El rey no esta en jaque
					  && !positionCaptured.check(color.opositeColor(), casilleroIntermedioRey) 					//El rey no puede ser atacado en casillero intermedio
					  && !positionCaptured.check(color.opositeColor(), casilleroDestinoRey)){					//El rey no puede ser atacado en casillero destino
						return true;
					}
				}
			}
		}
		return false;
	}
	
	protected boolean puedeEnroqueRey(
			DummyBoard dummyBoard, 
			PosicionPieza origen, 
			PosicionPieza rey,
			PosicionPieza torre,
			Square casilleroDestinoRey, 
			Square casilleroIntermedioRey) {
		if ( rey.equals(origen) ) {           																	//El rey se encuentra en su lugar
			if (torre.getValue().equals(dummyBoard.getPieza(torre.getKey()))) {								  	//La torre se encuentra en su lugar
				if ( dummyBoard.isEmtpy(casilleroDestinoRey) 													//El casillero destino REY esta vacio
				  && dummyBoard.isEmtpy(casilleroIntermedioRey)) {										  		//El casillero intermedio REY esta vacio
					if ( !positionCaptured.check(color.opositeColor(), rey.getKey()) 							//El rey no esta en jaque
					  && !positionCaptured.check(color.opositeColor(), casilleroIntermedioRey) 					//El rey no puede ser atacado en casillero intermedio
					  && !positionCaptured.check(color.opositeColor(), casilleroDestinoRey)){					//El rey no puede ser atacado en casillero destino
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	protected SimpleMove createSimpleMove(PosicionPieza origen, PosicionPieza destino) {
		return new SimpleReyMove(origen, destino);
	}

	@Override
	protected CaptureMove createCaptureMove(PosicionPieza origen, PosicionPieza destino) {
		return new CaptureReyMove(origen, destino);
	}
	
	@Override
	public boolean saveMovesInCache() {
		return false;
	}	

	public void setPositionCaptured(PositionCaptured positionCaptured) {
		this.positionCaptured = positionCaptured;
	}


	public void setBoardState(BoardState boardState) {
		this.boardState = boardState;
	}	

}
