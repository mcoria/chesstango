package chess.board.pseudomovesgenerators.strategies;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.moves.Move;

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
	public void generateMovesPseudoMoves(PiecePositioned origen){
		
		int toRank = -1; //Just in case
		Square casillero = origen.getKey();
		Square saltoSimpleCasillero = getCasilleroSaltoSimple(casillero);
		Square saltoDobleCasillero = getCasilleroSaltoDoble(casillero);
		
		Square casilleroAtaqueIzquirda = getCasilleroAtaqueIzquirda(casillero);
		Square casilleroAtaqueDerecha = getCasilleroAtaqueDerecha(casillero);
		
			
		PiecePositioned destino = null;
		
		if (saltoSimpleCasillero != null) {
			destino = this.piecePlacement.getPosicion(saltoSimpleCasillero);
			this.result.affectedByContainerAdd(saltoSimpleCasillero);
			// Esta vacio? consultamos de esta forma para evitar ir dos veces el tablero
			if (destino.getValue() == null) {
				Move moveSaltoSimple = this.createSimpleMove(origen, destino);
				
				// En caso de promocion
				toRank = saltoSimpleCasillero.getRank();
				if (toRank == 0 || toRank == 7) { // Es una promocion
					addSaltoSimplePromocion(origen, destino);
				} else {
					result.moveContainerAdd(moveSaltoSimple);
					
					if (saltoDobleCasillero != null) {
						destino = this.piecePlacement.getPosicion(saltoDobleCasillero);
						result.affectedByContainerAdd(saltoDobleCasillero);
						// Esta vacio? consultamos de esta forma para evitar ir dos veces el tablero
						if (destino.getValue() == null) {
							Move moveSaltoDoble = this.createSaltoDoblePawnMove(origen, destino, saltoSimpleCasillero);
							result.moveContainerAdd(moveSaltoDoble);
						}
					}					
				}
			}
		}

		if (casilleroAtaqueIzquirda != null) {			
			destino = this.piecePlacement.getPosicion(casilleroAtaqueIzquirda);
			this.result.affectedByContainerAdd(casilleroAtaqueIzquirda);
			this.result.capturedPositionsContainerAdd(casilleroAtaqueIzquirda);
			Piece piece = destino.getValue();
			// El casillero es ocupado por una pieza contraria?
			if (piece != null && color.oppositeColor().equals(piece.getColor())) {
				Move moveCaptura = this.createCaptureMoveIzquierda(origen, destino);
				// En caso de promocion
				toRank = saltoSimpleCasillero.getRank();
				if (toRank == 0 || toRank == 7) { // Es una promocion
					addCapturaPromocion(origen, destino);
				} else {
					result.moveContainerAdd(moveCaptura);
				}

			}
		}

		if (casilleroAtaqueDerecha != null) {
			destino = this.piecePlacement.getPosicion(casilleroAtaqueDerecha);
			this.result.affectedByContainerAdd(casilleroAtaqueDerecha);
			this.result.capturedPositionsContainerAdd(casilleroAtaqueDerecha);
			Piece piece = destino.getValue();
			// El casillero es ocupado por una pieza contraria?			
			if (piece != null && color.oppositeColor().equals(piece.getColor())) {
				Move moveCaptura =  this.createCaptureMoveDerecha(origen, destino);

				toRank = saltoSimpleCasillero.getRank();
				if (toRank == 0 || toRank == 7) { // Es una promocion
					addCapturaPromocion(origen, destino);
				} else {
					result.moveContainerAdd(moveCaptura);
				}
			}
		}
	}

	private void addSaltoSimplePromocion(PiecePositioned origen, PiecePositioned destino) {
		Piece[] promociones = getPiezaPromocion();
		for (int i = 0; i < promociones.length; i++) {
			this.result.moveContainerAdd(this.moveFactory.createSimplePawnPromocion(origen, destino, promociones[i]));
		}
	}
	
	private void addCapturaPromocion(PiecePositioned origen, PiecePositioned destino) {
		Piece[] promociones = getPiezaPromocion();
		for (int i = 0; i < promociones.length; i++) {
			this.result.moveContainerAdd(this.moveFactory.createCapturePawnPromocion(origen, destino, promociones[i]));
		}
	}


}
