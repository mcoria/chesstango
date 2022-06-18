package chess.board.position.imp;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

import chess.board.Color;
import chess.board.Square;

/**
 * @author Mauricio Coria
 *
 */
public class PositionState {

	private static class PositionStateData{
		private Color currentTurn;
		private Square enPassantSquare;
		private boolean castlingWhiteQueenAllowed;
		private boolean castlingWhiteKingAllowed;
		private boolean castlingBlackQueenAllowed;
		private boolean castlingBlackKingAllowed;
		private int halfMoveClock;
		private int fullMoveClock;
	}

	private final PositionStateData dataNode = new PositionStateData();
	
	private final Deque<PositionStateData> boardStateNodePila = new ArrayDeque<PositionStateData>();
	
	public Square getEnPassantSquare() {
		return dataNode.enPassantSquare;
	}
	public void setEnPassantSquare(Square enPassantSquare) {
		dataNode.enPassantSquare = enPassantSquare;
	}
	
	public boolean isCastlingWhiteQueenAllowed() {
		return dataNode.castlingWhiteQueenAllowed;
	}

	public void setCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
		dataNode.castlingWhiteQueenAllowed = castlingWhiteQueenAllowed;
	}
	
	public boolean isCastlingWhiteKingAllowed() {
		return dataNode.castlingWhiteKingAllowed;
	}

	public void setCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
		dataNode.castlingWhiteKingAllowed = castlingWhiteKingAllowed;
	}
	
	public boolean isCastlingBlackQueenAllowed() {
		return dataNode.castlingBlackQueenAllowed;
	}

	public void setCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
		dataNode.castlingBlackQueenAllowed = castlingBlackQueenAllowed;
	}

	public boolean isCastlingBlackKingAllowed() {
		return dataNode.castlingBlackKingAllowed;
	}

	public void setCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
		dataNode.castlingBlackKingAllowed = castlingBlackKingAllowed;
	}
	
	public Color getCurrentTurn() {
		return dataNode.currentTurn;
	}

	public void setCurrentTurn(Color turn) {
		dataNode.currentTurn = turn;
	}
	public void rollTurn() {
		dataNode.currentTurn = dataNode.currentTurn.oppositeColor();
	}

	public int getHalfMoveClock() {
		return dataNode.halfMoveClock;
	}

	public void setHalfMoveClock(int halfMoveClock) {
		this.dataNode.halfMoveClock = halfMoveClock;
	}

	public int getFullMoveClock() {
		return dataNode.fullMoveClock;
	}

	public void setFullMoveClock(int fullMoveClock) {
		this.dataNode.fullMoveClock = fullMoveClock;
	}


	public void pushState() {
		PositionStateData state = saveState();
		boardStateNodePila.push( state );
	}

	public void popState() {
		PositionStateData lastState = boardStateNodePila.pop();
		
		restoreState(lastState);
	}
	
	private PositionStateData saveState() {
		PositionStateData node = new PositionStateData();
		node.enPassantSquare = dataNode.enPassantSquare;
		node.castlingWhiteQueenAllowed = dataNode.castlingWhiteQueenAllowed;
		node.castlingWhiteKingAllowed = dataNode.castlingWhiteKingAllowed;
		node.castlingBlackQueenAllowed = dataNode.castlingBlackQueenAllowed;
		node.castlingBlackKingAllowed = dataNode.castlingBlackKingAllowed;
		node.currentTurn = dataNode.currentTurn;
		
		return node;
	}	
	
	private void restoreState(PositionStateData lastState){
		dataNode.enPassantSquare = lastState.enPassantSquare;
		dataNode.castlingWhiteQueenAllowed = lastState.castlingWhiteQueenAllowed;
		dataNode.castlingWhiteKingAllowed = lastState.castlingWhiteKingAllowed;
		dataNode.castlingBlackQueenAllowed = lastState.castlingBlackQueenAllowed;
		dataNode.castlingBlackKingAllowed = lastState.castlingBlackKingAllowed;	
		dataNode.currentTurn = lastState.currentTurn;
	}

	
	@Override
	public PositionState clone() throws CloneNotSupportedException {
		PositionState clone = new PositionState();
		clone.dataNode.enPassantSquare = dataNode.enPassantSquare;
		clone.dataNode.castlingWhiteQueenAllowed = dataNode.castlingWhiteQueenAllowed;
		clone.dataNode.castlingWhiteKingAllowed = dataNode.castlingWhiteKingAllowed;
		clone.dataNode.castlingBlackQueenAllowed = dataNode.castlingBlackQueenAllowed;
		clone.dataNode.castlingBlackKingAllowed = dataNode.castlingBlackKingAllowed;
		clone.dataNode.currentTurn = dataNode.currentTurn;
		return clone;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof PositionState){
			PositionState theInstance = (PositionState) obj;
			return Objects.equals(dataNode.currentTurn, theInstance.dataNode.currentTurn) && Objects.equals(dataNode.enPassantSquare, theInstance.dataNode.enPassantSquare) &&
					dataNode.castlingWhiteQueenAllowed == theInstance.dataNode.castlingWhiteQueenAllowed &&
					dataNode.castlingWhiteKingAllowed == theInstance.dataNode.castlingWhiteKingAllowed &&
					dataNode.castlingBlackQueenAllowed == theInstance.dataNode.castlingBlackQueenAllowed &&
					dataNode.castlingBlackKingAllowed == theInstance.dataNode.castlingBlackKingAllowed;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "Turno Actual: " + String.format("%-6s", dataNode.currentTurn.toString()) + ", pawnPasanteSquare: " +  (dataNode.enPassantSquare == null ? "- " : dataNode.enPassantSquare.toString()) +
				", castlingWhiteQueenAllowed: " + dataNode.castlingWhiteQueenAllowed +
				", castlingWhiteKingAllowed: " + dataNode.castlingWhiteKingAllowed +
				", castlingBlackQueenAllowed: " + dataNode.castlingBlackQueenAllowed +
				", castlingBlackKingAllowed: " + dataNode.castlingBlackKingAllowed 
		;
	}
}
