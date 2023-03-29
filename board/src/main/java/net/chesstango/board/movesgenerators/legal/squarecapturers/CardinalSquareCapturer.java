package net.chesstango.board.movesgenerators.legal.squarecapturers;

import net.chesstango.board.Color;
import net.chesstango.board.Square;
import net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece.CapturerByCardinals;
import net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece.SquareCapturerByPiece;
import net.chesstango.board.position.BoardReader;


/**
 * Este SquareCapturer busca las posibilidades de captura que existen para un Square dado considerando
 * atacantes Queen, Rook o Bishop.
 *
 *
 * @author Mauricio Coria
 *
 */
public class CardinalSquareCapturer implements SquareCapturer {
	
	private final BoardReader boardReader;
	private final SquareCapturerByPiece whiteCardinalCapturer;
	private final SquareCapturerByPiece blackCardinalCapturer;

	
	public CardinalSquareCapturer(BoardReader boardReader) {
		this.boardReader = boardReader;
		this.whiteCardinalCapturer = new CapturerByCardinals(boardReader, Color.WHITE);
		this.blackCardinalCapturer = new CapturerByCardinals(boardReader, Color.BLACK);
	}	

	@Override
	public boolean positionCaptured(Color color, Square square) {
		if(Color.WHITE.equals(color)){
			return whiteCardinalCapturer.positionCaptured(square);
		} else {
			return blackCardinalCapturer.positionCaptured(square);
		}
	}

	
}