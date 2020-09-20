package positioncaptures;

import java.util.Iterator;

import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import iterators.SaltoSquareIterator;
import layers.ColorBoard;
import layers.DummyBoard;
import movegenerators.CaballoMoveGenerator;

public class ImprovedCapturer implements Capturer {
	
	private DummyBoard dummyBoard = null; 
	private ColorBoard colorBoard = null;
	
	public ImprovedCapturer(DummyBoard dummyBoard, ColorBoard colorBoard) {
		this.dummyBoard = dummyBoard;
		this.colorBoard = colorBoard;
	}

	@Override
	public boolean positionCaptured(Color color, Square square) {
		if(positionCapturedByPeon(color, square) ||
		   positionCapturedByCaballo(color, square)	){
			return true;
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
