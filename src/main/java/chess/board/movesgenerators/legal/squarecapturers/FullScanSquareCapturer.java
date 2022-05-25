package chess.board.movesgenerators.legal.squarecapturers;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.Cardinal;
import chess.board.iterators.byposition.bypiece.KingBitIterator;
import chess.board.iterators.byposition.bypiece.PawnBlackBitIterator;
import chess.board.iterators.byposition.bypiece.PawnWhiteBitIterator;
import chess.board.iterators.bysquare.CardinalSquareIterator;
import chess.board.movesgenerators.legal.squarecapturers.bypiece.CapturerByKing;
import chess.board.movesgenerators.legal.squarecapturers.bypiece.CapturerByKnight;
import chess.board.movesgenerators.legal.squarecapturers.bypiece.SquareCapturerByPiece;
import chess.board.movesgenerators.pseudo.strategies.BishopMoveGenerator;
import chess.board.movesgenerators.pseudo.strategies.RookMoveGenerator;
import chess.board.position.PiecePlacementReader;

import java.util.Iterator;
import java.util.function.Function;


/**
 * Este SquareCapturer busca todas las posibilidades de captura que existen para un Square dado.
 *
 * @author Mauricio Coria
 *
 */
// TODO: el capturer para analyzer es distinto, deberia 
//       	- buscar todas las posibilidades de captura de Rey
//       	- durante la busqueda deberia identificar posiciones pinned
//       - deberiamos tener un capturer de posicion mas sencillo para LegalMoveGenerator
//			- Si no se encuentra en Jaque NO es necesario preguntar por jaque de knight; rey o peon !!!
//				deberia buscar el jaque en direccion del pinned
//			- cuando mueve el rey deberia preguntar por todas las posibilidades de captura
//		 - deberiamos tener un capturer especifico para Castling
public class FullScanSquareCapturer implements SquareCapturer {
	
	private final PiecePlacementReader piecePlacementReader;
	private final CapturerImp capturerWhite;
	private final CapturerImp capturerBlack;
	
	public FullScanSquareCapturer(PiecePlacementReader piecePlacementReader) {
		this.piecePlacementReader = piecePlacementReader;
		this.capturerWhite = new CapturerImp(Color.WHITE, this::createPawnWhiteIterator);
		this.capturerBlack = new CapturerImp(Color.BLACK, this::createPawnBlackIterator);
	}

	@Override
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
		private final SquareCapturerByPiece knightCapturer;
		private final Function<Square, Iterator<PiecePositioned>> createPawnJumpsIterator;
		private final Piece pawn;
		private final SquareCapturerByPiece kingCapturer;


		public CapturerImp(Color color, Function<Square, Iterator<PiecePositioned>> createPawnJumpsIterator) {
			this.rook =  Piece.getRook(color);
			this.bishop = Piece.getBishop(color);
			this.queen = Piece.getQueen(color);
			this.knightCapturer = new CapturerByKnight(piecePlacementReader, color);
			this.pawn = Piece.getPawn(color);
			this.kingCapturer = new CapturerByKing(piecePlacementReader, color);
			this.createPawnJumpsIterator = createPawnJumpsIterator;
		}

		public boolean positionCaptured(Square square) {
            return knightCapturer.positionCaptured(square) ||
                    positionCapturedByRook(square) ||
                    positionCapturedByBishop(square) ||
                    positionCapturedByPawn(square) ||
					kingCapturer.positionCaptured(square);
        }

		private final Cardinal[] direccionesBishop = BishopMoveGenerator.BISHOP_CARDINAL;
		private boolean positionCapturedByBishop(Square square) {
			return positionCapturedByDireccion(square, direccionesBishop,  bishop);
		}

		private final Cardinal[] direccionesRook = RookMoveGenerator.ROOK_CARDINAL;
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


		private boolean positionCapturedByPawn(Square square) {
			Iterator<PiecePositioned> iterator = createPawnJumpsIterator.apply(square);
			while (iterator.hasNext()) {
			    PiecePositioned destino = iterator.next();
			    if(pawn.equals(destino.getValue())){		    	
			    	return true;
			    }
			}
			return false;
		}

	}

	private Iterator<PiecePositioned> createPawnWhiteIterator(Square square) {
		return new PawnWhiteBitIterator<PiecePositioned>(piecePlacementReader, square);
	}

	private Iterator<PiecePositioned> createPawnBlackIterator(Square square) {
		return new PawnBlackBitIterator<PiecePositioned>(piecePlacementReader, square);
	}
}