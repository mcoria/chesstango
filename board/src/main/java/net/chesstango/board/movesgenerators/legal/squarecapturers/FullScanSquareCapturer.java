package net.chesstango.board.movesgenerators.legal.squarecapturers;

import net.chesstango.board.Color;
import net.chesstango.board.Square;
import net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece.*;
import net.chesstango.board.position.BoardReader;


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
	
	private final BoardReader boardReader;
	private final CapturerAgregate capturerWhite;
	private final CapturerAgregate capturerBlack;
	
	public FullScanSquareCapturer(BoardReader boardReader) {
		this.boardReader = boardReader;
		this.capturerWhite = new CapturerAgregate(Color.WHITE);
		this.capturerBlack = new CapturerAgregate(Color.BLACK);
	}

	@Override
	public boolean positionCaptured(Color color, Square square) {
		if(Color.WHITE.equals(color)){
			return capturerWhite.positionCaptured(square);
		} else {
			return capturerBlack.positionCaptured(square);
		}
	}

	
	private class CapturerAgregate implements SquareCapturerByPiece {
		private final SquareCapturerByPiece knightCapturer;
		private final SquareCapturerByPiece pawnCapturer;
		private final SquareCapturerByPiece kingCapturer;
		private final SquareCapturerByPiece cardinalCapturer;

		public CapturerAgregate(Color color) {
			this.cardinalCapturer = new CapturerByCardinals(boardReader, color);
			this.knightCapturer = new CapturerByKnight(boardReader, color);
			this.pawnCapturer = new CapturerByPawn(boardReader, color);
			this.kingCapturer = new CapturerByKing(boardReader, color);
		}

		public boolean positionCaptured(Square square) {
            return knightCapturer.positionCaptured(square) ||
					cardinalCapturer.positionCaptured(square) ||
					pawnCapturer.positionCaptured(square) ||
					kingCapturer.positionCaptured(square);
        }


	}

}