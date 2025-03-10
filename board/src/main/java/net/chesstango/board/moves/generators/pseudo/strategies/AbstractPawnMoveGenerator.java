package net.chesstango.board.moves.generators.pseudo.strategies;

import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.factories.PawnMoveFactory;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorResult;
import net.chesstango.board.moves.imp.MoveImp;

/**
 * The AbstractPawnMoveGenerator class applies the Template Method design pattern
 * by providing a structured algorithm for generating pawn moves, while allowing
 * subclasses to customize specific steps of the process, including the calculation
 * of square movements, attack directions, and promotion logic. Subclasses must
 * implement the abstract methods to define the customization points of this algorithm.
 * This pattern ensures the overall control flow remains consistent, following
 * the logic defined in the generatePseudoMoves() method, while details vary
 * based on the pawn's color or game context.
 * <p>
 * Template methods:
 * - generatePseudoMoves() provides the main algorithm framework.
 * Abstract methods to be implemented by subclasses:
 * - getSquareSimplePawnMove(Square square)
 * - getSquareSimpleTwoSquaresPawnMove(Square square)
 * - getSquareAttackLeft(Square square)
 * - getSquareAttackRight(Square square)
 * - getPromotionPieces()
 * - getLeftDirection()
 * - getRightDirection()
 *
 * @author Mauricio Coria
 */
@Setter
public abstract class AbstractPawnMoveGenerator extends AbstractMoveGenerator {

    private PawnMoveFactory moveFactory;

    /**
     * Determines the next square ahead for a pawn's simple forward move
     * based on its current position. The implementation will depend
     * on the pawn's color and direction of movement on the board.
     *
     * @param square The current square of the pawn.
     * @return The square directly ahead of the pawn for a single step move,
     * or null if the move is not valid.
     */
    protected abstract Square getOneSquareForward(Square square);


    /**
     * Determines the square two steps ahead for a pawn, based on its current position and movement logic.
     * This method is typically designed for pawns that are still in their initial position
     * and eligible for a two-square forward move.
     *
     * @param square The current square of the pawn.
     * @return The square located two steps ahead of the pawn for a valid double-step move,
     * or null if the move is not legal or not possible in the current context.
     */
    protected abstract Square getTwoSquareForward(Square square);
    
    /**
     * Determines the square reachable by a pawn to attack a piece to its left,
     * based on the pawn's current position and movement direction.
     *
     * @param square The current square of the pawn.
     * @return The square diagonally to the left of the pawn for an attack move,
     * or null if no such square is valid.
     */
    protected abstract Square getAttackSquareLeft(Square square);


    /**
     * Determines the square reachable by a pawn to attack a piece to its right,
     * based on the pawn's current position and movement direction.
     *
     * @param square The current square of the pawn.
     * @return The square diagonally to the right of the pawn for an attack move,
     * or null if no such square is valid.
     */
    protected abstract Square getAttackSquareRight(Square square);

    
    /**
     * Determines the array of pieces that a pawn can promote to
     * upon reaching the promotion rank.
     *
     * @return An array of Piece objects representing the valid promotion options for the pawn.
     */
    protected abstract Piece[] getPromotionPieces();

    protected abstract Cardinal getDiagonalLeftDirection();

    protected abstract Cardinal getDiagonalRightDirection();

    public AbstractPawnMoveGenerator(Color color) {
        super(color);
    }


    @Override
    public MoveGeneratorResult generatePseudoMoves(PiecePositioned from) {
        MoveGeneratorResult result = new MoveGeneratorResult(from);

        int toRank = -1; //Just in case
        Square square = from.getSquare();
        Square squareSimpleJump = getOneSquareForward(square);
        Square squareDoubleJump = getTwoSquareForward(square);

        Square squareAttackLeft = getAttackSquareLeft(square);
        Square squareAttackRight = getAttackSquareRight(square);

        PiecePositioned toPiecePositioned = null;

        
        // Handle simple forward move for the pawn
        if (squareSimpleJump != null) {
            toPiecePositioned = this.squareBoard.getPosition(squareSimpleJump);
            result.addAffectedByPositions(squareSimpleJump);
            // Check if the square ahead is empty
            if (toPiecePositioned.getPiece() == null) {
                MoveImp moveSaltoSimple = this.createSimplePawnMove(from, toPiecePositioned);

                // Check for pawn promotion when reaching the final rank
                toRank = squareSimpleJump.getRank();
                if (toRank == 0 || toRank == 7) { // Promotion scenario
                    addSimpleJumpPromotion(result, toPiecePositioned);
                } else {
                    result.addPseudoMove(moveSaltoSimple);

                    // Handle double-step forward move from the pawn's initial position
                    if (squareDoubleJump != null) {
                        toPiecePositioned = this.squareBoard.getPosition(squareDoubleJump);
                        result.addAffectedByPositions(squareDoubleJump);
                        // Check if the square two steps ahead is empty
                        if (toPiecePositioned.getPiece() == null) {
                            MoveImp moveSaltoDoble = this.createSimpleTwoSquaresPawnMove(from, toPiecePositioned, squareSimpleJump);
                            result.addPseudoMove(moveSaltoDoble);
                        }
                    }
                }
            }
        }


        // Handle diagonal left attack move for the pawn
        if (squareAttackLeft != null) {
            toPiecePositioned = this.squareBoard.getPosition(squareAttackLeft);
            result.addAffectedByPositions(squareAttackLeft); // Track the square as potentially affected by this move
            result.addCapturedPositions(squareAttackLeft);   // Mark the square as containing a captured piece
            Piece piece = toPiecePositioned.getPiece();      // Check if there is a piece on the square
            // Is the square occupied by an opponent's piece?
            if (piece != null && color.oppositeColor().equals(piece.getColor())) {
                MoveImp moveCapture = this.createCapturePawnMove(from, toPiecePositioned, getDiagonalLeftDirection());
                // Handle promotion if the capture move reaches the final rank
                toRank = squareSimpleJump.getRank();
                if (toRank == 0 || toRank == 7) { // Promotion scenario
                    addCapturePromotion(result, toPiecePositioned, getDiagonalLeftDirection());
                } else {
                    result.addPseudoMove(moveCapture); // Add the simple diagonal capture to result
                }
            }
        }

        // Handle diagonal right attack move for the pawn
        if (squareAttackRight != null) {
            toPiecePositioned = this.squareBoard.getPosition(squareAttackRight);
            result.addAffectedByPositions(squareAttackRight); // Track the square as potentially affected by this move
            result.addCapturedPositions(squareAttackRight);   // Mark the square as containing a captured piece
            Piece piece = toPiecePositioned.getPiece();       // Check if there is a piece on the square
            // Is the square occupied by an opponent's piece?
            if (piece != null && color.oppositeColor().equals(piece.getColor())) {
                MoveImp moveCapture = this.createCapturePawnMove(from, toPiecePositioned, getDiagonalRightDirection());
                // Handle promotion if the capture move reaches the final rank
                toRank = squareSimpleJump.getRank();
                if (toRank == 0 || toRank == 7) { // Promotion scenario
                    addCapturePromotion(result, toPiecePositioned, getDiagonalRightDirection());
                } else {
                    result.addPseudoMove(moveCapture); // Add the simple diagonal capture to result
                }
            }
        }

        return result;
    }


    private void addSimpleJumpPromotion(MoveGeneratorResult result, PiecePositioned toPiecePositioned) {
        PiecePositioned from = result.getFrom();
        Piece[] promotions = getPromotionPieces();
        for (Piece promotion : promotions) {
            result.addPseudoMove(this.moveFactory.createSimplePromotionPawnMove(from, toPiecePositioned, promotion));
        }
    }

    private void addCapturePromotion(MoveGeneratorResult result, PiecePositioned toPiecePositioned, Cardinal direction) {
        PiecePositioned from = result.getFrom();
        Piece[] promotions = getPromotionPieces();
        for (Piece promotion : promotions) {
            result.addPseudoMove(this.moveFactory.createCapturePromotionPawnMove(from, toPiecePositioned, promotion, direction));
        }
    }

    protected MoveImp createCapturePawnMove(PiecePositioned origen, PiecePositioned toPiecePositioned, Cardinal direction) {
        return this.moveFactory.createCapturePawnMove(origen, toPiecePositioned, direction);
    }

    protected MoveImp createSimplePawnMove(PiecePositioned origen, PiecePositioned toPiecePositioned) {
        return this.moveFactory.createSimpleOneSquarePawnMove(origen, toPiecePositioned);
    }

    protected MoveImp createSimpleTwoSquaresPawnMove(PiecePositioned origen, PiecePositioned toPiecePositioned, Square saltoSimpleCasillero) {
        return this.moveFactory.createSimpleTwoSquaresPawnMove(origen, toPiecePositioned, saltoSimpleCasillero);
    }
}
