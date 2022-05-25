package chess.board.movesgenerators.pseudo.strategies;

import chess.board.Color;
import chess.board.moves.MoveFactory;
import chess.board.position.PiecePlacementReader;
import chess.board.position.imp.ColorBoard;
import chess.board.movesgenerators.pseudo.MoveGeneratorByPiecePositioned;


/**
 * @author Mauricio Coria
 *
 */
//TODO: implementar el calculo de movimientos lo puede hacer en funcion de ColorBoard
public abstract class AbstractMoveGenerator implements MoveGeneratorByPiecePositioned {
	
	protected final Color color;
	
	protected PiecePlacementReader piecePlacement;

	protected ColorBoard colorBoard;	
	
	protected MoveFactory moveFactory;
	
	public AbstractMoveGenerator(Color color) {
		this.color = color;
	}

	public void setPiecePlacement(PiecePlacementReader piecePlacement) {
		this.piecePlacement = piecePlacement;
	}	

	public void setColorBoard(ColorBoard colorBoard) {
		this.colorBoard = colorBoard;
	}

	public void setMoveFactory(MoveFactory moveFactory) {
		this.moveFactory = moveFactory;
	}
	
}
