package chess.board.pseudomovesgenerators.strategies;

import java.util.Iterator;

import chess.board.Color;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.square.JumpSquareIterator;
import chess.board.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractJumpMoveGenerator extends AbstractMoveGenerator {
	
	protected abstract Move createSimpleMove(PiecePositioned origen, PiecePositioned destino);
	
	protected abstract Move createCaptureMove(PiecePositioned origen, PiecePositioned destino);		
	
	private final int[][] saltos;
	
	public AbstractJumpMoveGenerator(Color color, int[][] saltos) {
		super(color);
		this.saltos = saltos;
	}

	//El calculo de movimientos lo puede hacer en funcion de ColorBoard
	
	@Override
	public void generateMovesPseudoMoves(PiecePositioned from) {
		Square fromSquare = from.getKey();
		Iterator<Square> iterator = new JumpSquareIterator(fromSquare, saltos);
		while (iterator.hasNext()) {
			Square to = iterator.next();
			this.result.affectedByContainerAdd(to);
			this.result.capturedPositionsContainerAdd(to);
			Color colorDestino = colorBoard.getColor(to);
			if (colorDestino == null) {
				Move move = createSimpleMove(from, piecePlacement.getPosicion(to));
				this.result.moveContainerAdd(move);
			} else if (color.opositeColor().equals(colorDestino)) {
				Move move = createCaptureMove(from, piecePlacement.getPosicion(to));
				this.result.moveContainerAdd(move);
			} // else if(color.equals(pieza.getColor())){
				// continue;
				// }
		}
	}

}
