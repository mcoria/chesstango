package net.chesstango.board.moves;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.position.ChessPositionWriter;
import net.chesstango.board.position.BoardReader;
import net.chesstango.board.position.BoardWriter;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.imp.ColorBoard;
import net.chesstango.board.position.imp.MoveCacheBoard;
import net.chesstango.board.position.imp.PositionState;
import net.chesstango.board.position.imp.ZobristHash;

//TODO: Y si en vez de PosicionPieza utilizamos Square para To?
//      La mayoria de los movimientos posibles es a bysquare vacios
//      Tiene sentido puesto que las capturas solo son contra piezas contrarias, sin importar que pieza es.
//TODO: y se implementamos un cache de movimientos? Implementar flyweight  pattern

/**
 * @author Mauricio Coria
 *
 */
public interface Move extends Comparable<Move> {
	PiecePositioned getFrom();
	PiecePositioned getTo();

	default void executeMove(ChessPositionWriter chessPosition){
		chessPosition.executeMove(this);
	}
	default void undoMove(ChessPositionWriter chessPosition){
		chessPosition.undoMove(this);
	}
	default boolean filter(MoveFilter filter){
		return filter.filterMove(this);
	}

	void executeMove(BoardWriter board);
	void undoMove(BoardWriter board);

	void executeMove(PositionState positionState);
	void undoMove(PositionState positionState);

	void executeMove(ColorBoard colorBoard);
	void undoMove(ColorBoard colorBoard);

	void executeMove(MoveCacheBoard moveCache);
	void undoMove(MoveCacheBoard moveCache);
	void executeMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState, BoardReader board);
	void undoMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState, BoardReader board);

	Cardinal getMoveDirection();

	@Override
	default int compareTo(Move theOther) {
		//Comparamos from
		if(getFrom().getSquare().getRank() > theOther.getFrom().getSquare().getRank()){
			return 1;
		} else if (getFrom().getSquare().getRank() < theOther.getFrom().getSquare().getRank()){
			return -1;
		}


		if(getFrom().getSquare().getFile() <  theOther.getFrom().getSquare().getFile()){
			return 1;
		} else if(getFrom().getSquare().getFile() >  theOther.getFrom().getSquare().getFile()){
			return -1;
		}

		//---------------
		//Son iguales asi que comparamos to
		if(getTo().getSquare().getRank() < theOther.getTo().getSquare().getRank()){
			return 1;
		} else if (getTo().getSquare().getRank() > theOther.getTo().getSquare().getRank()){
			return -1;
		}


		if(getTo().getSquare().getFile() <  theOther.getTo().getSquare().getFile()){
			return -1;
		} else if(getTo().getSquare().getFile() >  theOther.getTo().getSquare().getFile()){
			return 1;
		}

		//--------------- Desde y hasta coinciden, que hacemos ?

		return 0;
	}
}