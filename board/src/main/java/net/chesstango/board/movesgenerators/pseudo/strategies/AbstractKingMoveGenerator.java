package net.chesstango.board.movesgenerators.pseudo.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.bysquare.bypiece.KingPositionsSquareIterator;
import net.chesstango.board.movesgenerators.pseudo.MoveGeneratorCastling;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.imp.KingCacheBoard;

import java.util.Iterator;


/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractKingMoveGenerator extends AbstractJumpMoveGenerator implements MoveGeneratorCastling {
	
	protected PositionStateReader positionState;
	
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
			if (torre.getPiece().equals(board.getPiece(torre.getSquare()))) {								  		//La torre se encuentra en su lugar
                //El casillero intermedio KING esta vacio
                return board.isEmpty(casilleroIntermedioRook)                                                    //El casillero intermedio ROOK esta vacio
                        && board.isEmpty(casilleroDestinoKing)                                                        //El casillero destino KING esta vacio
                        && board.isEmpty(casilleroIntermedioKing);
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
			if (torre.getPiece().equals(board.getPiece(torre.getSquare()))) {								  		//La torre se encuentra en su lugar
                //El casillero intermedio KING esta vacio
                return board.isEmpty(casilleroDestinoKing)                                                        //El casillero destino KING esta vacio
                        && board.isEmpty(casilleroIntermedioKing);
			}
		}
		return false;
	}

	@Override
	protected Iterator<Square> getSquareIterator(Square fromSquare) {
		return new KingPositionsSquareIterator(fromSquare);
	}

	public void setBoardState(PositionStateReader positionState) {
		this.positionState = positionState;
	}
	

	public void setKingCacheBoard(KingCacheBoard kingCacheBoard) {
		this.kingCacheBoard = kingCacheBoard;
	}	
	
}
