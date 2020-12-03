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
 * Incluso esta clase se puede descomponer en dos, una para blancos y otra para negros
 */
public class ImprovedCapturer implements Capturer {
	
	private DummyBoard dummyBoard = null; 
	
	public ImprovedCapturer(DummyBoard dummyBoard) {
		this.dummyBoard = dummyBoard;
	}

	@Override
	public boolean positionCaptured(Color color, Square square) {
		if(positionCapturedByPeon(color, square) ||
		   positionCapturedByCaballo(color, square)	||
		   positionCapturedByTorre(color, square)	||
		   positionCapturedByAlfil(color, square)){
			return true;
		}
		return false;
	}
	
	private Cardinal[]  direccionAlfil = new Cardinal[] {Cardinal.NorteEste, Cardinal.SurEste, Cardinal.SurOeste, Cardinal.NorteOeste};
	private boolean positionCapturedByAlfil(Color color, Square square) {
		final Pieza alfil;
		final Pieza reyna;
		if (Color.BLANCO.equals(color)) {
			alfil = Pieza.ALFIL_BLANCO;
			reyna = Pieza.REINA_BLANCO;;
		} else {
			alfil = Pieza.ALFIL_NEGRO;
			reyna = Pieza.REINA_NEGRO;;
		}		
		for (Cardinal cardinal : this.direccionAlfil) {
			if(cardinalPositionCapturedByPieza(alfil, reyna, square, cardinal)){
				return true;
			}
		}
		return false;
	}

	private Cardinal[]  direccionTorre = new Cardinal[] {Cardinal.Este, Cardinal.Oeste, Cardinal.Norte, Cardinal.Sur};
	private boolean positionCapturedByTorre(Color color, Square square) {
		final Pieza torre;
		final Pieza reyna;
		if (Color.BLANCO.equals(color)) {
			torre = Pieza.TORRE_BLANCO;
			reyna = Pieza.REINA_BLANCO;;
		} else {
			torre = Pieza.TORRE_NEGRO;
			reyna = Pieza.REINA_NEGRO;;
		}		
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

	private boolean positionCapturedByCaballo(Color color, Square square) {
		final Pieza caballo;
		if (Color.BLANCO.equals(color)) {
			caballo = Pieza.CABALLO_BLANCO;
		} else {
			caballo = Pieza.CABALLO_NEGRO;
		}
		Iterator<PosicionPieza> iterator = dummyBoard.iterator(new SaltoSquareIterator(square, CaballoMoveGenerator.SALTOS_CABALLO));
		while (iterator.hasNext()) {
		    PosicionPieza destino = iterator.next();
		    if(caballo.equals(destino.getValue())){
		    	return true;
		    }
		}
		return false;
	}

	private final int[][] casillerosPeonBlanco = {
		{ -1, -1 }, 
		{ 1, -1 }
	};
	private final int[][] casillerosPeonNegro = {
		{ -1, 1 }, 
		{ 1, 1 }
	};
	private boolean positionCapturedByPeon(Color color, Square square) {
		final int[][] saltos;
		final Pieza peon;
		if (Color.BLANCO.equals(color)) {
			saltos = casillerosPeonBlanco;
			peon = Pieza.PEON_BLANCO;
		} else {
			saltos = casillerosPeonNegro;
			peon = Pieza.PEON_NEGRO;
		}
		Iterator<PosicionPieza> iterator = dummyBoard.iterator(new SaltoSquareIterator(square, saltos));
		while (iterator.hasNext()) {
		    PosicionPieza destino = iterator.next();
		    if(peon.equals(destino.getValue())){
		    	return true;
		    }
		}
		return false;
	}

}
