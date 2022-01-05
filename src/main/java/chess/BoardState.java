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
	private boolean enroqueBlancoReyPermitido;
	private boolean enroqueNegroReinaPermitido;
	private boolean enroqueNegroReyPermitido;
	
	private static class BoardStateNode {
		private Color turnoActual;
		private Square peonPasanteSquare;
		private boolean enroqueBlancoReinaPermitido;
		private boolean enroqueBlancoReyPermitido;
		private boolean enroqueNegroReinaPermitido;
		private boolean enroqueNegroReyPermitido;
	}

	private Deque<BoardStateNode> boardStateNodePila = new ArrayDeque<BoardStateNode>();
	
	public Square getPeonPasanteSquare() {
		return peonPasanteSquare;
	}

	public void setPeonPasanteSquare(Square peonPasanteSquare) {
		this.peonPasanteSquare = peonPasanteSquare;
	}
	
	public boolean isEnroqueBlancoReinaPermitido() {
		return enroqueBlancoReinaPermitido;
	}

	public void setEnroqueBlancoReinaPermitido(boolean enroqueBlancoReinaPermitido) {
		this.enroqueBlancoReinaPermitido = enroqueBlancoReinaPermitido;
	}	
	
	public boolean isEnroqueBlancoReyPermitido() {
		return enroqueBlancoReyPermitido;
	}

	public void setEnroqueBlancoReyPermitido(boolean enroqueBlancoReyPermitido) {
		this.enroqueBlancoReyPermitido = enroqueBlancoReyPermitido;
	}
	
	public boolean isEnroqueNegroReinaPermitido() {
		return enroqueNegroReinaPermitido;
	}

	public void setEnroqueNegroReinaPermitido(boolean enroqueNegroReinaPermitido) {
		this.enroqueNegroReinaPermitido = enroqueNegroReinaPermitido;
	}

	public boolean isEnroqueNegroReyPermitido() {
		return enroqueNegroReyPermitido;
	}

	public void setEnroqueNegroReyPermitido(boolean enroqueNegroReyPermitido) {
		this.enroqueNegroReyPermitido = enroqueNegroReyPermitido;
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
		node.enroqueBlancoReyPermitido = enroqueBlancoReyPermitido;
		node.enroqueNegroReinaPermitido = enroqueNegroReinaPermitido;
		node.enroqueNegroReyPermitido = enroqueNegroReyPermitido;
		node.turnoActual = turnoActual;
		
		return node;
	}
	
	private void restoreState(BoardStateNode lastState){
		peonPasanteSquare = lastState.peonPasanteSquare;
		enroqueBlancoReinaPermitido = lastState.enroqueBlancoReinaPermitido;
		enroqueBlancoReyPermitido = lastState.enroqueBlancoReyPermitido;
		enroqueNegroReinaPermitido = lastState.enroqueNegroReinaPermitido;
		enroqueNegroReyPermitido = lastState.enroqueNegroReyPermitido;	
		turnoActual = lastState.turnoActual;	
	}

	
	@Override
	public BoardState clone() throws CloneNotSupportedException {
		BoardState clone = new BoardState();
		clone.peonPasanteSquare = peonPasanteSquare;
		clone.enroqueBlancoReinaPermitido = enroqueBlancoReinaPermitido;
		clone.enroqueBlancoReyPermitido = enroqueBlancoReyPermitido;
		clone.enroqueNegroReinaPermitido = enroqueNegroReinaPermitido;
		clone.enroqueNegroReyPermitido = enroqueNegroReyPermitido;
		clone.turnoActual = turnoActual;
		return clone;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BoardState){
			BoardState theInstance = (BoardState) obj;
			return Objects.equals(this.turnoActual, theInstance.turnoActual) && Objects.equals(this.peonPasanteSquare, theInstance.peonPasanteSquare) &&  
					this.enroqueBlancoReinaPermitido == theInstance.enroqueBlancoReinaPermitido &&
					this.enroqueBlancoReyPermitido == theInstance.enroqueBlancoReyPermitido &&
					this.enroqueNegroReinaPermitido == theInstance.enroqueNegroReinaPermitido &&
					this.enroqueNegroReyPermitido == theInstance.enroqueNegroReyPermitido;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "Turno Actual: " + String.format("%-6s", turnoActual.toString()) + ", peonPasanteSquare: " +  (peonPasanteSquare == null ? "- " : peonPasanteSquare.toString()) + 
				", enroqueBlancoReinaPermitido: " + enroqueBlancoReinaPermitido +
				", enroqueBlancoReyPermitido: " + enroqueBlancoReyPermitido +
				", enroqueNegroReinaPermitido: " + enroqueNegroReinaPermitido +
				", enroqueNegroReyPermitido: " + enroqueNegroReyPermitido 
		;
	}
}
