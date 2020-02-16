package movegenerators;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Move.MoveType;
import chess.Pieza;
import chess.Square;
import iterators.BoardIterator;
import iterators.SaltoSquareIterator;

public abstract class SaltoMoveGenerator extends AbstractMoveGenerator {
	
	protected Color color;
	private int[][] saltos;
	
	 public SaltoMoveGenerator(Color color, int[][] saltos) {
		this.color = color;
		this.saltos = saltos;
	}

	@Override
	protected Collection<Move> getPseudoMoves(Map.Entry<Square, Pieza> origen) {
		Square casillero = origen.getKey();
		BoardIterator iterator = tablero.iterator(new SaltoSquareIterator(casillero, saltos));
		Collection<Move> moves = createMoveContainer();
		while (iterator.hasNext()) {
		    Entry<Square, Pieza> destino = iterator.next();
		    Pieza pieza = destino.getValue();
		    if(pieza == null){
				this.filter.filterMove(moves, new Move(origen, destino, MoveType.SIMPLE));
		    } else if(color.equals(pieza.getColor())){
		    	continue;
		    } else if(color.opositeColor().equals(pieza.getColor())){
				this.filter.filterMove(moves, new Move(origen, destino, MoveType.CAPTURA));
		    }
		}
		return moves;
	}
	
	@Override
	public boolean puedeCapturarRey(DummyBoard tablero, Entry<Square, Pieza> origen, Square kingSquare) {
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
