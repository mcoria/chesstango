package chess;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Optional;

public class BoardState {
	private Color turnoActual;
	
	private Square peonPasanteSquare;
	private Map.Entry<Square, Pieza> captura;	
	
	private boolean enroqueBlancoReinaPermitido;
	private boolean enroqueBlancoReyPermitido;
	
	private boolean enroqueNegroReinaPermitido;
	private boolean enroqueNegroReyPermitido;	
	
	private Deque<Optional<Square>> peonPasanteSquarePila = new ArrayDeque<Optional<Square>>();
	private Deque<Optional<Map.Entry<Square, Pieza>>> capturadasPila = new ArrayDeque<Optional<Map.Entry<Square, Pieza>>> ();
	
	private Deque<Boolean> enroqueBlancoReinaPermitidoPila = new ArrayDeque<Boolean>();
	private Deque<Boolean> enroqueBlancoReyPermitidoPila = new ArrayDeque<Boolean>();
	private Deque<Boolean> enroqueNegroReinaPermitidoPila = new ArrayDeque<Boolean>();
	private Deque<Boolean> enroqueNegroReyPermitidoPila = new ArrayDeque<Boolean>();
	
	
	
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
		Optional<Square> square = Optional.ofNullable(peonPasanteSquare);
		Optional<Map.Entry<Square, Pieza>> source = Optional.ofNullable(captura);
		peonPasanteSquarePila.push(square);
		capturadasPila.push(source);
		enroqueBlancoReinaPermitidoPila.push(enroqueBlancoReinaPermitido);
		enroqueBlancoReyPermitidoPila.push(enroqueBlancoReyPermitido);
		enroqueNegroReinaPermitidoPila.push(enroqueNegroReinaPermitido);
		enroqueNegroReyPermitidoPila.push(enroqueNegroReyPermitido);
	}

	public void popState() {
		Optional<Square> square = peonPasanteSquarePila.pop();
		Optional<Map.Entry<Square, Pieza>> source = capturadasPila.pop();
		peonPasanteSquare = square.isPresent() ? square.get() : null;
		captura = source.isPresent() ? source.get() : null;
		
		enroqueBlancoReinaPermitido = enroqueBlancoReinaPermitidoPila.pop();
		enroqueBlancoReyPermitido = enroqueBlancoReyPermitidoPila.pop();
		enroqueNegroReinaPermitido = enroqueNegroReinaPermitidoPila.pop();
		enroqueNegroReyPermitido = enroqueNegroReyPermitidoPila.pop();
	}

}
