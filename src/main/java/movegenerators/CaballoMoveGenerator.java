package movegenerators;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;
import iterators.BoardIterator;
import iterators.SaltoSquareIterator;
import moveexecutors.CaptureMoveExecutor;
import moveexecutors.SimpleMoveExecutor;

public class CaballoMoveGenerator extends AbstractMoveGenerator {
	
	private Color color;
	public CaballoMoveGenerator(Color color) {
		this.color = color;
	}
	
	@Override
	public Set<Move> getPseudoMoves(DummyBoard tablero, Map.Entry<Square, Pieza> origen) {
		Square casillero = origen.getKey();
		BoardIterator iterator = tablero.iterator(new SaltoSquareIterator(casillero, SaltoSquareIterator.SALTOS_CABALLO));
		Set<Move> moves = createMoveContainer();
		while (iterator.hasNext()) {
		    Entry<Square, Pieza> destino = iterator.next();
		    Pieza pieza = destino.getValue();
		    if(pieza == null){
		    	Move move = new Move(origen, destino, new SimpleMoveExecutor(origen.getValue()));
		    	moves.add(move);
		    } else if(color.equals(pieza.getColor())){
		    	continue;
		    } else if(color.opositeColor().equals(pieza.getColor())){
		    	Move move = new Move(origen, destino, new CaptureMoveExecutor(origen.getValue(), pieza));
		    	moves.add(move);		    	
		    }
		}
		return moves;
	}

}
