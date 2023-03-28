package net.chesstango.board.movesgenerators.pseudo.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorResult;

/**
 * @author Mauricio Coria
 *
 */
abstract class AbstractPawnMoveGenerator extends AbstractMoveGenerator {

	protected abstract Square getSquareSimplePawnMove(Square square);

	protected abstract Square getSquareSimpleTwoSquaresPawnMove(Square square);

	protected abstract Square getSquareAttackLeft(Square square);
	
	protected abstract Square getSquareAttackRight(Square square);

	protected abstract Piece[] getPromotionPieces();

	protected abstract Cardinal getLeftDirection();

	protected abstract Cardinal getRightDirection();
	
	public AbstractPawnMoveGenerator(Color color) {
		super(color);
	}
	
	@Override
	public MoveGeneratorResult generatePseudoMoves(PiecePositioned from){
		MoveGeneratorResult result = new MoveGeneratorResult(from);
		
		int toRank = -1; //Just in case
		Square casillero = from.getSquare();
		Square saltoSimpleCasillero = getSquareSimplePawnMove(casillero);
		Square saltoDobleCasillero = getSquareSimpleTwoSquaresPawnMove(casillero);
		
		Square casilleroAtaqueIzquierda = getSquareAttackLeft(casillero);
		Square casilleroAtaqueDerecha = getSquareAttackRight(casillero);
		
			
		PiecePositioned destino = null;
		
		if (saltoSimpleCasillero != null) {
			destino = this.piecePlacement.getPosition(saltoSimpleCasillero);
			result.addAffectedByPositions(saltoSimpleCasillero);
			// Esta vacio? consultamos de esta forma para evitar ir dos veces el tablero
			if (destino.getPiece() == null) {
				Move moveSaltoSimple = this.createSimplePawnMove(from, destino);
				
				// En caso de promocion
				toRank = saltoSimpleCasillero.getRank();
				if (toRank == 0 || toRank == 7) { // Es una promocion
					addSaltoSimplePromocion(result, destino);
				} else {
					result.addPseudoMove(moveSaltoSimple);
					
					if (saltoDobleCasillero != null) {
						destino = this.piecePlacement.getPosition(saltoDobleCasillero);
						result.addAffectedByPositions(saltoDobleCasillero);
						// Esta vacio? consultamos de esta forma para evitar ir dos veces el tablero
						if (destino.getPiece() == null) {
							Move moveSaltoDoble = this.createSimpleTwoSquaresPawnMove(from, destino, saltoSimpleCasillero);
							result.addPseudoMove(moveSaltoDoble);
						}
					}					
				}
			}
		}

		if (casilleroAtaqueIzquierda != null) {
			destino = this.piecePlacement.getPosition(casilleroAtaqueIzquierda);
			result.addAffectedByPositions(casilleroAtaqueIzquierda);
			result.addCapturedPositions(casilleroAtaqueIzquierda);
			Piece piece = destino.getPiece();
			// El casillero es ocupado por una pieza contraria?
			if (piece != null && color.oppositeColor().equals(piece.getColor())) {
				Move moveCaptura = this.createCapturePawnMove(from, destino, getLeftDirection());
				// En caso de promocion
				toRank = saltoSimpleCasillero.getRank();
				if (toRank == 0 || toRank == 7) { // Es una promocion
					addCapturaPromocion(result, destino, getLeftDirection());
				} else {
					result.addPseudoMove(moveCaptura);
				}

			}
		}

		if (casilleroAtaqueDerecha != null) {
			destino = this.piecePlacement.getPosition(casilleroAtaqueDerecha);
			result.addAffectedByPositions(casilleroAtaqueDerecha);
			result.addCapturedPositions(casilleroAtaqueDerecha);
			Piece piece = destino.getPiece();
			// El casillero es ocupado por una pieza contraria?			
			if (piece != null && color.oppositeColor().equals(piece.getColor())) {
				Move moveCaptura =  this.createCapturePawnMove(from, destino, getRightDirection());

				toRank = saltoSimpleCasillero.getRank();
				if (toRank == 0 || toRank == 7) { // Es una promocion
					addCapturaPromocion(result, destino, getRightDirection());
				} else {
					result.addPseudoMove(moveCaptura);
				}
			}
		}

		return result;
	}


	private void addSaltoSimplePromocion(MoveGeneratorResult result, PiecePositioned destino) {
		PiecePositioned from = result.getFrom();
		Piece[] promociones = getPromotionPieces();
		for (int i = 0; i < promociones.length; i++) {
			result.addPseudoMove(this.moveFactory.createSimplePromotionPawnMove(from, destino, promociones[i]));
		}
	}

	private void addCapturaPromocion(MoveGeneratorResult result, PiecePositioned destino, Cardinal direction) {
		PiecePositioned from = result.getFrom();
		Piece[] promociones = getPromotionPieces();
		for (int i = 0; i < promociones.length; i++) {
			result.addPseudoMove(this.moveFactory.createCapturePromotionPawnMove(from, destino, promociones[i], direction));
		}
	}

	private Move createCapturePawnMove(PiecePositioned origen, PiecePositioned destino, Cardinal direction){
		return this.moveFactory.createCapturePawnMove(origen, destino, direction);
	}

	protected Move createSimplePawnMove(PiecePositioned origen, PiecePositioned destino) {
		return this.moveFactory.createSimpleOneSquarePawnMove(origen, destino);
	}

	protected Move createSimpleTwoSquaresPawnMove(PiecePositioned origen, PiecePositioned destino, Square saltoSimpleCasillero) {
		return this.moveFactory.createSimpleTwoSquaresPawnMove(origen, destino, saltoSimpleCasillero);
	}
}
