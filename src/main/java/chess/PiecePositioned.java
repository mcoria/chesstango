package chess;

import java.util.AbstractMap.SimpleImmutableEntry;

/**
 * @author Mauricio Coria
 *
 */
public class PiecePositioned extends SimpleImmutableEntry<Square, Piece> {

	public PiecePositioned(Square key, Piece value) {
		super(key, value);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
