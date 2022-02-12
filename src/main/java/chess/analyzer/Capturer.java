package chess.analyzer;

import chess.ChessPositionReader;
import chess.Color;
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.iterators.Cardinal;
import chess.iterators.pieceplacement.PiecePlacementIterator;
import chess.iterators.square.CardinalSquareIterator;
import chess.iterators.square.JumpSquareIterator;
import chess.pseudomovesgenerators.strategies.AbstractKingMoveGenerator;
import chess.pseudomovesgenerators.strategies.BishopMoveGenerator;
import chess.pseudomovesgenerators.strategies.KnightMoveGenerator;
import chess.pseudomovesgenerators.strategies.RookMoveGenerator;



/**
 * @author Mauricio Coria
 *
 */
public class Capturer {
	private final ChessPositionReader positionReader;
	private final ImprovedCapturerColor capturerWhite = new ImprovedCapturerColor(Color.WHITE);
	private final ImprovedCapturerColor capturerBlack = new ImprovedCapturerColor(Color.BLACK);
	
	public Capturer(ChessPositionReader positionReader) {
		this.positionReader = positionReader;
	}	

	public boolean positionCaptured(Color color, Square square) {
		if(Color.WHITE.equals(color)){
			return capturerWhite.positionCaptured(square);
		} else {
			return capturerBlack.positionCaptured(square);
		}
	}

	
	private class ImprovedCapturerColor {
		private final Piece torre;
		private final Piece alfil;
		private final Piece queen;
		private final Piece caballo;
		private final int[][] saltosPawn;
		private final Piece peon;
		private final Piece king;
		
		private final int[][] casillerosPawnWhite = {
				{ -1, -1 }, 
				{ 1, -1 }
			};
				
		private final int[][] casillerosPawnBlack = {
			{ -1, 1 }, 
			{ 1, 1 }
		};		

		
		public ImprovedCapturerColor(Color color) {
			torre =  Piece.getRook(color);
			alfil = Piece.getBishop(color);
			queen = Piece.getQueen(color);
			caballo = Piece.getKnight(color);
			peon = Piece.getPawn(color);
			king = Piece.getKing(color);		
			

			if (Color.WHITE.equals(color)) {
				saltosPawn = casillerosPawnWhite;
			} else {
				saltosPawn = casillerosPawnBlack;
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

		private Cardinal[]  direccionesBishop = BishopMoveGenerator.BISHOP_CARDINAL;
		private boolean positionCapturedByBishop(Square square) {
			return positionCapturedByDireccion(square, direccionesBishop,  alfil);
		}

		private Cardinal[]  direccionesRook = RookMoveGenerator.ROOK_CARDINAL;
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
			PiecePlacementIterator iterator = positionReader.iterator(new CardinalSquareIterator(square, cardinal));
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
			PiecePlacementIterator iterator = positionReader.iterator(new JumpSquareIterator(square, KnightMoveGenerator.SALTOS_CABALLO));
			while (iterator.hasNext()) {
			    PiecePositioned destino = iterator.next();
			    if(caballo.equals(destino.getValue())){		    	
			    	return true;
			    }
			}
			return false;
		}


		private boolean positionCapturedByPawn(Square square) {
			PiecePlacementIterator iterator = positionReader.iterator(new JumpSquareIterator(square, saltosPawn));
			while (iterator.hasNext()) {
			    PiecePositioned destino = iterator.next();
			    if(peon.equals(destino.getValue())){		    	
			    	return true;
			    }
			}
			return false;
		}
		
		private boolean positionCapturedByKing(Square square) {
			PiecePlacementIterator iterator = positionReader.iterator(new JumpSquareIterator(square, AbstractKingMoveGenerator.SALTOS_KING));
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