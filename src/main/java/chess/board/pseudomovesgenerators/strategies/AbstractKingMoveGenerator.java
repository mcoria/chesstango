package chess.board.pseudomovesgenerators.strategies;

import chess.board.Color;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.position.imp.KingCacheBoard;
import chess.board.position.imp.PositionState;
import chess.board.pseudomovesgenerators.MoveGeneratorCastling;


/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractKingMoveGenerator extends AbstractJumpMoveGenerator implements MoveGeneratorCastling{
	
	protected PositionState positionState;
	
	protected KingCacheBoard kingCacheBoard;
	
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
			final Square origen, 
			final PiecePositioned king,
			final PiecePositioned torre,
			final Square casilleroIntermedioRook,
			final Square casilleroDestinoKing, 
			final Square casilleroIntermedioKing) {
		if ( king.getKey().equals(origen) ) {           																//El king se encuentra en su lugar
			if (torre.getValue().equals(piecePlacement.getPieza(torre.getKey()))) {								  		//La torre se encuentra en su lugar
                //El casillero intermedio KING esta vacio
                return piecePlacement.isEmtpy(casilleroIntermedioRook)                                                    //El casillero intermedio ROOK esta vacio
                        && piecePlacement.isEmtpy(casilleroDestinoKing)                                                        //El casillero destino KING esta vacio
                        && piecePlacement.isEmtpy(casilleroIntermedioKing);
			}
		}
		return false;
	}
	
	protected boolean puedeEnroqueKing(
			final Square origen, 
			final PiecePositioned king,
			final PiecePositioned torre,
			final Square casilleroDestinoKing, 
			final Square casilleroIntermedioKing) {
		if ( king.getKey().equals(origen) ) {           																//El king se encuentra en su lugar
			if (torre.getValue().equals(piecePlacement.getPieza(torre.getKey()))) {								  		//La torre se encuentra en su lugar
                //El casillero intermedio KING esta vacio
                return piecePlacement.isEmtpy(casilleroDestinoKing)                                                        //El casillero destino KING esta vacio
                        && piecePlacement.isEmtpy(casilleroIntermedioKing);
			}
		}
		return false;
	}

	public void setBoardState(PositionState positionState) {
		this.positionState = positionState;
	}
	
	public void setKingCacheBoard(KingCacheBoard kingCacheBoard) {
		this.kingCacheBoard = kingCacheBoard;
	}	
	
}
