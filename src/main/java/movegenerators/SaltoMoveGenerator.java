package movegenerators;

import java.util.Map;
import java.util.Map.Entry;

import chess.Color;
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
	public void generateMoves(Map.Entry<Square, Pieza> origen) {
		Square casillero = origen.getKey();
		BoardIterator iterator = tablero.iterator(new SaltoSquareIterator(casillero, saltos));
		while (iterator.hasNext()) {
		    Entry<Square, Pieza> destino = iterator.next();
		    Pieza pieza = destino.getValue();
		    if(pieza == null){
				this.filter.filterMove(this.moveContainer, new SimpleMove(origen, destino));
		    } else if(color.equals(pieza.getColor())){
		    	continue;
		    } else if(color.opositeColor().equals(pieza.getColor())){
				this.filter.filterMove(this.moveContainer, new CaptureMove(origen, destino));
		    }
		}
	}
	
	// ESTO SE PUEDE MEJORAR, NO HACE FALTA INSTANCIAR SaltoSquareIterator
	@Override
	public boolean puedeCapturarRey(Entry<Square, Pieza> origen, Square kingSquare) {
		Square casillero = origen.getKey();
		SaltoSquareIterator iterator = new SaltoSquareIterator(casillero, saltos);
		while (iterator.hasNext()) {
		    Square salto = iterator.next();
	    	if(kingSquare.equals(salto)){
	    		return true;
	    	}
		}
		return false;		
	}

}
