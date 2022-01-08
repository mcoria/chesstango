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
	private boolean enroqueBlancoQueenPermitido;
	private boolean enroqueBlancoKingPermitido;
	private boolean enroqueNegroQueenPermitido;
	private boolean enroqueNegroKingPermitido;
	
	private static class BoardStateNode {
		private Color turnoActual;
		private Square peonPasanteSquare;
		private boolean enroqueBlancoQueenPermitido;
		private boolean enroqueBlancoKingPermitido;
		private boolean enroqueNegroQueenPermitido;
		private boolean enroqueNegroKingPermitido;
	}

	private Deque<BoardStateNode> boardStateNodePila = new ArrayDeque<BoardStateNode>();
	
	public Square getPawnPasanteSquare() {
		return peonPasanteSquare;
	}

	public void setPawnPasanteSquare(Square peonPasanteSquare) {
		this.peonPasanteSquare = peonPasanteSquare;
	}
	
	public boolean isCastlingWhiteQueenPermitido() {
		return enroqueBlancoQueenPermitido;
	}

	public void setCastlingWhiteQueenPermitido(boolean enroqueBlancoQueenPermitido) {
		this.enroqueBlancoQueenPermitido = enroqueBlancoQueenPermitido;
	}	
	
	public boolean isCastlingWhiteKingPermitido() {
		return enroqueBlancoKingPermitido;
	}

	public void setCastlingWhiteKingPermitido(boolean enroqueBlancoKingPermitido) {
		this.enroqueBlancoKingPermitido = enroqueBlancoKingPermitido;
	}
	
	public boolean isCastlingBlackQueenPermitido() {
		return enroqueNegroQueenPermitido;
	}

	public void setCastlingBlackQueenPermitido(boolean enroqueNegroQueenPermitido) {
		this.enroqueNegroQueenPermitido = enroqueNegroQueenPermitido;
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
		node.enroqueBlancoQueenPermitido = enroqueBlancoQueenPermitido;
		node.enroqueBlancoKingPermitido = enroqueBlancoKingPermitido;
		node.enroqueNegroQueenPermitido = enroqueNegroQueenPermitido;
		node.enroqueNegroKingPermitido = enroqueNegroKingPermitido;
		node.turnoActual = turnoActual;
		
		return node;
	}
	
	private void restoreState(BoardStateNode lastState){
		peonPasanteSquare = lastState.peonPasanteSquare;
		enroqueBlancoQueenPermitido = lastState.enroqueBlancoQueenPermitido;
		enroqueBlancoKingPermitido = lastState.enroqueBlancoKingPermitido;
		enroqueNegroQueenPermitido = lastState.enroqueNegroQueenPermitido;
		enroqueNegroKingPermitido = lastState.enroqueNegroKingPermitido;	
		turnoActual = lastState.turnoActual;	
	}

	
	@Override
	public BoardState clone() throws CloneNotSupportedException {
		BoardState clone = new BoardState();
		clone.peonPasanteSquare = peonPasanteSquare;
		clone.enroqueBlancoQueenPermitido = enroqueBlancoQueenPermitido;
		clone.enroqueBlancoKingPermitido = enroqueBlancoKingPermitido;
		clone.enroqueNegroQueenPermitido = enroqueNegroQueenPermitido;
		clone.enroqueNegroKingPermitido = enroqueNegroKingPermitido;
		clone.turnoActual = turnoActual;
		return clone;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BoardState){
			BoardState theInstance = (BoardState) obj;
			return Objects.equals(this.turnoActual, theInstance.turnoActual) && Objects.equals(this.peonPasanteSquare, theInstance.peonPasanteSquare) &&  
					this.enroqueBlancoQueenPermitido == theInstance.enroqueBlancoQueenPermitido &&
					this.enroqueBlancoKingPermitido == theInstance.enroqueBlancoKingPermitido &&
					this.enroqueNegroQueenPermitido == theInstance.enroqueNegroQueenPermitido &&
					this.enroqueNegroKingPermitido == theInstance.enroqueNegroKingPermitido;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "Turno Actual: " + String.format("%-6s", turnoActual.toString()) + ", peonPasanteSquare: " +  (peonPasanteSquare == null ? "- " : peonPasanteSquare.toString()) + 
				", enroqueBlancoQueenPermitido: " + enroqueBlancoQueenPermitido +
				", enroqueBlancoKingPermitido: " + enroqueBlancoKingPermitido +
				", enroqueNegroQueenPermitido: " + enroqueNegroQueenPermitido +
				", enroqueNegroKingPermitido: " + enroqueNegroKingPermitido 
		;
	}
}
