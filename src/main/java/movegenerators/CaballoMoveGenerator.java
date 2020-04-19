package movegenerators;

import java.util.Collection;

import chess.Color;
import chess.Move;
import chess.Pieza;
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
	protected void addMoveIfValid(PosicionPieza origen, PosicionPieza destino, Collection<Move> moveContainer) {
		Pieza pieza = destino.getValue();
		if(pieza == null){
			Move move = createSimpleMove(origen, destino);
			if(this.filter.filterMove(move)){
				moveContainer.add(move);
			}					
		} else if(color.equals(pieza.getColor())){
			return;
		} else if(color.opositeColor().equals(pieza.getColor())){
			Move move = createCaptureMove(origen, destino);
			if(this.filter.filterMove(move)){
				moveContainer.add(move);
			}				
		}
	}	
}
