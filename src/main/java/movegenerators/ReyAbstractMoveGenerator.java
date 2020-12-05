package movegenerators;

import java.util.ArrayList;
import java.util.Collection;

import chess.BoardState;
import chess.Color;
import chess.IsKingInCheck;
import chess.PosicionPieza;
import chess.Square;
import iterators.Cardinal;
import iterators.CardinalSquareIterator;
import layers.ColorBoard;
import layers.DummyBoard;
import moveexecutors.CaptureMove;
import moveexecutors.CaptureReyMove;
import moveexecutors.SimpleMove;
import moveexecutors.SimpleReyMove;
import positioncaptures.Capturer;

public abstract class ReyAbstractMoveGenerator extends SaltoMoveGenerator {
	
	protected BoardState boardState;
	
	protected Capturer capturer = null;
	
	protected IsKingInCheck kingInCheck = () -> false;
	
	protected ColorBoard colorBoard;
	
	protected boolean saveMovesInCache;
	
	public final static int[][] SALTOS_REY = { { 0, 1 }, // Norte
			{ 1, 1 },   // NE
			{ -1, 1 },  // NO
			{ 0, -1 },  // Sur
			{ 1, -1 },  // SE
			{ -1, -1 }, // SO
			{ 1, 0 },   // Este
			{ -1, 0 },  // Oeste
	};
	
	public ReyAbstractMoveGenerator(Color color) {
		super(color, SALTOS_REY);
	}	
	
	//Observar que se valida que el destino no puede ser capturado. No tiene sentido simular el movimiento para validarlo.
	protected boolean puedeEnroqueReina(
			final DummyBoard dummyBoard, 
			final PosicionPieza origen, 
			final PosicionPieza rey,
			final PosicionPieza torre,
			final Square casilleroIntermedioTorre,
			final Square casilleroDestinoRey, 
			final Square casilleroIntermedioRey) {
		if ( rey.equals(origen) ) {           																	//El rey se encuentra en su lugar
			if (torre.getValue().equals(dummyBoard.getPieza(torre.getKey()))) {								  	//La torre se encuentra en su lugar
				if ( dummyBoard.isEmtpy(casilleroIntermedioTorre)												//El casillero intermedio TORRE esta vacio
				  && dummyBoard.isEmtpy(casilleroDestinoRey) 													//El casillero destino REY esta vacio
				  && dummyBoard.isEmtpy(casilleroIntermedioRey)) {										  		//El casillero intermedio REY esta vacio
					if ( !this.kingInCheck.check() 																//El rey no esta en jaque
					  && !capturer.positionCaptured(color.opositeColor(), casilleroIntermedioRey) 				//El rey no puede ser atacado en casillero intermedio
					  && !capturer.positionCaptured(color.opositeColor(), casilleroDestinoRey)){				//El rey no puede ser atacado en casillero destino
						return true;
					}
				}
			}
		}
		return false;
	}
	
	protected boolean puedeEnroqueRey(
			final DummyBoard dummyBoard, 
			final PosicionPieza origen, 
			final PosicionPieza rey,
			final PosicionPieza torre,
			final Square casilleroDestinoRey, 
			final Square casilleroIntermedioRey) {
		if ( rey.equals(origen) ) {           																	//El rey se encuentra en su lugar
			if (torre.getValue().equals(dummyBoard.getPieza(torre.getKey()))) {								  	//La torre se encuentra en su lugar
				if ( dummyBoard.isEmtpy(casilleroDestinoRey) 													//El casillero destino REY esta vacio
				  && dummyBoard.isEmtpy(casilleroIntermedioRey)) {										  		//El casillero intermedio REY esta vacio
					if ( !this.kingInCheck.check()																//El rey no esta en jaque
					  && !capturer.positionCaptured(color.opositeColor(), casilleroIntermedioRey) 				//El rey no puede ser atacado en casillero intermedio
					  && !capturer.positionCaptured(color.opositeColor(), casilleroDestinoRey)){				//El rey no puede ser atacado en casillero destino
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private final Cardinal[] direcciones = new Cardinal[] {Cardinal.NorteEste, Cardinal.SurEste, Cardinal.SurOeste, Cardinal.NorteOeste, Cardinal.Este, Cardinal.Oeste, Cardinal.Norte, Cardinal.Sur};
	
	public Collection<Square> getPinnedSquare(Square kingSquare) {
		Collection<Square> pinnedCollection = new ArrayList<Square>();
		for (Cardinal cardinal : this.direcciones) {
			getPinned(kingSquare, cardinal, pinnedCollection);
		}
		return pinnedCollection;
	}
	
	protected void getPinned(Square kingSquare, Cardinal cardinal, Collection<Square> pinnedCollection) {		
		CardinalSquareIterator iterator = new CardinalSquareIterator(cardinal, kingSquare);
		while ( iterator.hasNext() ) {
		    Square destino = iterator.next();
		    Color colorDestino = colorBoard.getColor(destino);
		    if(colorDestino == null){
		    	continue;
		    } else if(color.equals(colorDestino)){
		    	pinnedCollection.add(destino);
		    	break;
		    } else if(color.opositeColor().equals(colorDestino)){
		    	break;
		    }
		}
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
		return this.saveMovesInCache;
	}
	
	public void setCapturer(Capturer capturer) {
		this.capturer = capturer;
	}

	public void setBoardState(BoardState boardState) {
		this.boardState = boardState;
	}
	
	public void setColorBoard(ColorBoard colorBoard) {
		this.colorBoard = colorBoard;
	}

	public void setKingInCheck(IsKingInCheck kingInCheck) {
		this.kingInCheck = kingInCheck;
	}	
	
}
