package chess.board.pseudomovesgenerators.strategies;

import chess.board.Color;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.square.JumpSquareIterator;
import chess.board.moves.Move;

import java.util.Iterator;


/**
 * @author Mauricio Coria
 *
 */
public class KnightMoveGenerator extends AbstractJumpMoveGenerator {
	
	public final static int[][] KNIGHT_JUMPS = {
			//Arriba
			{ -1, 2 }, 
			{ 1, 2 },
			
			//Derecha
			{ 2, -1 },
			{ 2, 1 },
			
			//Izquierda
			{ -2, -1 },
			{ -2, 1 },
			
			//Abajo
			{ -1, -2 }, 
			{ 1, -2 },
	};	
	
	public KnightMoveGenerator(Color color) {
		super(color);
	}
	
	@Override
	protected Move createSimpleMove(PiecePositioned origen, PiecePositioned destino) {
		return this.moveFactory.createSimpleMove(origen, destino);
	}


	@Override
	protected Move createCaptureMove(PiecePositioned origen, PiecePositioned destino) {
		return this.moveFactory.createCaptureMove(origen, destino);
	}

	@Override
	protected Iterator<Square> getSquareIterator(Square fromSquare) {
		return new JumpSquareIterator(fromSquare, KNIGHT_JUMPS);
	}

}
