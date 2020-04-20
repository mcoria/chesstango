package chess;

import java.util.ArrayDeque;
import java.util.Deque;

public class BoardState {
	
	private static class BoardStateNode {
		private Square peonPasanteSquare;
		
		private boolean enroqueBlancoReinaPermitido;
		private boolean enroqueBlancoReyPermitido;
		
		private boolean enroqueNegroReinaPermitido;
		private boolean enroqueNegroReyPermitido;
		
		private Color turnoActual;
	}
	
	private Color turnoActual;
	
	private Square peonPasanteSquare;
	
	private boolean enroqueBlancoReinaPermitido;
	private boolean enroqueBlancoReyPermitido;
	
	private boolean enroqueNegroReinaPermitido;
	private boolean enroqueNegroReyPermitido;
	
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

}
