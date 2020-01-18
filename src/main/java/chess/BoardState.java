package chess;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Optional;

public class BoardState {
	private Square peonPasanteSquare;
	private Map.Entry<Square, Pieza> captura;
	
	private Deque<Optional<Square>> peonPasanteSquarePila = new ArrayDeque<Optional<Square>>();
	private Deque<Optional<Map.Entry<Square, Pieza>>> capturadasPila = new ArrayDeque<Optional<Map.Entry<Square, Pieza>>> ();
	
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
		Optional<Square> square = Optional.ofNullable(peonPasanteSquare);
		Optional<Map.Entry<Square, Pieza>> source = Optional.ofNullable(captura);
		peonPasanteSquarePila.push(square);
		capturadasPila.push(source);
	}

	public void popState() {
		Optional<Square> square = peonPasanteSquarePila.pop();
		Optional<Map.Entry<Square, Pieza>> source = capturadasPila.pop();
		peonPasanteSquare = square.isPresent() ? square.get() : null;
		captura = source.isPresent() ? source.get() : null;
	}	
}
