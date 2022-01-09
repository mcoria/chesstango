package chess.position;

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

	private Color turnoActual;
	private Square peonPasanteSquare;
	private boolean enroqueBlancoQueenAllowed;
	private boolean enroqueBlancoKingAllowed;
	private boolean enroqueNegroQueenAllowed;
	private boolean enroqueNegroKingAllowed;
	
	private static class BoardStateNode {
		private Color turnoActual;
		private Square peonPasanteSquare;
		private boolean enroqueBlancoQueenAllowed;
		private boolean enroqueBlancoKingAllowed;
		private boolean enroqueNegroQueenAllowed;
		private boolean enroqueNegroKingAllowed;
	}

	private Deque<BoardStateNode> boardStateNodePila = new ArrayDeque<BoardStateNode>();
	
	public Square getPawnPasanteSquare() {
		return peonPasanteSquare;
	}

	public void setPawnPasanteSquare(Square peonPasanteSquare) {
		this.peonPasanteSquare = peonPasanteSquare;
	}
	
	public boolean isCastlingWhiteQueenAllowed() {
		return enroqueBlancoQueenAllowed;
	}

	public void setCastlingWhiteQueenAllowed(boolean enroqueBlancoQueenAllowed) {
		this.enroqueBlancoQueenAllowed = enroqueBlancoQueenAllowed;
	}	
	
	public boolean isCastlingWhiteKingAllowed() {
		return enroqueBlancoKingAllowed;
	}

	public void setCastlingWhiteKingAllowed(boolean enroqueBlancoKingAllowed) {
		this.enroqueBlancoKingAllowed = enroqueBlancoKingAllowed;
	}
	
	public boolean isCastlingBlackQueenAllowed() {
		return enroqueNegroQueenAllowed;
	}

	public void setCastlingBlackQueenAllowed(boolean enroqueNegroQueenAllowed) {
		this.enroqueNegroQueenAllowed = enroqueNegroQueenAllowed;
	}

	public boolean isCastlingBlackKingAllowed() {
		return enroqueNegroKingAllowed;
	}

	public void setCastlingBlackKingAllowed(boolean enroqueNegroKingAllowed) {
		this.enroqueNegroKingAllowed = enroqueNegroKingAllowed;
	}
	
	public Color getTurnoActual() {
		return turnoActual;
	}

	public void setTurnoActual(Color turnoActual) {
		this.turnoActual = turnoActual;
	}	

	
	public void rollTurno() {
		this.turnoActual = this.turnoActual.opositeColor();
	}
	
	
	public void pushState() {
		BoardStateNode state = saveState();
		
		boardStateNodePila.push( state );
	}

	public void popState() {
		BoardStateNode lastState = boardStateNodePila.pop();
		
		restoreState(lastState);
	}

	private BoardStateNode saveState() {
		BoardStateNode node = new BoardStateNode();
		node.peonPasanteSquare = peonPasanteSquare;
		node.enroqueBlancoQueenAllowed = enroqueBlancoQueenAllowed;
		node.enroqueBlancoKingAllowed = enroqueBlancoKingAllowed;
		node.enroqueNegroQueenAllowed = enroqueNegroQueenAllowed;
		node.enroqueNegroKingAllowed = enroqueNegroKingAllowed;
		node.turnoActual = turnoActual;
		
		return node;
	}
	
	private void restoreState(BoardStateNode lastState){
		peonPasanteSquare = lastState.peonPasanteSquare;
		enroqueBlancoQueenAllowed = lastState.enroqueBlancoQueenAllowed;
		enroqueBlancoKingAllowed = lastState.enroqueBlancoKingAllowed;
		enroqueNegroQueenAllowed = lastState.enroqueNegroQueenAllowed;
		enroqueNegroKingAllowed = lastState.enroqueNegroKingAllowed;	
		turnoActual = lastState.turnoActual;	
	}

	
	@Override
	public PositionState clone() throws CloneNotSupportedException {
		PositionState clone = new PositionState();
		clone.peonPasanteSquare = peonPasanteSquare;
		clone.enroqueBlancoQueenAllowed = enroqueBlancoQueenAllowed;
		clone.enroqueBlancoKingAllowed = enroqueBlancoKingAllowed;
		clone.enroqueNegroQueenAllowed = enroqueNegroQueenAllowed;
		clone.enroqueNegroKingAllowed = enroqueNegroKingAllowed;
		clone.turnoActual = turnoActual;
		return clone;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof PositionState){
			PositionState theInstance = (PositionState) obj;
			return Objects.equals(this.turnoActual, theInstance.turnoActual) && Objects.equals(this.peonPasanteSquare, theInstance.peonPasanteSquare) &&  
					this.enroqueBlancoQueenAllowed == theInstance.enroqueBlancoQueenAllowed &&
					this.enroqueBlancoKingAllowed == theInstance.enroqueBlancoKingAllowed &&
					this.enroqueNegroQueenAllowed == theInstance.enroqueNegroQueenAllowed &&
					this.enroqueNegroKingAllowed == theInstance.enroqueNegroKingAllowed;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "Turno Actual: " + String.format("%-6s", turnoActual.toString()) + ", peonPasanteSquare: " +  (peonPasanteSquare == null ? "- " : peonPasanteSquare.toString()) + 
				", enroqueBlancoQueenAllowed: " + enroqueBlancoQueenAllowed +
				", enroqueBlancoKingAllowed: " + enroqueBlancoKingAllowed +
				", enroqueNegroQueenAllowed: " + enroqueNegroQueenAllowed +
				", enroqueNegroKingAllowed: " + enroqueNegroKingAllowed 
		;
	}
}
