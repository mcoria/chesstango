package chess.pseudomovesgenerators;

import java.util.Iterator;

import chess.Color;
import chess.PiecePositioned;
import chess.Square;
import chess.iterators.Cardinal;
import chess.iterators.CardinalSquareIterator;
import chess.moves.Move;


/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractCardinalMoveGenerator extends AbstractMoveGenerator {
	
	protected abstract Move createSimpleMove(PiecePositioned origen, PiecePositioned destino);
	
	protected abstract Move createCaptureMove(PiecePositioned origen, PiecePositioned destino);		
	
	private final Cardinal[] direcciones;

	public AbstractCardinalMoveGenerator(Color color, Cardinal[] direcciones) {
		super(color);
		this.direcciones = direcciones;
	}

	//TODO: podra utilizarse streams para paralelizar?
	@Override
	public void generateMovesPseudoMoves(PiecePositioned origen) {
		for (Cardinal cardinal : this.direcciones) {
			getPseudoMoves(origen, cardinal);
		}
	}
	
	
	//El calculo de movimientos lo puede hacer en funcion de ColorBoard	
	protected void getPseudoMoves(PiecePositioned origen, Cardinal cardinal) {
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
	public boolean puedeCapturarPosicion(PiecePositioned origen, Square square) {
		for (Cardinal cardinal : this.direcciones) {
			if(cardinal.isInDirection(origen.getKey(), square)){
				return puedeCapturarPosicion(origen, square, cardinal);
			}
		}
		return false;
	}

	protected boolean puedeCapturarPosicion(PiecePositioned origen, Square square,
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

}
