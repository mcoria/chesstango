package movegenerators;

import chess.BoardState;
import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import moveexecutors.Move;

/**
 * @author Mauricio Coria
 *
 */
public abstract class PeonAbstractMoveGenerator extends AbstractMoveGenerator {
	
	protected BoardState boardState;

	protected abstract Square getCasilleroSaltoSimple(Square casillero);

	protected abstract Square getCasilleroSaltoDoble(Square casillero);

	protected abstract Square getCasilleroAtaqueIzquirda(Square casillero);
	
	protected abstract Square getCasilleroAtaqueDerecha(Square casillero);
	
	protected abstract PosicionPieza getCapturaPeonPasante(Square peonPasanteSquare);	
	
	protected abstract Pieza[] getPiezaPromocion();
	
	
	private boolean saveMovesInCache;
	
	private boolean hasCapturePeonPasante;

	
	public PeonAbstractMoveGenerator(Color color) {
		super(color);
	}
	
	@Override
	public void generateMovesPseudoMoves(PosicionPieza origen){
		this.saveMovesInCache = true;
		this.hasCapturePeonPasante = false;
		
		int toRank = -1; //Just in case
		Square casillero = origen.getKey();
		Square saltoSimpleCasillero = getCasilleroSaltoSimple(casillero);
		Square saltoDobleCasillero = getCasilleroSaltoDoble(casillero);
		
		Square casilleroAtaqueIzquirda = getCasilleroAtaqueIzquirda(casillero);
		Square casilleroAtaqueDerecha = getCasilleroAtaqueDerecha(casillero);
		
		Square peonPasanteSquare = boardState.getPeonPasanteSquare();
		
			
		PosicionPieza destino = null;
		
		if (saltoSimpleCasillero != null) {
			destino = this.tablero.getPosicion(saltoSimpleCasillero);
			this.result.affectedByContainerAdd(saltoSimpleCasillero);
			// Esta vacio? consultamos de esta forma para evitar ir dos veces el tablero
			if (destino.getValue() == null) {
				Move moveSaltoSimple = this.moveFactory.createSimpleMove(origen, destino);
				
				// En caso de promocion
				toRank = saltoSimpleCasillero.getRank();
				if (toRank == 0 || toRank == 7) { // Es una promocion
					addSaltoSimplePromocion(origen, destino);
				} else {
					result.moveContainerAdd(moveSaltoSimple);
					
					if (saltoDobleCasillero != null) {
						destino = this.tablero.getPosicion(saltoDobleCasillero);
						result.affectedByContainerAdd(saltoDobleCasillero);
						// Esta vacio? consultamos de esta forma para evitar ir dos veces el tablero
						if (destino.getValue() == null) {
							Move moveSaltoDoble = this.moveFactory.createSaltoDoblePeonMove(origen, destino, saltoSimpleCasillero);
							result.moveContainerAdd(moveSaltoDoble);
						}
					}					
				}
			}
		}

		if (casilleroAtaqueIzquirda != null) {			
			destino = this.tablero.getPosicion(casilleroAtaqueIzquirda);
			result.affectedByContainerAdd(casilleroAtaqueIzquirda);
			Pieza pieza = destino.getValue();
			// El casillero es ocupado por una pieza contraria?
			if (pieza != null && color.opositeColor().equals(pieza.getColor())) {
				Move moveCaptura = this.moveFactory.createCaptureMove(origen, destino);
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
			destino = this.tablero.getPosicion(casilleroAtaqueDerecha);
			result.affectedByContainerAdd(casilleroAtaqueDerecha);
			Pieza pieza = destino.getValue();
			// El casillero es ocupado por una pieza contraria?			
			if (pieza != null && color.opositeColor().equals(pieza.getColor())) {
				Move moveCaptura =  this.moveFactory.createCaptureMove(origen, destino);

				toRank = saltoSimpleCasillero.getRank();
				if (toRank == 0 || toRank == 7) { // Es una promocion
					addCapturaPromocion(origen, destino);
				} else {
					result.moveContainerAdd(moveCaptura);
				}
			}
		}
		
		if (peonPasanteSquare != null) {
			if (peonPasanteSquare.equals(casilleroAtaqueIzquirda) || peonPasanteSquare.equals(casilleroAtaqueDerecha)) {
				destino = this.tablero.getPosicion(peonPasanteSquare);
		    	Move move = this.moveFactory.createCapturePeonPasante(origen, destino, getCapturaPeonPasante(peonPasanteSquare));
		    	this.saveMovesInCache = false;
		    	this.hasCapturePeonPasante = true;
		    	result.moveContainerAdd(move);
			}
		}
	}

	private void addSaltoSimplePromocion(PosicionPieza origen, PosicionPieza destino) {
		Pieza[] promociones = getPiezaPromocion();
		for (int i = 0; i < promociones.length; i++) {
			this.result.moveContainerAdd(this.moveFactory.createSimplePeonPromocion(origen, destino, promociones[i]));
		}
	}
	
	private void addCapturaPromocion(PosicionPieza origen, PosicionPieza destino) {
		Pieza[] promociones = getPiezaPromocion();
		for (int i = 0; i < promociones.length; i++) {
			this.result.moveContainerAdd(this.moveFactory.createCapturePeonPromocion(origen, destino, promociones[i]));
		}
	}

	@Override
	public boolean puedeCapturarPosicion(PosicionPieza origen, Square square) {
		if(square.equals(getCasilleroAtaqueIzquirda(origen.getKey())) ||
		   square.equals(getCasilleroAtaqueDerecha(origen.getKey())) ){
			return true;
		}
		return false;
	}
	
	@Override
	protected Move createSimpleMove(PosicionPieza origen, PosicionPieza destino) {
		return this.moveFactory.createSimpleMove(origen, destino);
	}


	@Override
	protected Move createCaptureMove(PosicionPieza origen, PosicionPieza destino) {
		return this.moveFactory.createCaptureMove(origen, destino);
	}	

	@Override
	public boolean saveMovesInCache() {
		return this.saveMovesInCache;
	}
	
	@Override
	public boolean hasCapturePeonPasante() {
		return this.hasCapturePeonPasante;
	}	

	public void setBoardState(BoardState boardState) {
		this.boardState = boardState;
	}	

}
