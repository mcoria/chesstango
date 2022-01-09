package chess.pseudomovesgenerators;

import chess.Color;
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.iterators.Cardinal;
import chess.iterators.CardinalSquareIterator;
import chess.position.PositionState;

//TODO: Esto se puede mejorar, que valide saldos a su alrededor excepto izquierda y derecha, donde se anida para validar enroque

/**
 * @author Mauricio Coria
 *
 */
public abstract class KingAbstractMoveGenerator extends AbstractJumpMoveGenerator {
	
	protected PositionState positionState;
	
	public final static int[][] SALTOS_KING = { { 0, 1 }, // Norte
			{ 1, 1 },   // NE
			{ -1, 1 },  // NO
			{ 0, -1 },  // Sur
			{ 1, -1 },  // SE
			{ -1, -1 }, // SO
			{ 1, 0 },   // Este
			{ -1, 0 },  // Oeste
	};
	
	public KingAbstractMoveGenerator(Color color) {
		super(color, SALTOS_KING);
	}	

	protected boolean puedeEnroqueQueen(
			final PiecePositioned origen, 
			final PiecePositioned king,
			final PiecePositioned torre,
			final Square casilleroIntermedioTorre,
			final Square casilleroDestinoKing, 
			final Square casilleroIntermedioKing) {
		if ( king.equals(origen) ) {           																	//El king se encuentra en su lugar
			if (torre.getValue().equals(tablero.getPieza(torre.getKey()))) {								  	//La torre se encuentra en su lugar
				if ( tablero.isEmtpy(casilleroIntermedioTorre)													//El casillero intermedio ROOK esta vacio
				  && tablero.isEmtpy(casilleroDestinoKing) 														//El casillero destino KING esta vacio
				  && tablero.isEmtpy(casilleroIntermedioKing)) {										  			//El casillero intermedio KING esta vacio
						return true;
				}
			}
		}
		return false;
	}
	
	protected boolean puedeEnroqueKing(
			final PiecePositioned origen, 
			final PiecePositioned king,
			final PiecePositioned torre,
			final Square casilleroDestinoKing, 
			final Square casilleroIntermedioKing) {
		if ( king.equals(origen) ) {           																	//El king se encuentra en su lugar
			if (torre.getValue().equals(tablero.getPieza(torre.getKey()))) {								  	//La torre se encuentra en su lugar
				if ( tablero.isEmtpy(casilleroDestinoKing) 														//El casillero destino KING esta vacio
				  && tablero.isEmtpy(casilleroIntermedioKing)) {										  			//El casillero intermedio KING esta vacio
						return true;
				}
			}
		}
		return false;
	}
	
	private final static Cardinal[] cardinalesAlfil = new Cardinal[] {Cardinal.NorteEste, Cardinal.SurEste, Cardinal.SurOeste, Cardinal.NorteOeste};
	private final static Cardinal[] cardinalesTorre = new Cardinal[] {Cardinal.Este, Cardinal.Oeste, Cardinal.Norte, Cardinal.Sur};
	
	public long getPinnedSquare(Square kingSquare) {
		Piece reina = Piece.getQueen(this.color.opositeColor());
		Piece torre = Piece.getTorre(this.color.opositeColor());
		Piece alfil = Piece.getAlfil(this.color.opositeColor());
		long pinnedCollection = 0;
		
		pinnedCollection |= getPinnedCardinales(kingSquare, alfil, reina, cardinalesAlfil);
		pinnedCollection |= getPinnedCardinales(kingSquare, torre, reina, cardinalesTorre);

		return pinnedCollection;
	}
	
	protected long getPinnedCardinales(Square kingSquare, Piece torreOAlfil, Piece reina,
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
	
	
	protected Square getPinned(Square kingSquare, Piece torreOAlfil, Piece reina, Cardinal cardinal) {
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
					Piece piece = this.tablero.getPieza(destino);
					if (torreOAlfil.equals(piece) || reina.equals(piece)) {
						return pinned;
					} else {
						return null;
					}
				}
			}

		}
		return null;
	}

	public void setBoardState(PositionState positionState) {
		this.positionState = positionState;
	}
	
}
