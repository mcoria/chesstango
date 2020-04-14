package chess;

import java.util.AbstractMap.SimpleImmutableEntry;

public class PosicionPieza extends SimpleImmutableEntry<Square, Pieza> {

	public PosicionPieza(Square key, Pieza value) {
		super(key, value);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
