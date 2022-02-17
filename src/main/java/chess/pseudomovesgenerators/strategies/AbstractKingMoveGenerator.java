package chess.pseudomovesgenerators.strategies;

import chess.Color;
import chess.PiecePositioned;
import chess.Square;
import chess.position.imp.PositionState;


/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractKingMoveGenerator extends AbstractJumpMoveGenerator {
	
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
	
	public AbstractKingMoveGenerator(Color color) {
		super(color, SALTOS_KING);
	}	

	protected boolean puedeEnroqueQueen(
			final PiecePositioned origen, 
			final PiecePositioned king,
			final PiecePositioned torre,
			final Square casilleroIntermedioRook,
			final Square casilleroDestinoKing, 
			final Square casilleroIntermedioKing) {
		if ( king.equals(origen) ) {           																	//El king se encuentra en su lugar
			if (torre.getValue().equals(piecePlacement.getPieza(torre.getKey()))) {								  	//La torre se encuentra en su lugar
				if ( piecePlacement.isEmtpy(casilleroIntermedioRook)													//El casillero intermedio ROOK esta vacio
				  && piecePlacement.isEmtpy(casilleroDestinoKing) 														//El casillero destino KING esta vacio
				  && piecePlacement.isEmtpy(casilleroIntermedioKing)) {										  			//El casillero intermedio KING esta vacio
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
			if (torre.getValue().equals(piecePlacement.getPieza(torre.getKey()))) {								  	//La torre se encuentra en su lugar
				if ( piecePlacement.isEmtpy(casilleroDestinoKing) 														//El casillero destino KING esta vacio
				  && piecePlacement.isEmtpy(casilleroIntermedioKing)) {										  			//El casillero intermedio KING esta vacio
						return true;
				}
			}
		}
		return false;
	}

	public void setBoardState(PositionState positionState) {
		this.positionState = positionState;
	}
	
}
