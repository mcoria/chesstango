package chess.board.moves.imp;

import chess.board.PiecePositioned;
import chess.board.legalmovesgenerators.MoveFilter;
import chess.board.position.ChessPositionWriter;
import chess.board.position.imp.ColorBoard;

/**
 * @author Mauricio Coria
 *
 */
class CaptureMove extends AbstractMove {
	
	public CaptureMove(PiecePositioned from, PiecePositioned to) {
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
	public void executeMove(ColorBoard colorBoard) {
		colorBoard.removePositions(to);
		
		colorBoard.swapPositions(from.getValue().getColor(), from.getKey(), to.getKey());
	}
	
	@Override
	public void undoMove(ColorBoard colorBoard) {
		colorBoard.swapPositions(from.getValue().getColor(), to.getKey(), from.getKey());
		
		colorBoard.addPositions(to);
	}		
	
	@Override
	public boolean equals(Object obj) {
        return super.equals(obj) && obj instanceof CaptureMove;
    }

}
