package movegenerators;

import chess.BoardState;
import chess.Color;
import chess.Move;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import moveexecutors.CapturaPeonPromocion;
import moveexecutors.CaptureMove;
import moveexecutors.CapturePeonPasante;
import moveexecutors.SaltoDoblePeonMove;
import moveexecutors.SimpleMove;
import moveexecutors.SimplePeonPromocion;

public abstract class PeonAbstractMoveGenerator extends AbstractMoveGenerator {
	
	protected BoardState boardState;

	protected abstract Square getCasilleroSaltoSimple(Square casillero);

	protected abstract Square getCasilleroSaltoDoble(Square casillero);

	protected abstract Square getCasilleroAtaqueIzquirda(Square casillero);
	
	protected abstract Square getCasilleroAtaqueDerecha(Square casillero);
	
	protected abstract PosicionPieza getCapturaPeonPasante(Square peonPasanteSquare);	
	
	protected abstract Pieza[] getPiezaPromocion();
	
	private boolean saveMovesInCache;

	
	public PeonAbstractMoveGenerator(Color color) {
		super(color);
	}
	
	@Override
	public void generateMoves(PosicionPieza origen){
		this.saveMovesInCache = true;
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
			result.affectedByContainerAdd(saltoSimpleCasillero);
			// Esta vacio? consultamos de esta forma para evitar ir dos veces el tablero
			if (destino.getValue() == null) {
				Move moveSaltoSimple = new SimpleMove(origen, destino);
				
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
							Move moveSaltoDoble = new SaltoDoblePeonMove(origen, destino, saltoSimpleCasillero);
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
				Move moveCaptura = new CaptureMove(origen, destino);
				
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
				Move moveCaptura = new CaptureMove(origen, destino);

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
		    	Move move = new CapturePeonPasante(origen, destino, getCapturaPeonPasante(peonPasanteSquare));
		    	this.saveMovesInCache = false;
		    	result.moveContainerAdd(move);
			}
		}
	}

	private void addSaltoSimplePromocion(PosicionPieza origen, PosicionPieza destino) {
		Pieza[] promociones = getPiezaPromocion();
		for (int i = 0; i < promociones.length; i++) {
			result.moveContainerAdd(new SimplePeonPromocion(origen, destino, promociones[i]));
		}
	}
	
	private void addCapturaPromocion(PosicionPieza origen, PosicionPieza destino) {
		Pieza[] promociones = getPiezaPromocion();
		for (int i = 0; i < promociones.length; i++) {
			result.moveContainerAdd(new CapturaPeonPromocion(origen, destino, promociones[i]));
		}
	}	

	@Override
	public boolean puedeCapturarPosicion(PosicionPieza origen, Square kingSquare) {
		if(kingSquare.equals(getCasilleroAtaqueIzquirda(origen.getKey())) ||
		   kingSquare.equals(getCasilleroAtaqueDerecha(origen.getKey())) ){
			return true;
		}
		return false;
	}

	@Override
	public boolean saveMovesInCache() {
		return this.saveMovesInCache;
	}	

	public void setBoardState(BoardState boardState) {
		this.boardState = boardState;
	}

}
