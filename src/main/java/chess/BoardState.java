package chess;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

public class BoardState {
	
	private static class BoardStateNode {
		private Square peonPasanteSquare;
		
		private boolean enroqueBlancoReinaPermitido;
		private boolean enroqueBlancoReyPermitido;
		
		private boolean enroqueNegroReinaPermitido;
		private boolean enroqueNegroReyPermitido;
		
		private Map.Entry<Square, Pieza> captura;		
	}
	
	private BoardStateNode currentNode;
	
	private Color turnoActual;
	
	private Square peonPasanteSquare;
	
	private boolean enroqueBlancoReinaPermitido;
	private boolean enroqueBlancoReyPermitido;
	
	private boolean enroqueNegroReinaPermitido;
	private boolean enroqueNegroReyPermitido;	
	
	private Map.Entry<Square, Pieza> captura;
	
	private Deque<BoardStateNode> boardStateNodePila = new ArrayDeque<BoardStateNode>();
	
	public Square getPeonPasanteSquare() {
		return peonPasanteSquare;
	}

	public void setPeonPasanteSquare(Square peonPasanteSquare) {
		this.peonPasanteSquare = peonPasanteSquare;
	}

	public Map.Entry<Square, Pieza> getCaptura() {
		return captura;
	}

	public void setCaptura(Map.Entry<Square, Pieza> captura) {
		this.captura = captura;
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
		boardStateNodePila.push( this.currentNode );
		
		this.currentNode = null;
	}

	public void popState() {
		BoardStateNode node = boardStateNodePila.pop();
		
		this.currentNode = node;
		
		restoreState();
	}

	public void saveState() {
		BoardStateNode node = new BoardStateNode();
		node.peonPasanteSquare = peonPasanteSquare;
		node.captura = captura;
		node.enroqueBlancoReinaPermitido = enroqueBlancoReinaPermitido;
		node.enroqueBlancoReyPermitido = enroqueBlancoReyPermitido;
		node.enroqueNegroReinaPermitido = enroqueNegroReinaPermitido;
		node.enroqueNegroReyPermitido = enroqueNegroReyPermitido;
		
		this.currentNode = node;
	}
	
	public void restoreState(){
		peonPasanteSquare = this.currentNode.peonPasanteSquare;
		captura = this.currentNode.captura;
		enroqueBlancoReinaPermitido = this.currentNode.enroqueBlancoReinaPermitido;
		enroqueBlancoReyPermitido = this.currentNode.enroqueBlancoReyPermitido;
		enroqueNegroReinaPermitido = this.currentNode.enroqueNegroReinaPermitido;
		enroqueNegroReyPermitido = this.currentNode.enroqueNegroReyPermitido;		
	}

}
