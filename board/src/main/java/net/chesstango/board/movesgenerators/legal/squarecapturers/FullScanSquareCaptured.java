package net.chesstango.board.movesgenerators.legal.squarecapturers;

import net.chesstango.board.Color;
import net.chesstango.board.Square;
import net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece.*;
import net.chesstango.board.position.BitBoardReader;
import net.chesstango.board.position.SquareBoardReader;


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
public class FullScanSquareCaptured implements SquareCaptured {
	private final SquareBoardReader squareBoardReader;
	private final BitBoardReader bitBoardReader;
	private final CapturerAgregate capturerWhite;
	private final CapturerAgregate capturerBlack;
	
	public FullScanSquareCaptured(SquareBoardReader squareBoardReader, BitBoardReader bitBoardReader) {
		this.squareBoardReader = squareBoardReader;
		this.bitBoardReader = bitBoardReader;
		this.capturerWhite = new CapturerAgregate(Color.WHITE);
		this.capturerBlack = new CapturerAgregate(Color.BLACK);
	}

	@Override
	public boolean isCaptured(Color color, Square square) {
		if(Color.WHITE.equals(color)){
			return capturerWhite.positionCaptured(square);
		} else {
			return capturerBlack.positionCaptured(square);
		}
	}

	
	private class CapturerAgregate implements CapturerByPiece {
		private final CapturerByPiece knightCapturer;
		private final CapturerByPiece pawnCapturer;
		private final CapturerByPiece kingCapturer;
		private final CapturerByPiece rookCapturer;
		private final CapturerByPiece bishopCapturer;

		public CapturerAgregate(Color color) {
			this.rookCapturer = new CapturerByRook(squareBoardReader, bitBoardReader, color);
			this.bishopCapturer = new CapturerByBishop(squareBoardReader, bitBoardReader, color);
			this.knightCapturer = new CapturerByKnight(squareBoardReader, color);
			this.pawnCapturer = new CapturerByPawn(squareBoardReader, color);
			this.kingCapturer = new CapturerByKing(squareBoardReader, color);
		}

		public boolean positionCaptured(Square square) {
            return knightCapturer.positionCaptured(square) ||
					rookCapturer.positionCaptured(square) ||
					bishopCapturer.positionCaptured(square) ||
					pawnCapturer.positionCaptured(square) ||
					kingCapturer.positionCaptured(square);
        }
	}

}