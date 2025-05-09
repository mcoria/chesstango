package net.chesstango.board.internal.moves.generators.legal.squarecapturers;

import net.chesstango.board.Color;
import net.chesstango.board.Square;
import net.chesstango.board.internal.moves.generators.legal.squarecapturers.bypiece.CapturerByBishop;
import net.chesstango.board.internal.moves.generators.legal.squarecapturers.bypiece.CapturerByPiece;
import net.chesstango.board.internal.moves.generators.legal.squarecapturers.bypiece.CapturerByRook;
import net.chesstango.board.position.BitBoardReader;
import net.chesstango.board.position.SquareBoardReader;


/**
 * Este SquareCapturer busca las posibilidades de captura que existen para un Square dado considerando
 * atacantes Queen, Rook o Bishop.
 *
 *
 * @author Mauricio Coria
 *
 */
public class CardinalSquareCaptured implements SquareCaptured {
	private final BitBoardReader bitBoardReader;
	private final CardinalSquareCapturedAggregate capturerWhite;
	private final CardinalSquareCapturedAggregate capturerBlack;

	
	public CardinalSquareCaptured(SquareBoardReader squareBoardReader, BitBoardReader bitBoardReader) {
		this.bitBoardReader = bitBoardReader;
		this.capturerWhite = new CardinalSquareCapturedAggregate(squareBoardReader, bitBoardReader, Color.WHITE);
		this.capturerBlack = new CardinalSquareCapturedAggregate(squareBoardReader, bitBoardReader, Color.BLACK);
	}	

	@Override
	public boolean isCaptured(Color color, Square square) {
		if(Color.WHITE.equals(color)){
			return capturerWhite.positionCaptured(square, bitBoardReader.getPositions(Color.WHITE));
		} else {
			return capturerBlack.positionCaptured(square, bitBoardReader.getPositions(Color.BLACK));
		}
	}

	private static class CardinalSquareCapturedAggregate implements CapturerByPiece {

		private final CapturerByPiece rookCapturer;
		private final CapturerByPiece bishopCapturer;

		public CardinalSquareCapturedAggregate(SquareBoardReader squareBoardReader, BitBoardReader bitBoardReader, Color color) {
			this.rookCapturer = new CapturerByRook(squareBoardReader, bitBoardReader, color);
			this.bishopCapturer = new CapturerByBishop(squareBoardReader, bitBoardReader, color);
		}

		@Override
		public boolean positionCaptured(Square square, long possibleThreats) {
			return rookCapturer.positionCaptured(square, possibleThreats) ||
					bishopCapturer.positionCaptured(square, possibleThreats);
		}
	}

	
}