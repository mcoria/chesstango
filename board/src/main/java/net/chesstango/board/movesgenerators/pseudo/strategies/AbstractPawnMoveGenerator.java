package net.chesstango.board.movesgenerators.pseudo.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
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

	protected abstract Move createSimplePawnMove(PiecePositioned origen, PiecePositioned destino);

	protected abstract Move createSimpleTwoSquaresPawnMove(PiecePositioned origen, PiecePositioned destino, Square saltoSimpleCasillero);


	protected abstract Move createCapturePawnMoveLeft(PiecePositioned origen, PiecePositioned destino);

	protected abstract Move createCapturePawnMoveRight(PiecePositioned origen, PiecePositioned destino);
	
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
		
		Square casilleroAtaqueIzquirda = getSquareAttackLeft(casillero);
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

		if (casilleroAtaqueIzquirda != null) {			
			destino = this.piecePlacement.getPosition(casilleroAtaqueIzquirda);
			result.addAffectedByPositions(casilleroAtaqueIzquirda);
			result.addCapturedPositions(casilleroAtaqueIzquirda);
			Piece piece = destino.getPiece();
			// El casillero es ocupado por una pieza contraria?
			if (piece != null && color.oppositeColor().equals(piece.getColor())) {
				Move moveCaptura = this.createCapturePawnMoveLeft(from, destino);
				// En caso de promocion
				toRank = saltoSimpleCasillero.getRank();
				if (toRank == 0 || toRank == 7) { // Es una promocion
					addCapturaPromocion(result, destino);
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
				Move moveCaptura =  this.createCapturePawnMoveRight(from, destino);

				toRank = saltoSimpleCasillero.getRank();
				if (toRank == 0 || toRank == 7) { // Es una promocion
					addCapturaPromocion(result, destino);
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
			result.addPseudoMove(this.moveFactory.createSimplePawnPromotion(from, destino, promociones[i]));
		}
	}
	
	private void addCapturaPromocion(MoveGeneratorResult result, PiecePositioned destino) {
		PiecePositioned from = result.getFrom();
		Piece[] promociones = getPromotionPieces();
		for (int i = 0; i < promociones.length; i++) {
			result.addPseudoMove(this.moveFactory.createCapturePawnPromotion(from, destino, promociones[i]));
		}
	}


}
