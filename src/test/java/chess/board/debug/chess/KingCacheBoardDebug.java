package chess.board.debug.chess;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.position.PiecePlacement;
import chess.board.position.imp.KingCacheBoard;


/**
 * @author Mauricio Coria
 *
 */
public class KingCacheBoardDebug extends KingCacheBoard {
	
	public void validar(PiecePlacement dummyBoard) {
		if (squareKingWhiteCache != null && !Piece.KING_WHITE.equals(dummyBoard.getPieza(squareKingWhiteCache))) {
			throw new RuntimeException("KingCacheBoard - squareKingWhiteCache qued� desactualizado");
		}

		if (squareKingBlackCache != null && !Piece.KING_BLACK.equals(dummyBoard.getPieza(squareKingBlackCache))) {
			throw new RuntimeException("KingCacheBoard - squareKingBlackCache qued� desactualizado");
		}
		
		for (PiecePositioned pieza : dummyBoard) {
			if (Piece.KING_WHITE.equals(pieza.getValue()) && !pieza.getKey().equals(squareKingWhiteCache)) {
				throw new RuntimeException("KingCacheBoard - squareKingWhiteCache qued� desactualizado");
			}
			if (Piece.KING_BLACK.equals(pieza.getValue()) && !pieza.getKey().equals(squareKingBlackCache)) {
				throw new RuntimeException("KingCacheBoard - squareKingBlackCache qued� desactualizado");
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