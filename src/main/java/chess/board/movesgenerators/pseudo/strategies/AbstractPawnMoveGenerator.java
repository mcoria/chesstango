package chess.board.movesgenerators.pseudo.strategies;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.moves.Move;
import chess.board.movesgenerators.pseudo.MoveGeneratorResult;

/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractPawnMoveGenerator extends AbstractMoveGenerator {

	protected abstract Square getCasilleroSaltoSimple(Square casillero);

	protected abstract Square getCasilleroSaltoDoble(Square casillero);

	protected abstract Square getCasilleroAtaqueIzquirda(Square casillero);
	
	protected abstract Square getCasilleroAtaqueDerecha(Square casillero);
	
	protected abstract PiecePositioned getCapturaEnPassant(Square pawnPasanteSquare);	
	
	protected abstract Piece[] getPiezaPromocion();

	protected abstract Move createSimpleMove(PiecePositioned origen, PiecePositioned destino);

	protected abstract Move createSaltoDoblePawnMove(PiecePositioned origen, PiecePositioned destino, Square saltoSimpleCasillero);


	protected abstract Move createCaptureMoveIzquierda(PiecePositioned origen, PiecePositioned destino);

	protected abstract Move createCaptureMoveDerecha(PiecePositioned origen, PiecePositioned destino);
	
	public AbstractPawnMoveGenerator(Color color) {
		super(color);
	}
	
	@Override
	public MoveGeneratorResult generatePseudoMoves(PiecePositioned from){
		MoveGeneratorResult result = new MoveGeneratorResult(from);
		
		int toRank = -1; //Just in case
		Square casillero = from.getKey();
		Square saltoSimpleCasillero = getCasilleroSaltoSimple(casillero);
		Square saltoDobleCasillero = getCasilleroSaltoDoble(casillero);
		
		Square casilleroAtaqueIzquirda = getCasilleroAtaqueIzquirda(casillero);
		Square casilleroAtaqueDerecha = getCasilleroAtaqueDerecha(casillero);
		
			
		PiecePositioned destino = null;
		
		if (saltoSimpleCasillero != null) {
			destino = this.piecePlacement.getPosicion(saltoSimpleCasillero);
			result.addAffectedByPositions(saltoSimpleCasillero);
			// Esta vacio? consultamos de esta forma para evitar ir dos veces el tablero
			if (destino.getValue() == null) {
				Move moveSaltoSimple = this.createSimpleMove(from, destino);
				
				// En caso de promocion
				toRank = saltoSimpleCasillero.getRank();
				if (toRank == 0 || toRank == 7) { // Es una promocion
					addSaltoSimplePromocion(result, destino);
				} else {
					result.addPseudoMove(moveSaltoSimple);
					
					if (saltoDobleCasillero != null) {
						destino = this.piecePlacement.getPosicion(saltoDobleCasillero);
						result.addAffectedByPositions(saltoDobleCasillero);
						// Esta vacio? consultamos de esta forma para evitar ir dos veces el tablero
						if (destino.getValue() == null) {
							Move moveSaltoDoble = this.createSaltoDoblePawnMove(from, destino, saltoSimpleCasillero);
							result.addPseudoMove(moveSaltoDoble);
						}
					}					
				}
			}
		}

		if (casilleroAtaqueIzquirda != null) {			
			destino = this.piecePlacement.getPosicion(casilleroAtaqueIzquirda);
			result.addAffectedByPositions(casilleroAtaqueIzquirda);
			result.addCapturedPositions(casilleroAtaqueIzquirda);
			Piece piece = destino.getValue();
			// El casillero es ocupado por una pieza contraria?
			if (piece != null && color.oppositeColor().equals(piece.getColor())) {
				Move moveCaptura = this.createCaptureMoveIzquierda(from, destino);
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
			destino = this.piecePlacement.getPosicion(casilleroAtaqueDerecha);
			result.addAffectedByPositions(casilleroAtaqueDerecha);
			result.addCapturedPositions(casilleroAtaqueDerecha);
			Piece piece = destino.getValue();
			// El casillero es ocupado por una pieza contraria?			
			if (piece != null && color.oppositeColor().equals(piece.getColor())) {
				Move moveCaptura =  this.createCaptureMoveDerecha(from, destino);

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
		Piece[] promociones = getPiezaPromocion();
		for (int i = 0; i < promociones.length; i++) {
			result.addPseudoMove(this.moveFactory.createSimplePawnPromocion(from, destino, promociones[i]));
		}
	}
	
	private void addCapturaPromocion(MoveGeneratorResult result, PiecePositioned destino) {
		PiecePositioned from = result.getFrom();
		Piece[] promociones = getPiezaPromocion();
		for (int i = 0; i < promociones.length; i++) {
			result.addPseudoMove(this.moveFactory.createCapturePawnPromocion(from, destino, promociones[i]));
		}
	}


}
