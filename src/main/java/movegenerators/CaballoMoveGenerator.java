package movegenerators;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;
import iterators.BoardIterator;
import iterators.SaltoSquareIterator;

public class CaballoMoveGenerator extends AbstractMoveGenerator {
	
	private Color color;
	public CaballoMoveGenerator(Color color) {
		this.color = color;
	}
	
	@Override
	public Set<Move> getPseudoMoves(DummyBoard tablero, Square casillero) {
		BoardIterator iterator = tablero.iterator(new SaltoSquareIterator(casillero, SaltoSquareIterator.SALTOS_CABALLO));
		Set<Move> moves = new HashSet<Move>();
		while (iterator.hasNext()) {
		    Entry<Square, Pieza> entry = iterator.next();
		    Square destino = entry.getKey();
		    Pieza pieza = entry.getValue();
		    if(pieza == null){
		    	Move move = new Move(casillero, destino);
		    	moves.add(move);
		    } else if(color.equals(pieza.getColor())){
		    	continue;
		    } else if(color.opositeColor().equals(pieza.getColor())){
		    	Move move = new Move(casillero, destino, pieza);
		    	moves.add(move);		    	
		    }
		}
		return moves;
	}

}
