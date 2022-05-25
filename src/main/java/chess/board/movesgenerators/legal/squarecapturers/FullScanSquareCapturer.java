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
import chess.board.movesgenerators.legal.squarecapturers.bypiece.*;
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
		this.capturerWhite = new CapturerImp(Color.WHITE);
		this.capturerBlack = new CapturerImp(Color.BLACK);
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
		private final SquareCapturerByPiece knightCapturer;
		private final SquareCapturerByPiece pawnCapturer;
		private final SquareCapturerByPiece kingCapturer;
		private final SquareCapturerByPiece cardinalCapturer;

		public CapturerImp(Color color) {
			this.cardinalCapturer = new CapturerByCardinals(piecePlacementReader, color);
			this.knightCapturer = new CapturerByKnight(piecePlacementReader, color);
			this.pawnCapturer = new CapturerByPawn(piecePlacementReader, color);
			this.kingCapturer = new CapturerByKing(piecePlacementReader, color);
		}

		public boolean positionCaptured(Square square) {
            return knightCapturer.positionCaptured(square) ||
					cardinalCapturer.positionCaptured(square) ||
					pawnCapturer.positionCaptured(square) ||
					kingCapturer.positionCaptured(square);
        }


	}

}