package chess.position.imp;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

import chess.Color;
import chess.Square;

/**
 * @author Mauricio Coria
 *
 */
public class PositionState {

	private static class PositionStateData{
		private Color turnoActual;
		private Square pawnPasanteSquare;
		private boolean enroqueWhiteQueenAllowed;
		private boolean enroqueWhiteKingAllowed;
		private boolean enroqueBlackQueenAllowed;
		private boolean enroqueBlackKingAllowed;
	}

	private PositionStateData dataNode = new PositionStateData();
	
	private Deque<PositionStateData> boardStateNodePila = new ArrayDeque<PositionStateData>();
	
	public Square getEnPassantSquare() {
		return dataNode.pawnPasanteSquare;
	}

	public void setEnPassantSquare(Square pawnPasanteSquare) {
		dataNode.pawnPasanteSquare = pawnPasanteSquare;
	}
	
	public boolean isCastlingWhiteQueenAllowed() {
		return dataNode.enroqueWhiteQueenAllowed;
	}

	public void setCastlingWhiteQueenAllowed(boolean enroqueWhiteQueenAllowed) {
		dataNode.enroqueWhiteQueenAllowed = enroqueWhiteQueenAllowed;
	}	
	
	public boolean isCastlingWhiteKingAllowed() {
		return dataNode.enroqueWhiteKingAllowed;
	}

	public void setCastlingWhiteKingAllowed(boolean enroqueWhiteKingAllowed) {
		dataNode.enroqueWhiteKingAllowed = enroqueWhiteKingAllowed;
	}
	
	public boolean isCastlingBlackQueenAllowed() {
		return dataNode.enroqueBlackQueenAllowed;
	}

	public void setCastlingBlackQueenAllowed(boolean enroqueBlackQueenAllowed) {
		dataNode.enroqueBlackQueenAllowed = enroqueBlackQueenAllowed;
	}

	public boolean isCastlingBlackKingAllowed() {
		return dataNode.enroqueBlackKingAllowed;
	}

	public void setCastlingBlackKingAllowed(boolean enroqueBlackKingAllowed) {
		dataNode.enroqueBlackKingAllowed = enroqueBlackKingAllowed;
	}
	
	public Color getTurnoActual() {
		return dataNode.turnoActual;
	}

	public void setTurnoActual(Color turnoActual) {
		dataNode.turnoActual = turnoActual;
	}	

	
	public void rollTurno() {
		dataNode.turnoActual = dataNode.turnoActual.opositeColor();
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
		node.pawnPasanteSquare = dataNode.pawnPasanteSquare;
		node.enroqueWhiteQueenAllowed = dataNode.enroqueWhiteQueenAllowed;
		node.enroqueWhiteKingAllowed = dataNode.enroqueWhiteKingAllowed;
		node.enroqueBlackQueenAllowed = dataNode.enroqueBlackQueenAllowed;
		node.enroqueBlackKingAllowed = dataNode.enroqueBlackKingAllowed;
		node.turnoActual = dataNode.turnoActual;
		
		return node;
	}	
	
	private void restoreState(PositionStateData lastState){
		dataNode.pawnPasanteSquare = lastState.pawnPasanteSquare;
		dataNode.enroqueWhiteQueenAllowed = lastState.enroqueWhiteQueenAllowed;
		dataNode.enroqueWhiteKingAllowed = lastState.enroqueWhiteKingAllowed;
		dataNode.enroqueBlackQueenAllowed = lastState.enroqueBlackQueenAllowed;
		dataNode.enroqueBlackKingAllowed = lastState.enroqueBlackKingAllowed;	
		dataNode.turnoActual = lastState.turnoActual;	
	}

	
	@Override
	public PositionState clone() throws CloneNotSupportedException {
		PositionState clone = new PositionState();
		clone.dataNode.pawnPasanteSquare = dataNode.pawnPasanteSquare;
		clone.dataNode.enroqueWhiteQueenAllowed = dataNode.enroqueWhiteQueenAllowed;
		clone.dataNode.enroqueWhiteKingAllowed = dataNode.enroqueWhiteKingAllowed;
		clone.dataNode.enroqueBlackQueenAllowed = dataNode.enroqueBlackQueenAllowed;
		clone.dataNode.enroqueBlackKingAllowed = dataNode.enroqueBlackKingAllowed;
		clone.dataNode.turnoActual = dataNode.turnoActual;
		return clone;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof PositionState){
			PositionState theInstance = (PositionState) obj;
			return Objects.equals(dataNode.turnoActual, theInstance.dataNode.turnoActual) && Objects.equals(dataNode.pawnPasanteSquare, theInstance.dataNode.pawnPasanteSquare) &&  
					dataNode.enroqueWhiteQueenAllowed == theInstance.dataNode.enroqueWhiteQueenAllowed &&
					dataNode.enroqueWhiteKingAllowed == theInstance.dataNode.enroqueWhiteKingAllowed &&
					dataNode.enroqueBlackQueenAllowed == theInstance.dataNode.enroqueBlackQueenAllowed &&
					dataNode.enroqueBlackKingAllowed == theInstance.dataNode.enroqueBlackKingAllowed;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "Turno Actual: " + String.format("%-6s", dataNode.turnoActual.toString()) + ", pawnPasanteSquare: " +  (dataNode.pawnPasanteSquare == null ? "- " : dataNode.pawnPasanteSquare.toString()) + 
				", enroqueWhiteQueenAllowed: " + dataNode.enroqueWhiteQueenAllowed +
				", enroqueWhiteKingAllowed: " + dataNode.enroqueWhiteKingAllowed +
				", enroqueBlackQueenAllowed: " + dataNode.enroqueBlackQueenAllowed +
				", enroqueBlackKingAllowed: " + dataNode.enroqueBlackKingAllowed 
		;
	}
}
