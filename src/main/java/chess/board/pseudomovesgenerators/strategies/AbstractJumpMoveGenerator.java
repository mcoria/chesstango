package chess.board.pseudomovesgenerators.strategies;

import java.util.Iterator;

import chess.board.Color;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.square.JumpSquareIterator;
import chess.board.moves.Move;
import chess.board.pseudomovesgenerators.MoveGeneratorResult;

/**
 * @author Mauricio Coria
 *
 */
//TODO: En vez de computar los saltos podriamos ya tenerlos precargados para usar un iterador de bits
public abstract class AbstractJumpMoveGenerator extends AbstractMoveGenerator {
	
	protected abstract Move createSimpleMove(PiecePositioned origen, PiecePositioned destino);
	
	protected abstract Move createCaptureMove(PiecePositioned origen, PiecePositioned destino);

	protected abstract Iterator<Square> getSquareIterator(Square fromSquare);

	public AbstractJumpMoveGenerator(Color color) {
		super(color);
	}

	//El calculo de movimientos lo puede hacer en funcion de ColorBoard
	
	@Override
	public MoveGeneratorResult generatePseudoMoves(PiecePositioned from){
		MoveGeneratorResult result = new MoveGeneratorResult(from);
		Square fromSquare = from.getKey();
		Iterator<Square> iterator = getSquareIterator(fromSquare);
		while (iterator.hasNext()) {
			Square to = iterator.next();
			result.addAffectedByPositions(to);
			result.addCapturedPositions(to);
			Color colorDestino = colorBoard.getColor(to);
			if (colorDestino == null) {
				Move move = createSimpleMove(from, piecePlacement.getPosicion(to));
				result.addPseudoMove(move);
			} else if (color.oppositeColor().equals(colorDestino)) {
				Move move = createCaptureMove(from, piecePlacement.getPosicion(to));
				result.addPseudoMove(move);
			}
			// else if(color.equals(pieza.getColor())){
				// continue;
			// }
		}
		return result;
	}

}
