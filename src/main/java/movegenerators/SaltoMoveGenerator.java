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
	public void generateMoves(Map.Entry<Square, Pieza> origen, Collection<Move> moveContainer) {
		Square casillero = origen.getKey();
		BoardIterator iterator = tablero.iterator(new SaltoSquareIterator(casillero, saltos));
		while (iterator.hasNext()) {
		    Entry<Square, Pieza> destino = iterator.next();
		    Pieza pieza = destino.getValue();
		    if(pieza == null){
				this.filter.filterMove(moveContainer, createSimpleMove(origen, destino));
		    } else if(color.equals(pieza.getColor())){
		    	continue;
		    } else if(color.opositeColor().equals(pieza.getColor())){
				this.filter.filterMove(moveContainer, new CaptureMove(origen, destino));
		    }
		}
	}
	
	@Override
	public boolean puedeCapturarRey(Entry<Square, Pieza> origen, Square kingSquare) {
		Square squareOrigen = origen.getKey();
		int fileOrigen = squareOrigen.getFile();
		int rankOrigen = squareOrigen.getRank();
		
		int fileKing = kingSquare.getFile();
		int rankKing = kingSquare.getRank();

		for (int i = 0; i < saltos.length; i++) {
			if(fileOrigen + saltos[i][0] == fileKing && rankOrigen + saltos[i][1] == rankKing) {
				return true;
			}
				
		}
		return false;		
	}
	
	protected abstract SimpleMove createSimpleMove(Map.Entry<Square, Pieza> origen, Map.Entry<Square, Pieza> destino);
	
	protected abstract SimpleMove createCaptureMove(Map.Entry<Square, Pieza> origen, Map.Entry<Square, Pieza> destino);

}
