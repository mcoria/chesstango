package chess.positioncaptures;

import java.util.Iterator;

import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import chess.iterators.Cardinal;
import chess.iterators.CardinalSquareIterator;
import chess.iterators.SaltoSquareIterator;
import chess.layers.PosicionPiezaBoard;
import chess.pseudomovesgenerators.CaballoMoveGenerator;
import chess.pseudomovesgenerators.KingAbstractMoveGenerator;


/**
 * @author Mauricio Coria
 *
 */
public class ImprovedCapturer implements Capturer {
	
	private final ImprovedCapturerColor capturerBlanco;
	private final ImprovedCapturerColor capturerNegro;
	
	public ImprovedCapturer(PosicionPiezaBoard dummyBoard) {
		this.capturerBlanco = new ImprovedCapturerColor(Color.BLANCO, dummyBoard);
		this.capturerNegro = new ImprovedCapturerColor(Color.NEGRO, dummyBoard);
	}	

	@Override
	public boolean positionCaptured(Color color, Square square) {
		if(Color.BLANCO.equals(color)){
			return capturerBlanco.positionCaptured(square);
		} else {
			return capturerNegro.positionCaptured(square);
		}
	}

	
	private static class ImprovedCapturerColor {
		
		private final PosicionPiezaBoard dummyBoard; 
		
		private final Pieza torre;
		private final Pieza alfil;
		private final Pieza queen;
		private final Pieza caballo;
		private final int[][] saltosPeon;
		private final Pieza peon;
		private final Pieza king;
		
		private final int[][] casillerosPeonBlanco = {
			{ -1, -1 }, 
			{ 1, -1 }
		};
		
		private final int[][] casillerosPeonNegro = {
			{ -1, 1 }, 
			{ 1, 1 }
		};

		
		public ImprovedCapturerColor(Color color, PosicionPiezaBoard dummyBoard) {
			this.dummyBoard = dummyBoard;
			torre =  Pieza.getTorre(color);
			alfil = Pieza.getAlfil(color);
			queen = Pieza.getReina(color);
			caballo = Pieza.getCaballo(color);
			peon = Pieza.getPeon(color);
			king = Pieza.getKing(color);		
			

			if (Color.BLANCO.equals(color)) {
				saltosPeon = casillerosPeonBlanco;
			} else {
				saltosPeon = casillerosPeonNegro;
			}		
		}

		public boolean positionCaptured(Square square) {
			if(positionCapturedByCaballo(square)	||
			   positionCapturedByTorre(square)	||
			   positionCapturedByAlfil(square)   ||
			   positionCapturedByPeon(square) ||
			   positionCapturedByKing(square)) {
				return true;
			}
			return false;
		}

		private Cardinal[]  direccionesAlfil = new Cardinal[] {Cardinal.NorteEste, Cardinal.SurEste, Cardinal.SurOeste, Cardinal.NorteOeste};
		private boolean positionCapturedByAlfil(Square square) {
			return positionCapturedByDireccion(square, direccionesAlfil,  alfil);
		}

		private Cardinal[]  direccionesTorre = new Cardinal[] {Cardinal.Este, Cardinal.Oeste, Cardinal.Norte, Cardinal.Sur};
		private boolean positionCapturedByTorre(Square square) {		
			return positionCapturedByDireccion(square, direccionesTorre, torre);
		}

		private boolean positionCapturedByDireccion(Square square, Cardinal[] direcciones, Pieza torreOalfil) {		
			for (Cardinal cardinal : direcciones) {
				if(cardinalPositionCapturedByPieza(torreOalfil, queen, square, cardinal)){
					return true;
				}
			}
			return false;
		}
		
		private boolean cardinalPositionCapturedByPieza(Pieza torreOalfil, Pieza queen, Square square, Cardinal cardinal) {
			Iterator<PosicionPieza> iterator = this.dummyBoard.iterator(new CardinalSquareIterator(square, cardinal));
			while (iterator.hasNext()) {
				PosicionPieza destino = iterator.next();
				Pieza pieza = destino.getValue();
				if (pieza == null) {
					continue;
				} else if (queen.equals(pieza)) {
					return true;
				} else if (torreOalfil.equals(pieza)) {			
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
		
		private boolean positionCapturedByKing(Square square) {
			Iterator<PosicionPieza> iterator = dummyBoard.iterator(new SaltoSquareIterator(square, KingAbstractMoveGenerator.SALTOS_REY));
			while (iterator.hasNext()) {
			    PosicionPieza destino = iterator.next();
			    if(king.equals(destino.getValue())){		    	
			    	return true;
			    }
			}
			return false;
		}		

	}	

}
