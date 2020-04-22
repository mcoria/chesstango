package movegenerators;

import java.util.Collection;
import java.util.Iterator;

import chess.Color;
import chess.Move;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import iterators.Cardinal;
import iterators.CardinalSquareIterator;
import moveexecutors.CaptureMove;
import moveexecutors.SimpleMove;

public class CardinalMoveGenerator extends AbstractMoveGenerator {
	
	private final Cardinal[] direcciones;

	public CardinalMoveGenerator(Color color, Cardinal[] direcciones) {
		super(color);
		this.direcciones = direcciones;
	}

	@Override
	public void generateMoves(PosicionPieza origen, Collection<Move> moveContainer) {
		for (Cardinal cardinal : this.direcciones) {
			getPseudoMoves(origen, cardinal, moveContainer);
		}
	}
	
	
	protected void getPseudoMoves(PosicionPieza origen, Cardinal cardinal, Collection<Move> moveContainer) {
		Square casillero = origen.getKey();
		Iterator<PosicionPieza> iterator = this.tablero.iterator(new CardinalSquareIterator(cardinal, casillero));
		while (iterator.hasNext()) {
		    PosicionPieza destino = iterator.next();
		    Pieza pieza = destino.getValue();
		    if(pieza == null){
		    	Move move = new SimpleMove(origen, destino);
				if(this.filter.filterMove(move)){
					moveContainer.add(move);
				}
					
		    } else if(color.equals(pieza.getColor())){
		    	break;
		    } else if(color.opositeColor().equals(pieza.getColor())){
		    	Move move = new CaptureMove(origen, destino);
				if(this.filter.filterMove(move)){
					moveContainer.add(move);
				}	
		    	break;
		    }
		}
	}

	@Override
	public boolean puedeCapturarPosicion(PosicionPieza origen, Square kingSquare) {
		boolean result = false;
		for (Cardinal cardinal : this.direcciones) {
			if(cardinal.isInDirection(origen.getKey(), kingSquare)){
				result = puedeCapturarRey(origen, kingSquare, cardinal);
				if(result != false){
					break;
				}
			}
		}
		return result;
	}

	protected boolean puedeCapturarRey(PosicionPieza origen, Square kingSquare,
			Cardinal cardinal) {
		Square casillero = origen.getKey();
		Iterator<PosicionPieza> iterator = this.tablero.iterator(new CardinalSquareIterator(cardinal, casillero));
		while (iterator.hasNext()) {
		    PosicionPieza destino = iterator.next();
		    Pieza pieza = destino.getValue();
		    if(pieza == null){
		    	if(kingSquare.equals(destino.getKey())){
		    		return true;
		    	}
		    	continue;
		    } else if(color.equals(pieza.getColor())){
		    	if(kingSquare.equals(destino.getKey())){
		    		return true;
		    	}		    	
		    	break;
		    } else if(color.opositeColor().equals(pieza.getColor())){
		    	if(kingSquare.equals(destino.getKey())){
		    		return true;
		    	}
		    	break;
		    }
		}
		return false;
	}

}
