package net.chesstango.board.internal.moves.generators.pseudo.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.iterators.bysquare.CardinalSquareIterator;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPieceResult;
import net.chesstango.board.moves.PseudoMove;

import java.util.Iterator;



/**
 * Abstract class for generating pseudo-moves based on cardinal directions.
 * This class extends the AbstractMoveGenerator and provides a framework
 * for generating moves in specified cardinal directions.
 * Subclasses must implement the abstract methods to create specific types of moves.
 * The class uses a template method pattern to define the structure of the move generation process,
 * allowing subclasses to customize specific steps while maintaining the overall algorithm.
 *
 * @author Mauricio Coria
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

	/**
	 * Array of cardinal directions used for generating moves.
	 * This array holds the directions in which the piece can move,
	 * and is utilized by the move generation methods to iterate
	 * through possible moves in each direction.
	 */
	private final Cardinal[] directions;



	/**
	 * Constructor for AbstractCardinalMoveGenerator.
	 * Initializes the move generator with a specified color and cardinal directions.
	 * The Cardinal parameter specifies the directions in which a piece can move.
	 * This array of cardinal directions is used by the move generation methods to iterate
	 * through possible moves in each direction. By providing these directions,
	 * the constructor initializes the move generator with the specific movement capabilities of the piece,
	 * allowing it to generate appropriate pseudo-moves based on the given directions.
	 *
	 * @param color      the color of the piece for which moves are being generated
	 * @param directions an array of Cardinal directions in which the piece can move
	 */
	public AbstractCardinalMoveGenerator(Color color, Cardinal[] directions) {
		super(color);
		this.directions = directions;
	}


	/**
	 * Generates pseudo-moves for a given piece from a specified position.
	 * This method creates a new MoveGeneratorByPieceResult object for the given piece position
	 * and iterates through each cardinal direction to generate pseudo-moves.
	 * The generated pseudo-moves are stored in the result object.
	 *
	 * @param from the initial position of the piece for which to generate pseudo-moves
	 * @return a MoveGeneratorByPieceResult object containing the generated pseudo-moves
	 */
	@Override
	public final MoveGeneratorByPieceResult generateByPiecePseudoMoves(PiecePositioned from){
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
	 * This method is considered a <b>template method</b> because it defines the skeleton of an algorithm
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
	private void getPseudoMoves(MoveGeneratorByPieceResult result, Cardinal cardinal) {
		PiecePositioned from = result.getFrom();
		Square squareFrom = from.square();
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
			} else {
				break;
			}
		}
	}

}
