package movegenerators;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import chess.Color;
import chess.Move;
import chess.Pieza;
import chess.Square;
import iterators.BoardIterator;
import iterators.CardinalSquareIterator;
import iterators.CardinalSquareIterator.Cardinal;
import moveexecutors.CaptureMove;
import moveexecutors.SimpleMove;

public class CardinalMoveGenerator extends AbstractMoveGenerator {
	
	private Color color;
	
	private Cardinal[] direcciones;

	public CardinalMoveGenerator(Color color, Cardinal[] direcciones) {
		this.color = color;
		this.direcciones = direcciones;
	}

	@Override
	public Collection<Move> generateMoves(Map.Entry<Square, Pieza> origen) {
		Collection<Move> moves = createMoveContainer();
		for (Cardinal cardinal : this.direcciones) {
			getPseudoMoves(origen, cardinal, moves);
		}
		return moves;
	}
	
	
	protected void getPseudoMoves(Map.Entry<Square, Pieza> origen, Cardinal cardinal, Collection<Move> moves) {
		Square casillero = origen.getKey();
		BoardIterator iterator = this.tablero.iterator(new CardinalSquareIterator(cardinal, casillero));
		while (iterator.hasNext()) {
		    Entry<Square, Pieza> destino = iterator.next();
		    Pieza pieza = destino.getValue();
		    if(pieza == null){
				this.filter.filterMove(moves, new SimpleMove(origen, destino));
		    } else if(color.equals(pieza.getColor())){
		    	break;
		    } else if(color.opositeColor().equals(pieza.getColor())){
		    	this.filter.filterMove(moves, new CaptureMove(origen, destino));	
		    	break;
		    }
		}
	}

	@Override
	public boolean puedeCapturarRey(Entry<Square, Pieza> origen, Square kingSquare) {
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

	protected boolean puedeCapturarRey(Entry<Square, Pieza> origen, Square kingSquare,
			Cardinal cardinal) {
		Square casillero = origen.getKey();
		BoardIterator iterator = this.tablero.iterator(new CardinalSquareIterator(cardinal, casillero));
		while (iterator.hasNext()) {
		    Entry<Square, Pieza> destino = iterator.next();
		    Pieza pieza = destino.getValue();
		    if(pieza == null){
		    	if(kingSquare.equals(destino.getKey())){
		    		return true;
		    	}
		    	continue;
		    } else if(color.equals(pieza.getColor())){
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
