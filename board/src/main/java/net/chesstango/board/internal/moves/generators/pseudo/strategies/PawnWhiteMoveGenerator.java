package net.chesstango.board.internal.moves.generators.pseudo.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.moves.PseudoMove;

/**
 * @author Mauricio Coria
 *
 */
public class PawnWhiteMoveGenerator extends AbstractPawnMoveGenerator {
	
	private static final Piece[] WHITE_PROMOTIONS = new Piece[]{Piece.ROOK_WHITE, Piece.KNIGHT_WHITE, Piece.BISHOP_WHITE, Piece.QUEEN_WHITE};
	
	public PawnWhiteMoveGenerator() {
		super(Color.WHITE);
	}

	@Override
	protected Square getOneSquareForward(Square square) {
		return Square.of(square.getFile(), square.getRank() + 1);
	}

	@Override
	protected Square getTwoSquareForward(Square square) {
		return square.getRank() == 1 ? Square.of(square.getFile(), 3) : null;
	}

	@Override
	protected Square getAttackSquareLeft(Square square) {
		return Square.of(square.getFile() - 1, square.getRank() + 1);
	}
	
	@Override
	protected Square getAttackSquareRight(Square square) {
		return Square.of(square.getFile() + 1, square.getRank() + 1);
	}

	@Override
	protected Piece[] getPromotionPieces() {
		return WHITE_PROMOTIONS;
	}

	@Override
	protected Cardinal getDiagonalLeftDirection() {
		return Cardinal.NorteOeste;
	}

	@Override
	protected Cardinal getDiagonalRightDirection() {
		return Cardinal.NorteEste;
	}


	@Override
	public MovePair<PseudoMove> generateEnPassantPseudoMoves() {
		Square pawnPasanteSquare = positionState.getEnPassantSquare();
		MovePair<PseudoMove> moveContainer = new MovePair<>();
		if (pawnPasanteSquare != null) {
			PiecePositioned from = null;
			PiecePositioned capture = null;

			Square casilleroPawnIzquirda = Square.of(pawnPasanteSquare.getFile() - 1, pawnPasanteSquare.getRank() - 1);
			if (casilleroPawnIzquirda != null) {
				from = squareBoard.getPosition(casilleroPawnIzquirda);
				capture = squareBoard.getPosition(Square.of(pawnPasanteSquare.getFile(), pawnPasanteSquare.getRank() - 1));
				if (Piece.PAWN_WHITE.equals(from.getPiece())) {
					PseudoMove move = moveFactory.createCaptureEnPassantPawnMove(from, squareBoard.getPosition(pawnPasanteSquare), capture, Cardinal.NorteEste);
					moveContainer.setFirst(move);
				}
			}

			Square casilleroPawnDerecha = Square.of(pawnPasanteSquare.getFile() + 1, pawnPasanteSquare.getRank() - 1);
			if (casilleroPawnDerecha != null) {
				from = squareBoard.getPosition(casilleroPawnDerecha);
				capture = squareBoard.getPosition(Square.of(pawnPasanteSquare.getFile(), pawnPasanteSquare.getRank() - 1));
				if (Piece.PAWN_WHITE.equals(from.getPiece())) {
					PseudoMove move = moveFactory.createCaptureEnPassantPawnMove(from, squareBoard.getPosition(pawnPasanteSquare), capture, Cardinal.NorteOeste);
					moveContainer.setSecond(move);
				}
			}
		}
		return moveContainer;
	}
}
