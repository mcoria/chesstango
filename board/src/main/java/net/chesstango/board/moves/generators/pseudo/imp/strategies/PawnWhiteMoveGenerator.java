package net.chesstango.board.moves.generators.pseudo.imp.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.moves.MoveCommand;

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
		return Square.getSquare(square.getFile(), square.getRank() + 1);
	}

	@Override
	protected Square getTwoSquareForward(Square square) {
		return square.getRank() == 1 ? Square.getSquare(square.getFile(), 3) : null;
	}

	@Override
	protected Square getAttackSquareLeft(Square square) {
		return Square.getSquare(square.getFile() - 1, square.getRank() + 1);
	}
	
	@Override
	protected Square getAttackSquareRight(Square square) {
		return Square.getSquare(square.getFile() + 1, square.getRank() + 1);
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
	public MovePair<MoveCommand> generateEnPassantPseudoMoves() {
		Square pawnPasanteSquare = positionState.getEnPassantSquare();
		MovePair<MoveCommand> moveContainer = new MovePair<>();
		if (pawnPasanteSquare != null) {
			PiecePositioned from = null;
			PiecePositioned capture = null;

			Square casilleroPawnIzquirda = Square.getSquare(pawnPasanteSquare.getFile() - 1, pawnPasanteSquare.getRank() - 1);
			if (casilleroPawnIzquirda != null) {
				from = squareBoard.getPosition(casilleroPawnIzquirda);
				capture = squareBoard.getPosition(Square.getSquare(pawnPasanteSquare.getFile(), pawnPasanteSquare.getRank() - 1));
				if (Piece.PAWN_WHITE.equals(from.getPiece())) {
					MoveCommand move = moveFactory.createCaptureEnPassantPawnMove(from, squareBoard.getPosition(pawnPasanteSquare), capture, Cardinal.NorteEste);
					moveContainer.setFirst(move);
				}
			}

			Square casilleroPawnDerecha = Square.getSquare(pawnPasanteSquare.getFile() + 1, pawnPasanteSquare.getRank() - 1);
			if (casilleroPawnDerecha != null) {
				from = squareBoard.getPosition(casilleroPawnDerecha);
				capture = squareBoard.getPosition(Square.getSquare(pawnPasanteSquare.getFile(), pawnPasanteSquare.getRank() - 1));
				if (Piece.PAWN_WHITE.equals(from.getPiece())) {
					MoveCommand move = moveFactory.createCaptureEnPassantPawnMove(from, squareBoard.getPosition(pawnPasanteSquare), capture, Cardinal.NorteOeste);
					moveContainer.setSecond(move);
				}
			}
		}
		return moveContainer;
	}
}
