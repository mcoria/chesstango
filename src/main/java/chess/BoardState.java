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
	
	private BoardStateNode currentBoardState = new BoardStateNode();
	
	private Deque<BoardStateNode> boardStateNodePila = new ArrayDeque<BoardStateNode>();
	
	public Square getPeonPasanteSquare() {
		return currentBoardState.peonPasanteSquare;
	}

	public void setPeonPasanteSquare(Square peonPasanteSquare) {
		this.currentBoardState.peonPasanteSquare = peonPasanteSquare;
	}
	
	public boolean isEnroqueBlancoReinaPermitido() {
		return currentBoardState.enroqueBlancoReinaPermitido;
	}

	public void setEnroqueBlancoReinaPermitido(boolean enroqueBlancoReinaPermitido) {
		this.currentBoardState.enroqueBlancoReinaPermitido = enroqueBlancoReinaPermitido;
	}	
	
	public boolean isEnroqueBlancoReyPermitido() {
		return currentBoardState.enroqueBlancoReyPermitido;
	}

	public void setEnroqueBlancoReyPermitido(boolean enroqueBlancoReyPermitido) {
		this.currentBoardState.enroqueBlancoReyPermitido = enroqueBlancoReyPermitido;
	}
	
	public boolean isEnroqueNegroReinaPermitido() {
		return currentBoardState.enroqueNegroReinaPermitido;
	}

	public void setEnroqueNegroReinaPermitido(boolean enroqueNegroReinaPermitido) {
		this.currentBoardState.enroqueNegroReinaPermitido = enroqueNegroReinaPermitido;
	}

	public boolean isEnroqueNegroReyPermitido() {
		return currentBoardState.enroqueNegroReyPermitido;
	}

	public void setEnroqueNegroReyPermitido(boolean enroqueNegroReyPermitido) {
		this.currentBoardState.enroqueNegroReyPermitido = enroqueNegroReyPermitido;
	}
	
	public Color getTurnoActual() {
		return currentBoardState.turnoActual;
	}

	public void setTurnoActual(Color turnoActual) {
		this.currentBoardState.turnoActual = turnoActual;
	}	

	
	public void rollTurno() {
		this.currentBoardState.turnoActual = this.currentBoardState.turnoActual.opositeColor();
	}
	
	
	public void pushState() {		
		boardStateNodePila.push( currentBoardState );
		
		BoardStateNode newState = new BoardStateNode();
		newState.peonPasanteSquare = currentBoardState.peonPasanteSquare;
		newState.enroqueBlancoReinaPermitido = currentBoardState.enroqueBlancoReinaPermitido;
		newState.enroqueBlancoReyPermitido = currentBoardState.enroqueBlancoReyPermitido;
		newState.enroqueNegroReinaPermitido = currentBoardState.enroqueNegroReinaPermitido;
		newState.enroqueNegroReyPermitido = currentBoardState.enroqueNegroReyPermitido;
		newState.turnoActual = currentBoardState.turnoActual;
		
		currentBoardState = newState;
	}

	public void popState() {
		currentBoardState = boardStateNodePila.pop();
	}

}
