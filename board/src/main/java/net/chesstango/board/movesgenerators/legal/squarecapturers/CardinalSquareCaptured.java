package net.chesstango.board.movesgenerators.legal.squarecapturers;

import net.chesstango.board.Color;
import net.chesstango.board.Square;
import net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece.CapturerByBishop;
import net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece.CapturerByRook;
import net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece.CapturerByPiece;
import net.chesstango.board.position.BoardReader;


/**
 * Este SquareCapturer busca las posibilidades de captura que existen para un Square dado considerando
 * atacantes Queen, Rook o Bishop.
 *
 *
 * @author Mauricio Coria
 *
 */
public class CardinalSquareCaptured implements SquareCaptured {
	
	private final BoardReader boardReader;
	private final CapturerAgregate capturerWhite;
	private final CapturerAgregate capturerBlack;

	
	public CardinalSquareCaptured(BoardReader boardReader) {
		this.boardReader = boardReader;
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

		private final CapturerByPiece rookCapturer;
		private final CapturerByPiece bishopCapturer;

		public CapturerAgregate(Color color) {
			this.rookCapturer = new CapturerByRook(boardReader, color);
			this.bishopCapturer = new CapturerByBishop(boardReader, color);
		}

		public boolean positionCaptured(Square square) {
			return rookCapturer.positionCaptured(square) ||
					bishopCapturer.positionCaptured(square);
		}
	}

	
}