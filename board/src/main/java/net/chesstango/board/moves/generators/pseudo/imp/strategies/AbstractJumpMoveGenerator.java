package net.chesstango.board.moves.generators.pseudo.imp.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPieceResult;
import net.chesstango.board.moves.PseudoMove;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
//TODO: En vez de computar los saltos podriamos ya tenerlos precargados para usar un iterador de bits
public abstract class AbstractJumpMoveGenerator extends AbstractMoveGenerator {

    /**
     * Factory Method
     */
    protected abstract PseudoMove createSimpleMove(PiecePositioned from, PiecePositioned to);

    /**
     * Factory Method
     */
    protected abstract PseudoMove createCaptureMove(PiecePositioned from, PiecePositioned to);

    protected abstract Iterator<Square> getSquareIterator(Square fromSquare);

    public AbstractJumpMoveGenerator(Color color) {
        super(color);
    }

    /**
     * Generates pseudo-moves for a given piece from a specified position.
     * This method iterates through the possible destination squares and generates
     * pseudo-moves based on the type of move (simple move or capture move).
     * <p>
     * The generatePseudoMoves method is considered a <b>template method</b> because it defines
     * the overall structure of the algorithm for generating pseudo-moves for a piece,
     * but it defers some specific steps to subclasses.
     * This method is final, meaning it cannot be overridden, ensuring that the general
     * process remains consistent across different implementations.
     * However, it relies on abstract methods (createSimpleMove, createCaptureMove, and getSquareIterator)
     * that must be implemented by subclasses to provide specific behavior for creating moves
     * and iterating over squares. This design allows for flexibility and customization in
     * the move generation process while maintaining a consistent algorithm structure
     *
     * @param from the piece positioned from which to generate pseudo-moves
     * @return a result object containing the generated pseudo-moves
     */
    @Override
    public final MoveGeneratorByPieceResult generateByPiecePseudoMoves(PiecePositioned from) {
        MoveGeneratorByPieceResult result = new MoveGeneratorByPieceResult(from);
        Square fromSquare = from.getSquare();
        Iterator<Square> iterator = getSquareIterator(fromSquare);
        while (iterator.hasNext()) {
            Square to = iterator.next();
            result.addAffectedByPositions(to);
            result.addCapturedPositions(to);
            Color colorDestino = bitBoard.getColor(to);
            if (colorDestino == null) {
                PseudoMove move = createSimpleMove(from, squareBoard.getPosition(to));
                result.addPseudoMove(move);
            } else if (color.oppositeColor().equals(colorDestino)) {
                PseudoMove move = createCaptureMove(from, squareBoard.getPosition(to));
                result.addPseudoMove(move);
            }
        }
        return result;
    }

}
