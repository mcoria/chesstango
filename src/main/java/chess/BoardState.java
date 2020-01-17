package chess;

import java.util.Map;

public class BoardState {
	private Square peonPasanteSquare;
	private Map.Entry<Square, Pieza> captura;
	
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
	
	public void pushState() {
		// TODO Auto-generated method stub
		
	}

	public void popState() {
		// TODO Auto-generated method stub
		
	}	
}
