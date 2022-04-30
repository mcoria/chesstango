package chess.board.analyzer.capturers;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.Cardinal;
import chess.board.iterators.pieceplacement.PiecePlacementIterator;
import chess.board.iterators.square.CardinalSquareIterator;
import chess.board.position.PiecePlacementReader;
import chess.board.pseudomovesgenerators.strategies.BishopMoveGenerator;
import chess.board.pseudomovesgenerators.strategies.RookMoveGenerator;



/**
 * @author Mauricio Coria
 *
 */
// TODO: el capturer para analyzer es distinto, deberia 
//       	- buscar todas las posibilidades de captura de Rey
//       	- durante la busqueda deberia identificar posiciones pinned
//       - deberiamos tener un capturer de posicion mas sencillo para LegalMoveGenerator
//			- Si no se encuentra en Jaque NO es necesario preguntar por jaque de caballo; rey o peon !!!
//				deberia buscar el jaque en direccion del pinned
//			- cuando mueve el rey deberia preguntar por todas las posibilidades de captura
//		 - deberiamos tener un capturer especifico para Castling
public class NoCheckCapturer {
	
	private final PiecePlacementReader piecePlacementReader;
	private final ImprovedCapturerColor capturerWhite = new ImprovedCapturerColor(Color.WHITE);
	private final ImprovedCapturerColor capturerBlack = new ImprovedCapturerColor(Color.BLACK);
	
	public NoCheckCapturer(PiecePlacementReader piecePlacementReader) {
		this.piecePlacementReader = piecePlacementReader;
	}	

	public boolean positionCaptured(Color color, Square square) {
		if(Color.WHITE.equals(color)){
			return capturerWhite.positionCaptured(square);
		} else {
			return capturerBlack.positionCaptured(square);
		}
	}

	
	private class ImprovedCapturerColor {
		private final Piece rook;
		private final Piece bishop;
		private final Piece queen;	

		
		public ImprovedCapturerColor(Color color) {
			this.rook =  Piece.getRook(color);
			this.bishop = Piece.getBishop(color);
			this.queen = Piece.getQueen(color);
		}

		public boolean positionCaptured(Square square) {
            return positionCapturedByRook(square) ||
                    positionCapturedByBishop(square);
        }

		private final Cardinal[]  direccionesBishop = BishopMoveGenerator.BISHOP_CARDINAL;
		private boolean positionCapturedByBishop(Square square) {
			return positionCapturedByDireccion(square, direccionesBishop,  bishop);
		}

		private final Cardinal[]  direccionesRook = RookMoveGenerator.ROOK_CARDINAL;
		private boolean positionCapturedByRook(Square square) {		
			return positionCapturedByDireccion(square, direccionesRook, rook);
		}

		private boolean positionCapturedByDireccion(Square square, Cardinal[] direcciones, Piece rookObishop) {		
			for (Cardinal cardinal : direcciones) {
				if(positionCapturedByCardinalPieza(rookObishop, queen, square, cardinal)){
					return true;
				}
			}
			return false;
		}
		
		
		
		private boolean positionCapturedByCardinalPieza(Piece rookObishop, Piece queen, Square square, Cardinal cardinal) {
			PiecePlacementIterator iterator = piecePlacementReader.iterator(new CardinalSquareIterator(square, cardinal));
			while (iterator.hasNext()) {
				PiecePositioned destino = iterator.next();
				Piece piece = destino.getValue();
				if (piece == null) {
					continue;
				} else if (queen.equals(piece)) {
					return true;
				} else if (rookObishop.equals(piece)) {			
					return true;
				} else {
					break;
				}
			}
			return false;
		}


	}
	
}