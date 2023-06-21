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
	private final FullScanSquareCapturedAggregate capturerWhite;
	private final FullScanSquareCapturedAggregate capturerBlack;
	
	public FullScanSquareCaptured(SquareBoardReader squareBoardReader, BitBoardReader bitBoardReader) {
		this.squareBoardReader = squareBoardReader;
		this.bitBoardReader = bitBoardReader;
		this.capturerWhite = new FullScanSquareCapturedAggregate(squareBoardReader, bitBoardReader, Color.WHITE);
		this.capturerBlack = new FullScanSquareCapturedAggregate(squareBoardReader, bitBoardReader, Color.BLACK);
	}

	@Override
	public boolean isCaptured(Color color, Square square) {
		if(Color.WHITE.equals(color)){
			return capturerWhite.positionCaptured(square, bitBoardReader.getPositions(Color.WHITE));
		} else {
			return capturerBlack.positionCaptured(square, bitBoardReader.getPositions(Color.BLACK));
		}
	}


	protected class FullScanSquareCapturedAggregate extends CardinalSquareCaptured.CardinalSquareCapturedAggregate {
		private final CapturerByPiece knightCapturer;
		private final CapturerByPiece pawnCapturer;
		private final CapturerByPiece kingCapturer;

		public FullScanSquareCapturedAggregate(SquareBoardReader squareBoardReader, BitBoardReader bitBoardReader, Color color) {
			super(squareBoardReader, bitBoardReader, color);
			this.knightCapturer = new CapturerByKnight(squareBoardReader, color);
			this.pawnCapturer = new CapturerByPawn(squareBoardReader, color);
			this.kingCapturer = new CapturerByKing(squareBoardReader, color);
		}

		@Override
		public boolean positionCaptured(Square square, long possibleAttackers) {
            return super.positionCaptured(square, possibleAttackers) ||
					knightCapturer.positionCaptured(square, possibleAttackers) ||
					pawnCapturer.positionCaptured(square, possibleAttackers) ||
					kingCapturer.positionCaptured(square, possibleAttackers);
        }
	}

}