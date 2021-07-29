package moveexecutors;

import chess.Board;
import layers.ColorBoard;
import layers.MoveCacheBoard;
import layers.PosicionPiezaBoard;

public class EnroqueMove extends MoveDecorator  {
	protected final SimpleMove torreMove;	
	
	public EnroqueMove(SimpleReyMove reyMove, SimpleMove torreMove) {
		super(reyMove);
		this.torreMove = torreMove;
	}

	@Override
	public void executeMove(Board board) {
		board.executeKingMove(this);
	}
	
	@Override
	public void undoMove(Board board) {
		board.undoKingMove(this);
	}
	
	@Override
	public void executeMove(PosicionPiezaBoard board) {
		move.executeMove(board);
		torreMove.executeMove(board);
	}


	@Override
	public void undoMove(PosicionPiezaBoard board) {
		move.undoMove(board);
		torreMove.undoMove(board);
	}	
	
	@Override
	public void executeMove(ColorBoard colorBoard) {
		move.executeMove(colorBoard);
		torreMove.executeMove(colorBoard);
	}

	@Override
	public void undoMove(ColorBoard colorBoard) {
		move.undoMove(colorBoard);
		torreMove.undoMove(colorBoard);
	}
	
	
	@Override
	public void executeMove(MoveCacheBoard moveCache) {
		moveCache.pushState();
		moveCache.clearPseudoMoves(move.getFrom().getKey(), move.getTo().getKey(), torreMove.getFrom().getKey(), torreMove.getTo().getKey());
	}


}
