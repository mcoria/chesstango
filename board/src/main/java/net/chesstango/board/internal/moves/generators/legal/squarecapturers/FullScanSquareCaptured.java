package net.chesstango.board.internal.moves.generators.legal.squarecapturers;

import net.chesstango.board.Color;
import net.chesstango.board.Square;
import net.chesstango.board.internal.moves.generators.legal.squarecapturers.bypiece.CapturerByKing;
import net.chesstango.board.internal.moves.generators.legal.squarecapturers.bypiece.CapturerByKnight;
import net.chesstango.board.internal.moves.generators.legal.squarecapturers.bypiece.CapturerByPawn;
import net.chesstango.board.internal.moves.generators.legal.squarecapturers.bypiece.CapturerByPiece;
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
public class FullScanSquareCaptured extends CardinalSquareCaptured {
	private final BitBoardReader bitBoardReader;
	private final NoCardinalSquareCapturedAggregate capturerWhite;
	private final NoCardinalSquareCapturedAggregate capturerBlack;
	
	public FullScanSquareCaptured(SquareBoardReader squareBoardReader, BitBoardReader bitBoardReader) {
		super(squareBoardReader, bitBoardReader);
		this.bitBoardReader = bitBoardReader;
		this.capturerWhite = new NoCardinalSquareCapturedAggregate(squareBoardReader, bitBoardReader, Color.WHITE);
		this.capturerBlack = new NoCardinalSquareCapturedAggregate(squareBoardReader, bitBoardReader, Color.BLACK);
	}

	@Override
	public boolean isCaptured(Color color, Square square) {
		if(Color.WHITE.equals(color)){
			return super.isCaptured(Color.WHITE, square)  || capturerWhite.positionCaptured(square, bitBoardReader.getPositions(Color.WHITE));
		} else {
			return super.isCaptured(Color.BLACK, square)  || capturerBlack.positionCaptured(square, bitBoardReader.getPositions(Color.BLACK));
		}
	}


	private static class NoCardinalSquareCapturedAggregate implements CapturerByPiece {
		private final CapturerByPiece knightCapturer;
		private final CapturerByPiece pawnCapturer;
		private final CapturerByPiece kingCapturer;

		public NoCardinalSquareCapturedAggregate(SquareBoardReader squareBoardReader, BitBoardReader bitBoardReader, Color color) {
			this.knightCapturer = new CapturerByKnight(squareBoardReader, color);
			this.pawnCapturer = new CapturerByPawn(squareBoardReader, color);
			this.kingCapturer = new CapturerByKing(squareBoardReader, color);
		}

		@Override
		public boolean positionCaptured(Square square, long possibleThreats) {
            return knightCapturer.positionCaptured(square, possibleThreats) ||
					pawnCapturer.positionCaptured(square, possibleThreats) ||
					kingCapturer.positionCaptured(square, possibleThreats);
        }
	}

}