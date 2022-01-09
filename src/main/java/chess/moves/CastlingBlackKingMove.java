package chess.moves;

import chess.BoardState;
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;

/**
 * @author Mauricio Coria
 *
 */
public class CastlingBlackKingMove extends CastlingMove {

	public static final PiecePositioned FROM = new PiecePositioned(Square.e8, Piece.KING_BLACK);
	public static final PiecePositioned TO = new PiecePositioned(Square.g8, null);
	
	public static final PiecePositioned TORRE_FROM = new PiecePositioned(Square.h8, Piece.ROOK_BLACK);
	public static final PiecePositioned TORRE_TO = new PiecePositioned(Square.f8, null);
	
	private static final SimpleKingMove REY_MOVE = new SimpleKingMove(FROM, TO);
	private static final SimpleMove TORRE_MOVE = new SimpleMove(TORRE_FROM, TORRE_TO);
	
	public CastlingBlackKingMove() {
		super(REY_MOVE, TORRE_MOVE);
	}
	
	@Override
	public void executeMove(BoardState boardState) {
		super.executeMove(boardState);
		boardState.setCastlingBlackKingPermitido(false);
		boardState.setCastlingBlackQueenPermitido(false);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CastlingBlackKingMove){
			return true;
		}
		return false;
	}
}
