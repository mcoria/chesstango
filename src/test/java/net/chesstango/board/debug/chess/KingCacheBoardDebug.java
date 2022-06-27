package net.chesstango.board.debug.chess;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.position.PiecePlacement;
import net.chesstango.board.position.imp.KingCacheBoard;


/**
 * @author Mauricio Coria
 *
 */
public class KingCacheBoardDebug extends KingCacheBoard {
	
	public void validar(PiecePlacement dummyBoard) {
		if (squareKingWhiteCache != null && !Piece.KING_WHITE.equals(dummyBoard.getPiece(squareKingWhiteCache))) {
			throw new RuntimeException("KingCacheBoard - squareKingWhiteCache quedo desactualizado");
		}

		if (squareKingBlackCache != null && !Piece.KING_BLACK.equals(dummyBoard.getPiece(squareKingBlackCache))) {
			throw new RuntimeException("KingCacheBoard - squareKingBlackCache quedo desactualizado");
		}
		
		for (PiecePositioned pieza : dummyBoard) {
			if (Piece.KING_WHITE.equals(pieza.getValue()) && !pieza.getKey().equals(squareKingWhiteCache)) {
				throw new RuntimeException("KingCacheBoard - squareKingWhiteCache quedo desactualizado");
			}
			if (Piece.KING_BLACK.equals(pieza.getValue()) && !pieza.getKey().equals(squareKingBlackCache)) {
				throw new RuntimeException("KingCacheBoard - squareKingBlackCache quedo desactualizado");
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
