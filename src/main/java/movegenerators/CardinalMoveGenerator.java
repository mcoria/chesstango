package movegenerators;

import java.util.Iterator;

import chess.Color;
import chess.Move;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import iterators.Cardinal;
import iterators.CardinalSquareIterator;
import layers.ColorBoard;
import moveexecutors.CaptureMove;
import moveexecutors.SimpleMove;

public class CardinalMoveGenerator extends AbstractMoveGenerator {
	
	private final Cardinal[] direcciones;
	
	protected ColorBoard colorBoard;

	public CardinalMoveGenerator(Color color, Cardinal[] direcciones) {
		super(color);
		this.direcciones = direcciones;
	}

	@Override
	public void generateMovesPseudoMoves(PosicionPieza origen) {
		for (Cardinal cardinal : this.direcciones) {
			getPseudoMoves(origen, cardinal);
		}
	}
	
	
	protected void getPseudoMoves(PosicionPieza origen, Cardinal cardinal) {
		Square casillero = origen.getKey();
		Iterator<PosicionPieza> iterator = this.tablero.iterator(new CardinalSquareIterator(casillero, cardinal));
		while (iterator.hasNext()) {
		    PosicionPieza destino = iterator.next();
		    this.result.affectedByContainerAdd(destino.getKey());
		    Pieza pieza = destino.getValue();
		    if(pieza == null){
		    	Move move = new SimpleMove(origen, destino);
		    	result.moveContainerAdd(move);
		    } else if(color.equals(pieza.getColor())){
		    	break;
		    } else if(color.opositeColor().equals(pieza.getColor())){
		    	Move move = new CaptureMove(origen, destino);
		    	result.moveContainerAdd(move);
		    	break;
		    }
		}
	}

	@Override
	public boolean puedeCapturarPosicion(PosicionPieza origen, Square square) {
		for (Cardinal cardinal : this.direcciones) {
			if(cardinal.isInDirection(origen.getKey(), square)){
				return puedeCapturarPosicion(origen, square, cardinal);
			}
		}
		return false;
	}

	protected boolean puedeCapturarPosicion(PosicionPieza origen, Square square,
			Cardinal cardinal) {
		Square casillero = origen.getKey();
		CardinalSquareIterator iterator = new CardinalSquareIterator(casillero, cardinal);
		while ( iterator.hasNext() ) {
		    Square destino = iterator.next();
	    	if(square.equals(destino)){
	    		return true;
	    	}
		    if(this.colorBoard.isEmpty(destino)){
		    	continue;
		    } else {		    	
		    	break;
			}
		}
		return false;
	}
	

	public void setColorBoard(ColorBoard colorBoard) {
		this.colorBoard = colorBoard;
	}

	@Override
	public boolean saveMovesInCache() {
		return true;
	}

}
