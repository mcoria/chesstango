package net.chesstango.board.internal.position;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.position.SquareBoard;


/**
 * @author Mauricio Coria
 *
 */
public class KingSquareDebug extends KingSquareImp {
	
	public void validar(SquareBoard dummySquareBoard) {
		if (squareKingWhiteCache != null && !Piece.KING_WHITE.equals(dummySquareBoard.getPiece(squareKingWhiteCache))) {
			throw new RuntimeException("KingCacheBoard - squareKingWhiteCache quedo desactualizado");
		}

		if (squareKingBlackCache != null && !Piece.KING_BLACK.equals(dummySquareBoard.getPiece(squareKingBlackCache))) {
			throw new RuntimeException("KingCacheBoard - squareKingBlackCache quedo desactualizado");
		}
		
		for (PiecePositioned pieza : dummySquareBoard) {
			if (Piece.KING_WHITE.equals(pieza.piece()) && !pieza.square().equals(squareKingWhiteCache)) {
				throw new RuntimeException("KingCacheBoard - squareKingWhiteCache quedo desactualizado");
			}
			if (Piece.KING_BLACK.equals(pieza.piece()) && !pieza.square().equals(squareKingBlackCache)) {
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
