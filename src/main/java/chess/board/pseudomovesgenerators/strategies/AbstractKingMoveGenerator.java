package chess.board.pseudomovesgenerators.strategies;

import chess.board.Color;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.bysquares.bypiece.KingBitSquareIterator;
import chess.board.position.imp.KingCacheBoard;
import chess.board.position.imp.PositionState;
import chess.board.pseudomovesgenerators.MoveGeneratorCastling;

import java.util.Iterator;


/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractKingMoveGenerator extends AbstractJumpMoveGenerator implements MoveGeneratorCastling{
	
	protected PositionState positionState;
	
	protected KingCacheBoard kingCacheBoard;
	
	public AbstractKingMoveGenerator(Color color) {
		super(color);
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

	@Override
	protected Iterator<Square> getSquareIterator(Square fromSquare) {
		return new KingBitSquareIterator(fromSquare);
	}

	public void setBoardState(PositionState positionState) {
		this.positionState = positionState;
	}
	
	public void setKingCacheBoard(KingCacheBoard kingCacheBoard) {
		this.kingCacheBoard = kingCacheBoard;
	}	
	
}
