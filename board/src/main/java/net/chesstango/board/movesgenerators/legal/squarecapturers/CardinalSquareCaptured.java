package net.chesstango.board.movesgenerators.legal.squarecapturers;

import net.chesstango.board.Color;
import net.chesstango.board.Square;
import net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece.CapturerByBishop;
import net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece.CapturerByPiece;
import net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece.CapturerByRook;
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
	private final SquareBoardReader squareBoardReader;
	private final BitBoardReader bitBoardReader;
	private final CardinalSquareCapturedAggregate capturerWhite;
	private final CardinalSquareCapturedAggregate capturerBlack;

	
	public CardinalSquareCaptured(SquareBoardReader squareBoardReader, BitBoardReader bitBoardReader) {
		this.squareBoardReader = squareBoardReader;
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

	protected static class CardinalSquareCapturedAggregate implements CapturerByPiece {

		private final CapturerByPiece rookCapturer;
		private final CapturerByPiece bishopCapturer;

		public CardinalSquareCapturedAggregate(SquareBoardReader squareBoardReader, BitBoardReader bitBoardReader, Color color) {
			this.rookCapturer = new CapturerByRook(squareBoardReader, bitBoardReader, color);
			this.bishopCapturer = new CapturerByBishop(squareBoardReader, bitBoardReader, color);
		}

		@Override
		public boolean positionCaptured(Square square, long possibleAttackers) {
			return rookCapturer.positionCaptured(square, possibleAttackers) ||
					bishopCapturer.positionCaptured(square, possibleAttackers);
		}
	}

	
}