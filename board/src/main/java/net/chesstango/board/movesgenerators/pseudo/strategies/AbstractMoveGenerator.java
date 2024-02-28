package net.chesstango.board.movesgenerators.pseudo.strategies;

import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.moves.factories.MoveFactory;
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

	@Setter
	protected SquareBoardReader squareBoard;

	@Setter
	protected BitBoardReader bitBoard;
	
	public AbstractMoveGenerator(Color color) {
		this.color = color;
	}
	
}
