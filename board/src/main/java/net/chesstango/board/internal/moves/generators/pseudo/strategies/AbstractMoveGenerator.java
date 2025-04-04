package net.chesstango.board.internal.moves.generators.pseudo.strategies;

import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPiece;
import net.chesstango.board.position.SquareBoardReader;
import net.chesstango.board.position.BitBoardReader;


/**
 * @author Mauricio Coria
 *
 */
//TODO: implementar el calculo de movimientos lo puede hacer en funcion de ColorBoard
public abstract class AbstractMoveGenerator implements MoveGeneratorByPiece {
	
	protected final Color color;

	@Setter
	protected SquareBoardReader squareBoard;

	@Setter
	protected BitBoardReader bitBoard;
	
	public AbstractMoveGenerator(Color color) {
		this.color = color;
	}
	
}
