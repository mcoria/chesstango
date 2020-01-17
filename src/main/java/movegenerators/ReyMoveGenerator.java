package movegenerators;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Move.MoveType;
import chess.Pieza;
import chess.Square;
import iterators.BoardIterator;
import iterators.SaltoSquareIterator;

public class ReyMoveGenerator extends AbstractMoveGenerator {
	
	private Color color;
	public ReyMoveGenerator(Color color) {
		this.color = color;
	}
	
	@Override
	public Set<Move> getPseudoMoves(DummyBoard tablero, Map.Entry<Square, Pieza> origen) {
		Square casillero = origen.getKey();
		BoardIterator iterator = tablero.iterator(new SaltoSquareIterator(casillero, SaltoSquareIterator.SALTOS_REY));
		Set<Move> moves = createMoveContainer();
		while (iterator.hasNext()) {
		    Entry<Square, Pieza> destino = iterator.next();
		    Pieza pieza = destino.getValue();
		    if(pieza == null){
		    	Move move = new Move(origen, destino, MoveType.SIMPLE);
		    	moves.add(move);
		    } else if(color.equals(pieza.getColor())){
		    	continue;
		    } else if(color.opositeColor().equals(pieza.getColor())){
		    	Move move = new Move(origen, destino, MoveType.CAPTURA);
		    	moves.add(move);		    	
		    }
		}
		return moves;
	}

}
