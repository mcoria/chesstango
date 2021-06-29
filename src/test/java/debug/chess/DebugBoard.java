package debug.chess;

import chess.Board;
import chess.BoardStatus;
import layers.imp.ArrayPosicionPiezaBoard;

public class DebugBoard extends Board {
	
	@Override
	// TODO: el metodo getBoardStatus(): 
	//      								a) no modifica el tablero
	public BoardStatus getBoardStatus() {
		BoardStatus result;

		try {
			ArrayPosicionPiezaBoard boardInicial = ((ArrayPosicionPiezaBoard) super.dummyBoard).clone();

			result = super.getBoardStatus();

			if (!super.dummyBoard.equals(boardInicial)) {
				throw new RuntimeException("El board fué modificado");
			}

		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}

		return result;
	}

}
