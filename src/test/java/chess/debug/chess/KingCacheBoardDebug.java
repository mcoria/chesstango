package chess.debug.chess;

import chess.Color;
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.position.KingCacheBoard;
import chess.position.PiecePlacement;


/**
 * @author Mauricio Coria
 *
 */
public class KingCacheBoardDebug extends KingCacheBoard {

	public KingCacheBoardDebug(PiecePlacement piecePlacement) {
		super(piecePlacement);
	}
	
	public void validar(PiecePlacement dummyBoard) {
		if (squareKingWhiteCache != null && !Piece.KING_WHITE.equals(dummyBoard.getPieza(squareKingWhiteCache))) {
			throw new RuntimeException("KingCacheBoard - squareKingWhiteCache quedó desactualizado");
		}

		if (squareKingBlackCache != null && !Piece.KING_BLACK.equals(dummyBoard.getPieza(squareKingBlackCache))) {
			throw new RuntimeException("KingCacheBoard - squareKingBlackCache quedó desactualizado");
		}
		
		for (PiecePositioned pieza : dummyBoard) {
			if (Piece.KING_WHITE.equals(pieza.getValue()) && !pieza.getKey().equals(squareKingWhiteCache)) {
				throw new RuntimeException("KingCacheBoard - squareKingWhiteCache quedó desactualizado");
			}
			if (Piece.KING_BLACK.equals(pieza.getValue()) && !pieza.getKey().equals(squareKingBlackCache)) {
				throw new RuntimeException("KingCacheBoard - squareKingBlackCache quedó desactualizado");
			}			
		}
		
	}
	
	@Override
	public Square getKingSquare(Color color) {
		if(color == null){
			throw new RuntimeException("getKingSquare() - invoked with null parameter");
		}
		return super.getKingSquare(color);
	}

}
