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
		
		private Map.Entry<Square, Pieza> from;
		private Map.Entry<Square, Pieza> to;
		private Map.Entry<Square, Pieza> captura;		
	}
	
	private Color turnoActual;
	
	private Square peonPasanteSquare;
	
	private boolean enroqueBlancoReinaPermitido;
	private boolean enroqueBlancoReyPermitido;
	
	private boolean enroqueNegroReinaPermitido;
	private boolean enroqueNegroReyPermitido;	
	
	
	private Map.Entry<Square, Pieza> from;
	private Map.Entry<Square, Pieza> to;
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
	
	public Map.Entry<Square, Pieza> getFrom() {
		return from;
	}

	public void setFrom(Map.Entry<Square, Pieza> from) {
		this.from = from;
	}

	public Map.Entry<Square, Pieza> getTo() {
		return to;
	}

	public void setTo(Map.Entry<Square, Pieza> to) {
		this.to = to;
	}
	
	public void pushState() {
		BoardStateNode node = new BoardStateNode();
		node.from = from;
		node.to = to;
		node.peonPasanteSquare = peonPasanteSquare;
		node.captura = captura;
		node.enroqueBlancoReinaPermitido = enroqueBlancoReinaPermitido;
		node.enroqueBlancoReyPermitido = enroqueBlancoReyPermitido;
		node.enroqueNegroReinaPermitido = enroqueNegroReinaPermitido;
		node.enroqueNegroReyPermitido = enroqueNegroReyPermitido;
		
		boardStateNodePila.push(node);
		
	}

	public void popState() {
		BoardStateNode node = boardStateNodePila.pop();
		
		from = node.from;
		to =  node.to;
		peonPasanteSquare = node.peonPasanteSquare;
		captura = node.captura;
		enroqueBlancoReinaPermitido = node.enroqueBlancoReinaPermitido;
		enroqueBlancoReyPermitido = node.enroqueBlancoReyPermitido;
		enroqueNegroReinaPermitido = node.enroqueNegroReinaPermitido;
		enroqueNegroReyPermitido = node.enroqueNegroReyPermitido;
	}

}
