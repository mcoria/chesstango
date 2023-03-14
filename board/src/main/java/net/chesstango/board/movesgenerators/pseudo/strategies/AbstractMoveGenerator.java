package net.chesstango.board.movesgenerators.pseudo.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.moves.MoveFactory;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorByPiecePositioned;
import net.chesstango.board.position.PiecePlacementReader;
import net.chesstango.board.position.imp.ColorBoard;


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
