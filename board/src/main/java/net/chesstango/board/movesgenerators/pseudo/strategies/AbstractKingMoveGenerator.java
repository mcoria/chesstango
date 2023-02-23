package net.chesstango.board.movesgenerators.pseudo.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.bysquare.bypiece.KingPositionsSquareIterator;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorCastling;
import net.chesstango.board.position.imp.KingCacheBoard;
import net.chesstango.board.position.imp.PositionState;

import java.util.Iterator;


/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractKingMoveGenerator extends AbstractJumpMoveGenerator implements MoveGeneratorCastling {
	
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
		if ( king.getSquare().equals(origen) ) {           																//El king se encuentra en su lugar
			if (torre.getPiece().equals(piecePlacement.getPiece(torre.getSquare()))) {								  		//La torre se encuentra en su lugar
                //El casillero intermedio KING esta vacio
                return piecePlacement.isEmpty(casilleroIntermedioRook)                                                    //El casillero intermedio ROOK esta vacio
                        && piecePlacement.isEmpty(casilleroDestinoKing)                                                        //El casillero destino KING esta vacio
                        && piecePlacement.isEmpty(casilleroIntermedioKing);
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
		if ( king.getSquare().equals(origen) ) {           																//El king se encuentra en su lugar
			if (torre.getPiece().equals(piecePlacement.getPiece(torre.getSquare()))) {								  		//La torre se encuentra en su lugar
                //El casillero intermedio KING esta vacio
                return piecePlacement.isEmpty(casilleroDestinoKing)                                                        //El casillero destino KING esta vacio
                        && piecePlacement.isEmpty(casilleroIntermedioKing);
			}
		}
		return false;
	}

	@Override
	protected Iterator<Square> getSquareIterator(Square fromSquare) {
		return new KingPositionsSquareIterator(fromSquare);
	}

	public void setBoardState(PositionState positionState) {
		this.positionState = positionState;
	}
	
	public void setKingCacheBoard(KingCacheBoard kingCacheBoard) {
		this.kingCacheBoard = kingCacheBoard;
	}	
	
}
