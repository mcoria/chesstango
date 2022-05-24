package chess.board.legalmovesgenerators.squarecapturers;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.Cardinal;
import chess.board.iterators.square.CardinalSquareIterator;
import chess.board.position.PiecePlacementReader;
import chess.board.pseudomovesgenerators.strategies.BishopMoveGenerator;
import chess.board.pseudomovesgenerators.strategies.RookMoveGenerator;

import java.util.Iterator;


/**
 * Este SquareCapturer busca las posibilidades de captura que existen para un Square dado considerando
 * atacantes Queen, Rook o Bishop.
 *
 *
 * @author Mauricio Coria
 *
 */
public class CardinalSquareCapturer {
	
	private final PiecePlacementReader piecePlacementReader;
	private final CapturerImp capturerWhite = new CapturerImp(Color.WHITE);
	private final CapturerImp capturerBlack = new CapturerImp(Color.BLACK);
	
	public CardinalSquareCapturer(PiecePlacementReader piecePlacementReader) {
		this.piecePlacementReader = piecePlacementReader;
	}	

	public boolean positionCaptured(Color color, Square square) {
		if(Color.WHITE.equals(color)){
			return capturerWhite.positionCaptured(square);
		} else {
			return capturerBlack.positionCaptured(square);
		}
	}

	
	private class CapturerImp {
		private final Piece rook;
		private final Piece bishop;
		private final Piece queen;	

		
		public CapturerImp(Color color) {
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
			Iterator<PiecePositioned> iterator = piecePlacementReader.iterator(new CardinalSquareIterator(square, cardinal));
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