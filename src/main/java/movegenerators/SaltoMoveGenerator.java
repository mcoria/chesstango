package movegenerators;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import chess.Color;
import chess.Move;
import chess.Pieza;
import chess.Square;
import iterators.BoardIterator;
import iterators.SaltoSquareIterator;
import moveexecutors.CaptureMove;
import moveexecutors.SimpleMove;

public abstract class SaltoMoveGenerator extends AbstractMoveGenerator {
	
	protected Color color;
	private int[][] saltos;
	
	 public SaltoMoveGenerator(Color color, int[][] saltos) {
		this.color = color;
		this.saltos = saltos;
	}

	@Override
	public Collection<Move> generateMoves(Map.Entry<Square, Pieza> origen) {
		Square casillero = origen.getKey();
		BoardIterator iterator = tablero.iterator(new SaltoSquareIterator(casillero, saltos));
		Collection<Move> moves = createMoveContainer();
		while (iterator.hasNext()) {
		    Entry<Square, Pieza> destino = iterator.next();
		    Pieza pieza = destino.getValue();
		    if(pieza == null){
				this.filter.filterMove(moves, new SimpleMove(origen, destino));
		    } else if(color.equals(pieza.getColor())){
		    	continue;
		    } else if(color.opositeColor().equals(pieza.getColor())){
				this.filter.filterMove(moves, new CaptureMove(origen, destino));
		    }
		}
		return moves;
	}
	
	@Override
	public boolean puedeCapturarRey(Entry<Square, Pieza> origen, Square kingSquare) {
		Square casillero = origen.getKey();
		BoardIterator iterator = tablero.iterator(new SaltoSquareIterator(casillero, saltos));
		while (iterator.hasNext()) {
		    Entry<Square, Pieza> destino = iterator.next();
		    Pieza pieza = destino.getValue();
		    if(pieza == null){
		    	if(kingSquare.equals(destino.getKey())){
		    		return true;
		    	}
		    } else if(color.equals(pieza.getColor())){
		    	continue;
		    } else if(color.opositeColor().equals(pieza.getColor())){
		    	if(kingSquare.equals(destino.getKey())){
		    		return true;
		    	}	    	
		    }
		}
		return false;		
	}
		
}
