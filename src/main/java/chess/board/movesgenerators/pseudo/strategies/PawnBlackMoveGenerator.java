package chess.board.movesgenerators.pseudo.strategies;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.Cardinal;
import chess.board.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public class PawnBlackMoveGenerator extends AbstractPawnMoveGenerator {
	
	private static final Piece[] PROMOCIONES_BLACK = new Piece[]{Piece.ROOK_BLACK, Piece.KNIGHT_BLACK, Piece.BISHOP_BLACK, Piece.QUEEN_BLACK};
	
	public PawnBlackMoveGenerator() {
		super(Color.BLACK);
	}


	@Override
	protected Square getSquareSimplePawnMove(Square square) {
		return Square.getSquare(square.getFile(), square.getRank() - 1);
	}

	@Override
	protected Square getSquareSimpleTwoSquaresPawnMove(Square square) {
		return square.getRank() == 6 ? Square.getSquare(square.getFile(), 4) : null;
	}

	@Override
	protected Square getSquareAttackLeft(Square square) {
		return Square.getSquare(square.getFile() - 1, square.getRank() - 1);
	}
	
	@Override
	protected Square getSquareAttackRight(Square square) {
		return Square.getSquare(square.getFile() + 1, square.getRank() - 1);
	}

	@Override
	protected Piece[] getPromotionPieces() {
		return PROMOCIONES_BLACK;
	}

	@Override
	protected Move createSimplePawnMove(PiecePositioned origen, PiecePositioned destino) {
		return this.moveFactory.createSimplePawnMove(origen, destino);
	}

	@Override
	protected Move createSimpleTwoSquaresPawnMove(PiecePositioned origen, PiecePositioned destino, Square saltoSimpleCasillero) {
		return this.moveFactory.createSimpleTwoSquaresPawnMove(origen, destino, saltoSimpleCasillero);
	}

	@Override
	protected Move createCapturePawnMoveLeft(PiecePositioned origen, PiecePositioned destino) {
		return this.moveFactory.createCapturePawnMove(origen, destino, Cardinal.SurOeste);
	}

	@Override
	protected Move createCapturePawnMoveRight(PiecePositioned origen, PiecePositioned destino) {
		return this.moveFactory.createCapturePawnMove(origen, destino, Cardinal.SurEste);
	}
}
