package net.chesstango.board.movesgenerators.pseudo.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.moves.MoveFactory;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorByPiecePositioned;
import net.chesstango.board.position.SquareBoardReader;
import net.chesstango.board.position.BitBoardReader;


/**
 * @author Mauricio Coria
 *
 */
//TODO: implementar el calculo de movimientos lo puede hacer en funcion de ColorBoard
public abstract class AbstractMoveGenerator implements MoveGeneratorByPiecePositioned {
	
	protected final Color color;
	
	protected SquareBoardReader squareBoard;

	protected BitBoardReader bitBoard;
	
	protected MoveFactory moveFactory;
	
	public AbstractMoveGenerator(Color color) {
		this.color = color;
	}

	public void setSquareBoard(SquareBoardReader squareBoard) {
		this.squareBoard = squareBoard;
	}	

	public void setBitBoard(BitBoardReader bitBoard) {
		this.bitBoard = bitBoard;
	}

	public void setMoveFactory(MoveFactory moveFactory) {
		this.moveFactory = moveFactory;
	}
	
}
