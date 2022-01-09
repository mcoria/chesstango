package chess.positioncaptures;

import java.util.Iterator;

import chess.Color;
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.iterators.Cardinal;
import chess.iterators.square.CardinalSquareIterator;
import chess.iterators.square.SaltoSquareIterator;
import chess.position.PiecePlacement;
import chess.pseudomovesgenerators.KnightMoveGenerator;
import chess.pseudomovesgenerators.KingAbstractMoveGenerator;


/**
 * @author Mauricio Coria
 *
 */
public class ImprovedCapturer implements Capturer {
	
	private final ImprovedCapturerColor capturerBlanco;
	private final ImprovedCapturerColor capturerNegro;
	
	public ImprovedCapturer(PiecePlacement dummyBoard) {
		this.capturerBlanco = new ImprovedCapturerColor(Color.WHITE, dummyBoard);
		this.capturerNegro = new ImprovedCapturerColor(Color.BLACK, dummyBoard);
	}	

	@Override
	public boolean positionCaptured(Color color, Square square) {
		if(Color.WHITE.equals(color)){
			return capturerBlanco.positionCaptured(square);
		} else {
			return capturerNegro.positionCaptured(square);
		}
	}

	
	private static class ImprovedCapturerColor {
		
		private final PiecePlacement dummyBoard; 
		
		private final Piece torre;
		private final Piece alfil;
		private final Piece queen;
		private final Piece caballo;
		private final int[][] saltosPawn;
		private final Piece peon;
		private final Piece king;
		
		private final int[][] casillerosPawnBlanco = {
			{ -1, -1 }, 
			{ 1, -1 }
		};
		
		private final int[][] casillerosPawnNegro = {
			{ -1, 1 }, 
			{ 1, 1 }
		};

		
		public ImprovedCapturerColor(Color color, PiecePlacement dummyBoard) {
			this.dummyBoard = dummyBoard;
			torre =  Piece.getRook(color);
			alfil = Piece.getBishop(color);
			queen = Piece.getQueen(color);
			caballo = Piece.getKnight(color);
			peon = Piece.getPawn(color);
			king = Piece.getKing(color);		
			

			if (Color.WHITE.equals(color)) {
				saltosPawn = casillerosPawnBlanco;
			} else {
				saltosPawn = casillerosPawnNegro;
			}		
		}

		public boolean positionCaptured(Square square) {
			if(positionCapturedByKnight(square)	||
			   positionCapturedByRook(square)	||
			   positionCapturedByBishop(square)   ||
			   positionCapturedByPawn(square) ||
			   positionCapturedByKing(square)) {
				return true;
			}
			return false;
		}

		private Cardinal[]  direccionesBishop = new Cardinal[] {Cardinal.NorteEste, Cardinal.SurEste, Cardinal.SurOeste, Cardinal.NorteOeste};
		private boolean positionCapturedByBishop(Square square) {
			return positionCapturedByDireccion(square, direccionesBishop,  alfil);
		}

		private Cardinal[]  direccionesRook = new Cardinal[] {Cardinal.Este, Cardinal.Oeste, Cardinal.Norte, Cardinal.Sur};
		private boolean positionCapturedByRook(Square square) {		
			return positionCapturedByDireccion(square, direccionesRook, torre);
		}

		private boolean positionCapturedByDireccion(Square square, Cardinal[] direcciones, Piece torreOalfil) {		
			for (Cardinal cardinal : direcciones) {
				if(cardinalPositionCapturedByPieza(torreOalfil, queen, square, cardinal)){
					return true;
				}
			}
			return false;
		}
		
		private boolean cardinalPositionCapturedByPieza(Piece torreOalfil, Piece queen, Square square, Cardinal cardinal) {
			Iterator<PiecePositioned> iterator = this.dummyBoard.iterator(new CardinalSquareIterator(square, cardinal));
			while (iterator.hasNext()) {
				PiecePositioned destino = iterator.next();
				Piece piece = destino.getValue();
				if (piece == null) {
					continue;
				} else if (queen.equals(piece)) {
					return true;
				} else if (torreOalfil.equals(piece)) {			
					return true;
				} else {
					break;
				}
			}
			return false;
		}

		private boolean positionCapturedByKnight(Square square) {
			Iterator<PiecePositioned> iterator = dummyBoard.iterator(new SaltoSquareIterator(square, KnightMoveGenerator.SALTOS_CABALLO));
			while (iterator.hasNext()) {
			    PiecePositioned destino = iterator.next();
			    if(caballo.equals(destino.getValue())){		    	
			    	return true;
			    }
			}
			return false;
		}


		private boolean positionCapturedByPawn(Square square) {
			Iterator<PiecePositioned> iterator = dummyBoard.iterator(new SaltoSquareIterator(square, saltosPawn));
			while (iterator.hasNext()) {
			    PiecePositioned destino = iterator.next();
			    if(peon.equals(destino.getValue())){		    	
			    	return true;
			    }
			}
			return false;
		}
		
		private boolean positionCapturedByKing(Square square) {
			Iterator<PiecePositioned> iterator = dummyBoard.iterator(new SaltoSquareIterator(square, KingAbstractMoveGenerator.SALTOS_KING));
			while (iterator.hasNext()) {
			    PiecePositioned destino = iterator.next();
			    if(king.equals(destino.getValue())){		    	
			    	return true;
			    }
			}
			return false;
		}		

	}	

}
