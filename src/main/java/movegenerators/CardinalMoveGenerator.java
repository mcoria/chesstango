package movegenerators;

import java.util.Iterator;

import chess.Color;
import chess.Move;
import chess.PosicionPieza;
import chess.Square;
import iterators.Cardinal;
import iterators.CardinalSquareIterator;

public class CardinalMoveGenerator extends AbstractMoveGenerator {
	
	private final Cardinal[] direcciones;

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
	
	
	//El calculo de movimientos lo puede hacer en funcion de ColorBoard	
	protected void getPseudoMoves(PosicionPieza origen, Cardinal cardinal) {
		Square casillero = origen.getKey();
		Iterator<Square> iterator = new CardinalSquareIterator(casillero, cardinal);
		while (iterator.hasNext()) {
			Square destino = iterator.next();
			this.result.affectedByContainerAdd(destino);
			Color colorDestino = colorBoard.getColor(destino);
			if (colorDestino == null) {
				Move move = createSimpleMove(origen, tablero.getPosicion(destino));
				result.moveContainerAdd(move);
			} else if (color.opositeColor().equals(colorDestino)) {
				Move move = createCaptureMove(origen, tablero.getPosicion(destino));
				result.moveContainerAdd(move);
				break;
			} else { // if(color.equals(pieza.getColor())){
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

	@Override
	public boolean saveMovesInCache() {
		return true;
	}

	@Override
	public boolean hasCapturePeonPasante() {
		return false;
	}

}
