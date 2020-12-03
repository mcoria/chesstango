package positioncaptures;

import java.util.Iterator;

import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import iterators.Cardinal;
import iterators.CardinalSquareIterator;
import iterators.SaltoSquareIterator;
import layers.DummyBoard;
import movegenerators.CaballoMoveGenerator;

/*
 * Esto se podria mejorar mas, en vez de buscar dummyBoard podriamos buscar en ColorBoard y luego si el color coincide ahi si buscar en dummy
 */
public class ImprovedCapturerColor {
	
	private DummyBoard dummyBoard = null; 
	
	private Pieza torre;
	private Pieza alfil;
	private Pieza reyna;
	private Pieza caballo;
	final int[][] saltosPeon;
	final Pieza peon;	
	
	private final int[][] casillerosPeonBlanco = {
		{ -1, -1 }, 
		{ 1, -1 }
	};
	
	private final int[][] casillerosPeonNegro = {
		{ -1, 1 }, 
		{ 1, 1 }
	};
	
	public ImprovedCapturerColor(Color color, DummyBoard dummyBoard) {
		this.dummyBoard = dummyBoard;
		if (Color.BLANCO.equals(color)) {
			torre = Pieza.TORRE_BLANCO;
			alfil = Pieza.ALFIL_BLANCO;
			reyna = Pieza.REINA_BLANCO;
			caballo = Pieza.CABALLO_BLANCO;
			saltosPeon = casillerosPeonBlanco;
			peon = Pieza.PEON_BLANCO;			
		} else {
			torre = Pieza.TORRE_NEGRO;
			alfil = Pieza.ALFIL_NEGRO;
			reyna = Pieza.REINA_NEGRO;;
			caballo = Pieza.CABALLO_NEGRO;
			saltosPeon = casillerosPeonNegro;
			peon = Pieza.PEON_NEGRO;			
		}		
	}

	public boolean positionCaptured(Square square) {
		if(positionCapturedByCaballo(square)	||
		   positionCapturedByTorre(square)	||
		   positionCapturedByAlfil(square)   ||
		   positionCapturedByPeon(square)){
			return true;
		}
		return false;
	}
	
	private Cardinal[]  direccionAlfil = new Cardinal[] {Cardinal.NorteEste, Cardinal.SurEste, Cardinal.SurOeste, Cardinal.NorteOeste};
	private boolean positionCapturedByAlfil(Square square) {
		for (Cardinal cardinal : this.direccionAlfil) {
			if(cardinalPositionCapturedByPieza(alfil, reyna, square, cardinal)){
				return true;
			}
		}
		return false;
	}

	private Cardinal[]  direccionTorre = new Cardinal[] {Cardinal.Este, Cardinal.Oeste, Cardinal.Norte, Cardinal.Sur};
	private boolean positionCapturedByTorre(Square square) {		
		for (Cardinal cardinal : this.direccionTorre) {
			if(cardinalPositionCapturedByPieza(torre, reyna, square, cardinal)){
				return true;
			}
		}
		return false;
	}

	private boolean cardinalPositionCapturedByPieza(Pieza torreOalfil, Pieza reyna, Square square, Cardinal cardinal) {
		Iterator<PosicionPieza> iterator = this.dummyBoard.iterator(new CardinalSquareIterator(cardinal, square));
		while (iterator.hasNext()) {
		    PosicionPieza destino = iterator.next();
		    Pieza pieza = destino.getValue();
		    if(pieza == null){
		    	continue;
		    } else if(reyna.equals(pieza)  || torreOalfil.equals(pieza)){
		    	return true;
		    } else {
		    	break;
		    }
		}
		return false;
	}

	private boolean positionCapturedByCaballo(Square square) {
		Iterator<PosicionPieza> iterator = dummyBoard.iterator(new SaltoSquareIterator(square, CaballoMoveGenerator.SALTOS_CABALLO));
		while (iterator.hasNext()) {
		    PosicionPieza destino = iterator.next();
		    if(caballo.equals(destino.getValue())){
		    	return true;
		    }
		}
		return false;
	}


	private boolean positionCapturedByPeon(Square square) {
		Iterator<PosicionPieza> iterator = dummyBoard.iterator(new SaltoSquareIterator(square, saltosPeon));
		while (iterator.hasNext()) {
		    PosicionPieza destino = iterator.next();
		    if(peon.equals(destino.getValue())){
		    	return true;
		    }
		}
		return false;
	}

}
