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
	private boolean enroqueWhiteQueenAllowed;
	private boolean enroqueWhiteKingAllowed;
	private boolean enroqueBlackQueenAllowed;
	private boolean enroqueBlackKingAllowed;

	private Deque<PositionState> boardStateNodePila = new ArrayDeque<PositionState>();
	
	public Square getPawnPasanteSquare() {
		return peonPasanteSquare;
	}

	public void setPawnPasanteSquare(Square peonPasanteSquare) {
		this.peonPasanteSquare = peonPasanteSquare;
	}
	
	public boolean isCastlingWhiteQueenAllowed() {
		return enroqueWhiteQueenAllowed;
	}

	public void setCastlingWhiteQueenAllowed(boolean enroqueWhiteQueenAllowed) {
		this.enroqueWhiteQueenAllowed = enroqueWhiteQueenAllowed;
	}	
	
	public boolean isCastlingWhiteKingAllowed() {
		return enroqueWhiteKingAllowed;
	}

	public void setCastlingWhiteKingAllowed(boolean enroqueWhiteKingAllowed) {
		this.enroqueWhiteKingAllowed = enroqueWhiteKingAllowed;
	}
	
	public boolean isCastlingBlackQueenAllowed() {
		return enroqueBlackQueenAllowed;
	}

	public void setCastlingBlackQueenAllowed(boolean enroqueBlackQueenAllowed) {
		this.enroqueBlackQueenAllowed = enroqueBlackQueenAllowed;
	}

	public boolean isCastlingBlackKingAllowed() {
		return enroqueBlackKingAllowed;
	}

	public void setCastlingBlackKingAllowed(boolean enroqueBlackKingAllowed) {
		this.enroqueBlackKingAllowed = enroqueBlackKingAllowed;
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
		try {
			PositionState state = clone();
			boardStateNodePila.push( state );
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	public void popState() {
		PositionState lastState = boardStateNodePila.pop();
		
		restoreState(lastState);
	}
	
	private void restoreState(PositionState lastState){
		peonPasanteSquare = lastState.peonPasanteSquare;
		enroqueWhiteQueenAllowed = lastState.enroqueWhiteQueenAllowed;
		enroqueWhiteKingAllowed = lastState.enroqueWhiteKingAllowed;
		enroqueBlackQueenAllowed = lastState.enroqueBlackQueenAllowed;
		enroqueBlackKingAllowed = lastState.enroqueBlackKingAllowed;	
		turnoActual = lastState.turnoActual;	
	}

	
	@Override
	public PositionState clone() throws CloneNotSupportedException {
		PositionState clone = new PositionState();
		clone.peonPasanteSquare = peonPasanteSquare;
		clone.enroqueWhiteQueenAllowed = enroqueWhiteQueenAllowed;
		clone.enroqueWhiteKingAllowed = enroqueWhiteKingAllowed;
		clone.enroqueBlackQueenAllowed = enroqueBlackQueenAllowed;
		clone.enroqueBlackKingAllowed = enroqueBlackKingAllowed;
		clone.turnoActual = turnoActual;
		return clone;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof PositionState){
			PositionState theInstance = (PositionState) obj;
			return Objects.equals(this.turnoActual, theInstance.turnoActual) && Objects.equals(this.peonPasanteSquare, theInstance.peonPasanteSquare) &&  
					this.enroqueWhiteQueenAllowed == theInstance.enroqueWhiteQueenAllowed &&
					this.enroqueWhiteKingAllowed == theInstance.enroqueWhiteKingAllowed &&
					this.enroqueBlackQueenAllowed == theInstance.enroqueBlackQueenAllowed &&
					this.enroqueBlackKingAllowed == theInstance.enroqueBlackKingAllowed;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "Turno Actual: " + String.format("%-6s", turnoActual.toString()) + ", peonPasanteSquare: " +  (peonPasanteSquare == null ? "- " : peonPasanteSquare.toString()) + 
				", enroqueWhiteQueenAllowed: " + enroqueWhiteQueenAllowed +
				", enroqueWhiteKingAllowed: " + enroqueWhiteKingAllowed +
				", enroqueBlackQueenAllowed: " + enroqueBlackQueenAllowed +
				", enroqueBlackKingAllowed: " + enroqueBlackKingAllowed 
		;
	}
}
