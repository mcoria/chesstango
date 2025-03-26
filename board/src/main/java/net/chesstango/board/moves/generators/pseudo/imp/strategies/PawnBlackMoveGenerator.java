package net.chesstango.board.moves.generators.pseudo.imp.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.moves.imp.MoveCommand;
import net.chesstango.board.moves.imp.MoveImp;

/**
 * @author Mauricio Coria
 *
 */
public class PawnBlackMoveGenerator extends AbstractPawnMoveGenerator {
	
	private static final Piece[] BLACK_PROMOTIONS = new Piece[]{Piece.ROOK_BLACK, Piece.KNIGHT_BLACK, Piece.BISHOP_BLACK, Piece.QUEEN_BLACK};
	
	public PawnBlackMoveGenerator() {
		super(Color.BLACK);
	}

	@Override
	protected Square getOneSquareForward(Square square) {
		return Square.getSquare(square.getFile(), square.getRank() - 1);
	}

	@Override
	protected Square getTwoSquareForward(Square square) {
		return square.getRank() == 6 ? Square.getSquare(square.getFile(), 4) : null;
	}

	@Override
	protected Square getAttackSquareLeft(Square square) {
		return Square.getSquare(square.getFile() - 1, square.getRank() - 1);
	}
	
	@Override
	protected Square getAttackSquareRight(Square square) {
		return Square.getSquare(square.getFile() + 1, square.getRank() - 1);
	}

	@Override
	protected Piece[] getPromotionPieces() {
		return BLACK_PROMOTIONS;
	}

	@Override
	protected Cardinal getDiagonalLeftDirection() {
		return Cardinal.SurOeste;
	}

	@Override
	protected Cardinal getDiagonalRightDirection() {
		return Cardinal.SurEste;
	}

	@Override
	public MovePair<MoveCommand> generateEnPassantPseudoMoves() {
		Square pawnPasanteSquare = positionState.getEnPassantSquare();
		MovePair<MoveCommand> moveContainer = new MovePair<>();
		if (pawnPasanteSquare != null) {
			PiecePositioned from = null;
			PiecePositioned capture = null;

			Square casilleroPawnIzquirda = Square.getSquare(pawnPasanteSquare.getFile() - 1, pawnPasanteSquare.getRank() + 1);
			if (casilleroPawnIzquirda != null) {
				from = squareBoard.getPosition(casilleroPawnIzquirda);
				capture = squareBoard.getPosition(Square.getSquare(pawnPasanteSquare.getFile(), pawnPasanteSquare.getRank() + 1));
				if (Piece.PAWN_BLACK.equals(from.getPiece())) {
					MoveCommand move = moveFactory.createCaptureEnPassantPawnMove(from, squareBoard.getPosition(pawnPasanteSquare), capture, Cardinal.SurEste);
					moveContainer.setFirst(move);
				}
			}

			Square casilleroPawnDerecha = Square.getSquare(pawnPasanteSquare.getFile() + 1, pawnPasanteSquare.getRank() + 1);
			if (casilleroPawnDerecha != null) {
				from = squareBoard.getPosition(casilleroPawnDerecha);
				capture = squareBoard.getPosition(Square.getSquare(pawnPasanteSquare.getFile(), pawnPasanteSquare.getRank() + 1));
				if (Piece.PAWN_BLACK.equals(from.getPiece())) {
					MoveCommand move = moveFactory.createCaptureEnPassantPawnMove(from, squareBoard.getPosition(pawnPasanteSquare), capture, Cardinal.SurOeste);
					moveContainer.setSecond(move);
				}
			}
		}
		return moveContainer;
	}
}
