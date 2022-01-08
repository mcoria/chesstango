package chess.pseudomovesgenerators;

import chess.BoardState;
import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import chess.iterators.Cardinal;
import chess.iterators.CardinalSquareIterator;

//TODO: Esto se puede mejorar, que valide saldos a su alrededor excepto izquierda y derecha, donde se anida para validar enroque

/**
 * @author Mauricio Coria
 *
 */
public abstract class ReyAbstractMoveGenerator extends AbstractJumpMoveGenerator {
	
	protected BoardState boardState;
	
	public final static int[][] SALTOS_REY = { { 0, 1 }, // Norte
			{ 1, 1 },   // NE
			{ -1, 1 },  // NO
			{ 0, -1 },  // Sur
			{ 1, -1 },  // SE
			{ -1, -1 }, // SO
			{ 1, 0 },   // Este
			{ -1, 0 },  // Oeste
	};
	
	public ReyAbstractMoveGenerator(Color color) {
		super(color, SALTOS_REY);
	}	

	protected boolean puedeEnroqueReina(
			final PosicionPieza origen, 
			final PosicionPieza rey,
			final PosicionPieza torre,
			final Square casilleroIntermedioTorre,
			final Square casilleroDestinoRey, 
			final Square casilleroIntermedioRey) {
		if ( rey.equals(origen) ) {           																	//El rey se encuentra en su lugar
			if (torre.getValue().equals(tablero.getPieza(torre.getKey()))) {								  	//La torre se encuentra en su lugar
				if ( tablero.isEmtpy(casilleroIntermedioTorre)													//El casillero intermedio TORRE esta vacio
				  && tablero.isEmtpy(casilleroDestinoRey) 														//El casillero destino REY esta vacio
				  && tablero.isEmtpy(casilleroIntermedioRey)) {										  			//El casillero intermedio REY esta vacio
						return true;
				}
			}
		}
		return false;
	}
	
	protected boolean puedeEnroqueRey(
			final PosicionPieza origen, 
			final PosicionPieza rey,
			final PosicionPieza torre,
			final Square casilleroDestinoRey, 
			final Square casilleroIntermedioRey) {
		if ( rey.equals(origen) ) {           																	//El rey se encuentra en su lugar
			if (torre.getValue().equals(tablero.getPieza(torre.getKey()))) {								  	//La torre se encuentra en su lugar
				if ( tablero.isEmtpy(casilleroDestinoRey) 														//El casillero destino REY esta vacio
				  && tablero.isEmtpy(casilleroIntermedioRey)) {										  			//El casillero intermedio REY esta vacio
						return true;
				}
			}
		}
		return false;
	}
	
	private final static Cardinal[] cardinalesAlfil = new Cardinal[] {Cardinal.NorteEste, Cardinal.SurEste, Cardinal.SurOeste, Cardinal.NorteOeste};
	private final static Cardinal[] cardinalesTorre = new Cardinal[] {Cardinal.Este, Cardinal.Oeste, Cardinal.Norte, Cardinal.Sur};
	
	public long getPinnedSquare(Square kingSquare) {
		Pieza reina = Pieza.getReina(this.color.opositeColor());
		Pieza torre = Pieza.getTorre(this.color.opositeColor());
		Pieza alfil = Pieza.getAlfil(this.color.opositeColor());
		long pinnedCollection = 0;
		
		pinnedCollection |= getPinnedCardinales(kingSquare, alfil, reina, cardinalesAlfil);
		pinnedCollection |= getPinnedCardinales(kingSquare, torre, reina, cardinalesTorre);

		return pinnedCollection;
	}
	
	protected long getPinnedCardinales(Square kingSquare, Pieza torreOAlfil, Pieza reina,
			Cardinal[] direcciones) {
		long pinnedCollection = 0;
		for (Cardinal cardinal : direcciones) {
			Square pinned = getPinned(kingSquare, torreOAlfil, reina, cardinal);
			if (pinned != null) {
				pinnedCollection |= pinned.getPosicion();
			}
		}
		return pinnedCollection;
	}
	
	
	protected Square getPinned(Square kingSquare, Pieza torreOAlfil, Pieza reina, Cardinal cardinal) {
		Square pinned = null;
		CardinalSquareIterator iterator = new CardinalSquareIterator(kingSquare, cardinal);
		while (iterator.hasNext()) {
			Square destino = iterator.next();
			Color colorDestino = colorBoard.getColor(destino);
			if (colorDestino == null) {
				continue;
			}
			if (pinned == null) {
				if (color.equals(colorDestino)) {
					pinned = destino;
				} else { // if (color.opositeColor().equals(colorDestino))
					return null;
				}
			} else {
				if (color.equals(colorDestino)) {
					return null;
				} else { //// if (color.opositeColor().equals(colorDestino))
					Pieza pieza = this.tablero.getPieza(destino);
					if (torreOAlfil.equals(pieza) || reina.equals(pieza)) {
						return pinned;
					} else {
						return null;
					}
				}
			}

		}
		return null;
	}

	public void setBoardState(BoardState boardState) {
		this.boardState = boardState;
	}
	
}
