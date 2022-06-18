package chess.board.moves.imp;

import chess.board.PiecePositioned;
import chess.board.iterators.Cardinal;
import chess.board.movesgenerators.legal.MoveFilter;
import chess.board.position.ChessPositionWriter;
import chess.board.position.imp.ColorBoard;
import chess.board.position.imp.PositionState;

/**
 * @author Mauricio Coria
 *
 */
class CaptureMove extends AbstractMove {
	
	public CaptureMove(PiecePositioned from, PiecePositioned to) {
		super(from, to);
	}

    public CaptureMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
		super(from, to, cardinal);
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
	public void executeMove(PositionState positionState) {
		super.executeMove(positionState);
		positionState.resetHalfMoveClock();
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
