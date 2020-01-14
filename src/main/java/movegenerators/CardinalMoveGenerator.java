package movegenerators;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;
import iterators.BoardIterator;
import iterators.CardinalSquareIterator;
import iterators.CardinalSquareIterator.Cardinal;
import moveexecutors.CaptureMoveExecutor;
import moveexecutors.SimpleMoveExecutor;

public class CardinalMoveGenerator extends AbstractMoveGenerator {
	
	private Color color;
	
	private Cardinal[] direcciones;

	public CardinalMoveGenerator(Color color, Cardinal[] direcciones) {
		this.color = color;
		this.direcciones = direcciones;
	}

	@Override
	public Set<Move> getPseudoMoves(DummyBoard dummyBoard, Map.Entry<Square, Pieza> origen) {
		Set<Move> moves = createMoveContainer();
		for (Cardinal cardinal : this.direcciones) {
			moves.addAll(getPseudoMoves(dummyBoard, origen, cardinal));
		}
		return moves;
	}
	
	
	public Set<Move> getPseudoMoves(DummyBoard tablero, Map.Entry<Square, Pieza> origen, Cardinal cardinal) {
		Square casillero = origen.getKey();
		BoardIterator iterator = tablero.iterator(new CardinalSquareIterator(cardinal, casillero));
		Set<Move> moves = new HashSet<Move>();
		while (iterator.hasNext()) {
		    Entry<Square, Pieza> entry = iterator.next();
		    Square destino = entry.getKey();
		    Pieza pieza = entry.getValue();
		    if(pieza == null){
		    	Move move = new Move(casillero, destino, new SimpleMoveExecutor(origen.getValue()));
		    	moves.add(move);
		    } else if(color.equals(pieza.getColor())){
		    	break;
		    } else if(color.opositeColor().equals(pieza.getColor())){
		    	Move move = new Move(casillero, destino, new CaptureMoveExecutor(origen.getValue(), pieza));
		    	moves.add(move);
		    	break;
		    }
		}
		return moves;	
	}


}
