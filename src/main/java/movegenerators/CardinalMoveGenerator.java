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
import iterators.CardinalSquareIterator;
import iterators.CardinalSquareIterator.Cardinal;

public class CardinalMoveGenerator extends AbstractMoveGenerator {
	
	private Color color;
	
	private Cardinal[] direcciones;

	public CardinalMoveGenerator(Color color, Cardinal[] direcciones) {
		this.color = color;
		this.direcciones = direcciones;
	}

	@Override
	protected Collection<Move> getPseudoMoves(Map.Entry<Square, Pieza> origen) {
		Collection<Move> moves = createMoveContainer();
		for (Cardinal cardinal : this.direcciones) {
			moves.addAll(getPseudoMoves(origen, cardinal));
		}
		return moves;
	}
	
	
	protected Collection<Move> getPseudoMoves(Map.Entry<Square, Pieza> origen, Cardinal cardinal) {
		Square casillero = origen.getKey();
		BoardIterator iterator = this.tablero.iterator(new CardinalSquareIterator(cardinal, casillero));
		Collection<Move> moves = createMoveContainer();
		while (iterator.hasNext()) {
		    Entry<Square, Pieza> destino = iterator.next();
		    Pieza pieza = destino.getValue();
		    if(pieza == null){
				this.filter.filterMove(moves, new Move(origen, destino, MoveType.SIMPLE));
		    } else if(color.equals(pieza.getColor())){
		    	break;
		    } else if(color.opositeColor().equals(pieza.getColor())){
		    	this.filter.filterMove(moves, new Move(origen, destino, MoveType.CAPTURA));	
		    	break;
		    }
		}
		return moves;	
	}

	@Override
	public boolean puedeCapturarRey(DummyBoard dummyBoard, Entry<Square, Pieza> origen, Square kingSquare) {
		boolean result = false;
		for (Cardinal cardinal : this.direcciones) {
			if(cardinal.isInDirection(origen.getKey(), kingSquare)){
				result = puedeCapturarRey(dummyBoard, origen, kingSquare, cardinal);
				if(result != false){
					break;
				}
			}
		}
		return result;
	}

	protected boolean puedeCapturarRey(DummyBoard dummyBoard, Entry<Square, Pieza> origen, Square kingSquare,
			Cardinal cardinal) {
		Square casillero = origen.getKey();
		BoardIterator iterator = dummyBoard.iterator(new CardinalSquareIterator(cardinal, casillero));
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
