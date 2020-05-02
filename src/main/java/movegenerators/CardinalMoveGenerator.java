package movegenerators;

import java.util.Iterator;

import chess.BoardCache;
import chess.Color;
import chess.Move;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import iterators.Cardinal;
import iterators.CardinalSquareIterator;
import moveexecutors.CaptureMove;
import moveexecutors.SimpleMove;

public class CardinalMoveGenerator extends AbstractMoveGenerator {
	
	private final Cardinal[] direcciones;
	
	protected BoardCache boardCache;

	public CardinalMoveGenerator(Color color, Cardinal[] direcciones) {
		super(color);
		this.direcciones = direcciones;
	}

	@Override
	public void generateMoves(PosicionPieza origen) {
		for (Cardinal cardinal : this.direcciones) {
			getPseudoMoves(origen, cardinal);
		}
	}
	
	
	protected void getPseudoMoves(PosicionPieza origen, Cardinal cardinal) {
		Square casillero = origen.getKey();
		Iterator<PosicionPieza> iterator = this.tablero.iterator(new CardinalSquareIterator(cardinal, casillero));
		while (iterator.hasNext()) {
		    PosicionPieza destino = iterator.next();
		    squareContainer.add(destino.getKey());
		    Pieza pieza = destino.getValue();
		    if(pieza == null){
		    	Move move = new SimpleMove(origen, destino);
		    	moveContainer.add(move);
		    } else if(color.equals(pieza.getColor())){
		    	break;
		    } else if(color.opositeColor().equals(pieza.getColor())){
		    	Move move = new CaptureMove(origen, destino);
		    	moveContainer.add(move);
		    	break;
		    }
		}
	}

	//Reveer estas dos operaciones, se pueden mejorar
	@Override
	public boolean puedeCapturarPosicion(PosicionPieza origen, Square kingSquare) {
		for (Cardinal cardinal : this.direcciones) {
			if(cardinal.isInDirection(origen.getKey(), kingSquare)){
				return puedeCapturarPosicion(origen, kingSquare, cardinal);
			}
		}
		return false;
	}

	protected boolean puedeCapturarPosicion(PosicionPieza origen, Square kingSquare,
			Cardinal cardinal) {
		Square casillero = origen.getKey();
		CardinalSquareIterator iterator = new CardinalSquareIterator(cardinal, casillero);
		while ( iterator.hasNext() ) {
		    Square destino = iterator.next();
		    if(this.boardCache.isEmpty(destino)){
		    	if(kingSquare.equals(destino)){
		    		return true;
		    	}
		    	continue;
		    } else {
		    	if(kingSquare.equals(destino)){
		    		return true;
		    	}		    	
		    	break;
			}
		}
		return false;
	}
	

	public void setBoardCache(BoardCache boardCache) {
		this.boardCache = boardCache;
	}

}
