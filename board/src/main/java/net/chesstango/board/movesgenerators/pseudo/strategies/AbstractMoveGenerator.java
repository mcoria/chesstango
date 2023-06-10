package net.chesstango.board.movesgenerators.pseudo.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.moves.MoveFactory;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorByPiecePositioned;
import net.chesstango.board.position.BoardReader;
import net.chesstango.board.position.ColorBoardReader;


/**
 * @author Mauricio Coria
 *
 */
//TODO: implementar el calculo de movimientos lo puede hacer en funcion de ColorBoard
public abstract class AbstractMoveGenerator implements MoveGeneratorByPiecePositioned {
	
	protected final Color color;
	
	protected BoardReader board;

	protected ColorBoardReader colorBoard;
	
	protected MoveFactory moveFactory;
	
	public AbstractMoveGenerator(Color color) {
		this.color = color;
	}

	public void setBoard(BoardReader board) {
		this.board = board;
	}	

	public void setColorBoard(ColorBoardReader colorBoard) {
		this.colorBoard = colorBoard;
	}

	public void setMoveFactory(MoveFactory moveFactory) {
		this.moveFactory = moveFactory;
	}
	
}
