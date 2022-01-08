package chess;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

/**
 * @author Mauricio Coria
 *
 */
public class BoardState {

	private Color turnoActual;
	private Square peonPasanteSquare;
	private boolean enroqueBlancoReinaPermitido;
	private boolean enroqueBlancoKingPermitido;
	private boolean enroqueNegroReinaPermitido;
	private boolean enroqueNegroKingPermitido;
	
	private static class BoardStateNode {
		private Color turnoActual;
		private Square peonPasanteSquare;
		private boolean enroqueBlancoReinaPermitido;
		private boolean enroqueBlancoKingPermitido;
		private boolean enroqueNegroReinaPermitido;
		private boolean enroqueNegroKingPermitido;
	}

	private Deque<BoardStateNode> boardStateNodePila = new ArrayDeque<BoardStateNode>();
	
	public Square getPeonPasanteSquare() {
		return peonPasanteSquare;
	}

	public void setPeonPasanteSquare(Square peonPasanteSquare) {
		this.peonPasanteSquare = peonPasanteSquare;
	}
	
	public boolean isCastlingWhiteReinaPermitido() {
		return enroqueBlancoReinaPermitido;
	}

	public void setCastlingWhiteReinaPermitido(boolean enroqueBlancoReinaPermitido) {
		this.enroqueBlancoReinaPermitido = enroqueBlancoReinaPermitido;
	}	
	
	public boolean isCastlingWhiteKingPermitido() {
		return enroqueBlancoKingPermitido;
	}

	public void setCastlingWhiteKingPermitido(boolean enroqueBlancoKingPermitido) {
		this.enroqueBlancoKingPermitido = enroqueBlancoKingPermitido;
	}
	
	public boolean isCastlingBlackReinaPermitido() {
		return enroqueNegroReinaPermitido;
	}

	public void setCastlingBlackReinaPermitido(boolean enroqueNegroReinaPermitido) {
		this.enroqueNegroReinaPermitido = enroqueNegroReinaPermitido;
	}

	public boolean isCastlingBlackKingPermitido() {
		return enroqueNegroKingPermitido;
	}

	public void setCastlingBlackKingPermitido(boolean enroqueNegroKingPermitido) {
		this.enroqueNegroKingPermitido = enroqueNegroKingPermitido;
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
		node.enroqueBlancoReinaPermitido = enroqueBlancoReinaPermitido;
		node.enroqueBlancoKingPermitido = enroqueBlancoKingPermitido;
		node.enroqueNegroReinaPermitido = enroqueNegroReinaPermitido;
		node.enroqueNegroKingPermitido = enroqueNegroKingPermitido;
		node.turnoActual = turnoActual;
		
		return node;
	}
	
	private void restoreState(BoardStateNode lastState){
		peonPasanteSquare = lastState.peonPasanteSquare;
		enroqueBlancoReinaPermitido = lastState.enroqueBlancoReinaPermitido;
		enroqueBlancoKingPermitido = lastState.enroqueBlancoKingPermitido;
		enroqueNegroReinaPermitido = lastState.enroqueNegroReinaPermitido;
		enroqueNegroKingPermitido = lastState.enroqueNegroKingPermitido;	
		turnoActual = lastState.turnoActual;	
	}

	
	@Override
	public BoardState clone() throws CloneNotSupportedException {
		BoardState clone = new BoardState();
		clone.peonPasanteSquare = peonPasanteSquare;
		clone.enroqueBlancoReinaPermitido = enroqueBlancoReinaPermitido;
		clone.enroqueBlancoKingPermitido = enroqueBlancoKingPermitido;
		clone.enroqueNegroReinaPermitido = enroqueNegroReinaPermitido;
		clone.enroqueNegroKingPermitido = enroqueNegroKingPermitido;
		clone.turnoActual = turnoActual;
		return clone;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BoardState){
			BoardState theInstance = (BoardState) obj;
			return Objects.equals(this.turnoActual, theInstance.turnoActual) && Objects.equals(this.peonPasanteSquare, theInstance.peonPasanteSquare) &&  
					this.enroqueBlancoReinaPermitido == theInstance.enroqueBlancoReinaPermitido &&
					this.enroqueBlancoKingPermitido == theInstance.enroqueBlancoKingPermitido &&
					this.enroqueNegroReinaPermitido == theInstance.enroqueNegroReinaPermitido &&
					this.enroqueNegroKingPermitido == theInstance.enroqueNegroKingPermitido;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "Turno Actual: " + String.format("%-6s", turnoActual.toString()) + ", peonPasanteSquare: " +  (peonPasanteSquare == null ? "- " : peonPasanteSquare.toString()) + 
				", enroqueBlancoReinaPermitido: " + enroqueBlancoReinaPermitido +
				", enroqueBlancoKingPermitido: " + enroqueBlancoKingPermitido +
				", enroqueNegroReinaPermitido: " + enroqueNegroReinaPermitido +
				", enroqueNegroKingPermitido: " + enroqueNegroKingPermitido 
		;
	}
}
