package chess.board.moves.imp;

import chess.board.PiecePositioned;
import chess.board.legalmovesgenerators.MoveFilter;
import chess.board.moves.MoveKing;
import chess.board.position.ChessPositionWriter;
import chess.board.position.imp.KingCacheBoard;

/**
 * @author Mauricio Coria
 *
 */
class CaptureKingMove extends CaptureMove  implements MoveKing  {

	public CaptureKingMove(PiecePositioned from, PiecePositioned to) {
		super(from, to);
	}
	
	@Override
	public void executeMove(ChessPositionWriter chessPosition) {
		chessPosition.executeMove(this);
	}
	
	@Override
	public void undoMove(ChessPositionWriter chessPosition) {
		chessPosition.undoMove(this);
	}	
	
	@Override
	public boolean filter(MoveFilter filter){
		return filter.filterMove(this);
	}
	
	@Override
	public void executeMove(KingCacheBoard kingCacheBoard){
		kingCacheBoard.setKingSquare(getFrom().getValue().getColor(), getTo().getKey());
	}
	
	@Override
	public void undoMove(KingCacheBoard kingCacheBoard){
		kingCacheBoard.setKingSquare(getFrom().getValue().getColor(), getFrom().getKey());	
	}	
	
	@Override
	public boolean equals(Object obj) {
        return super.equals(obj) && obj instanceof CaptureKingMove;
    }

}