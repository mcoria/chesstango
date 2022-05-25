package chess.board.movesgenerators.legal.squarecapturers;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.Cardinal;
import chess.board.iterators.bysquare.CardinalSquareIterator;
import chess.board.movesgenerators.legal.squarecapturers.bypiece.CapturerByCardinals;
import chess.board.movesgenerators.legal.squarecapturers.bypiece.SquareCapturerByPiece;
import chess.board.position.PiecePlacementReader;
import chess.board.movesgenerators.pseudo.strategies.BishopMoveGenerator;
import chess.board.movesgenerators.pseudo.strategies.RookMoveGenerator;

import java.util.Iterator;


/**
 * Este SquareCapturer busca las posibilidades de captura que existen para un Square dado considerando
 * atacantes Queen, Rook o Bishop.
 *
 *
 * @author Mauricio Coria
 *
 */
public class CardinalSquareCapturer implements SquareCapturer {
	
	private final PiecePlacementReader piecePlacementReader;
	private final SquareCapturerByPiece whiteCardinalCapturer;
	private final SquareCapturerByPiece blackCardinalCapturer;

	
	public CardinalSquareCapturer(PiecePlacementReader piecePlacementReader) {
		this.piecePlacementReader = piecePlacementReader;
		this.whiteCardinalCapturer = new CapturerByCardinals(piecePlacementReader, Color.WHITE);
		this.blackCardinalCapturer = new CapturerByCardinals(piecePlacementReader, Color.BLACK);
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