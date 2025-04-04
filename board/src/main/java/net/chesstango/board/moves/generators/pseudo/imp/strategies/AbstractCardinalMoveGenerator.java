package net.chesstango.board.moves.generators.pseudo.imp.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.iterators.bysquare.CardinalSquareIterator;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPieceResult;
import net.chesstango.board.moves.PseudoMove;

import java.util.Iterator;


/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractCardinalMoveGenerator extends AbstractMoveGenerator {
	
	/**
	 * Factory Method
	 */
	protected abstract PseudoMove createSimpleMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal);

	/**
	 * Factory Method
	 */
	protected abstract PseudoMove createCaptureMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal);
	
	private final Cardinal[] directions;

	public AbstractCardinalMoveGenerator(Color color, Cardinal[] directions) {
		super(color);
		this.directions = directions;
	}

	@Override
	public MoveGeneratorByPieceResult generateByPiecePseudoMoves(PiecePositioned from){
		MoveGeneratorByPieceResult result = new MoveGeneratorByPieceResult(from);
		for (Cardinal cardinal : this.directions) {
			getPseudoMoves(result, cardinal);
		}
		return result;
	}
	

	/**
	 * Generates pseudo-moves for a given piece from a specified position in a given cardinal direction.
	 * This method iterates through the squares in the specified cardinal direction and generates
	 * pseudo-moves based on the type of move (simple move or capture move).
	 * <p>
	 * The getPseudoMoves method is considered a <b>template method</b> because it defines the skeleton of an algorithm
	 * for generating pseudo-moves in a specific cardinal direction, but it defers some steps to subclasses.
	 * The method itself is final, meaning it cannot be overridden, ensuring the overall structure of the algorithm
	 * remains consistent. However, it relies on abstract methods (createSimpleMove and createCaptureMove)
	 * that must be implemented by subclasses to provide specific behavior for creating moves.
	 * This allows for flexibility and customization in the move generation process while maintaining
	 * a consistent algorithm structure.
	 *
	 * @param result   the result object to store generated pseudo-moves
	 * @param cardinal the cardinal direction to generate moves in
	 */
	protected final void getPseudoMoves(MoveGeneratorByPieceResult result, Cardinal cardinal) {
		PiecePositioned from = result.getFrom();
		Square squareFrom = from.getSquare();
		Iterator<Square> iterator = new CardinalSquareIterator(squareFrom, cardinal);
		while (iterator.hasNext()) {
			Square to = iterator.next();
			result.addAffectedByPositions(to);
			result.addCapturedPositions(to);
			Color colorDestino = bitBoard.getColor(to);
			if (colorDestino == null) {
				PseudoMove move = createSimpleMove(from, squareBoard.getPosition(to), cardinal);
				result.addPseudoMove(move);
			} else if (color.oppositeColor().equals(colorDestino)) {
				PseudoMove move = createCaptureMove(from, squareBoard.getPosition(to), cardinal);
				result.addPseudoMove(move);
				break;
			} else { // if(color.equals(pieza.getColor())){
				break;
			}
		}
	}

}
