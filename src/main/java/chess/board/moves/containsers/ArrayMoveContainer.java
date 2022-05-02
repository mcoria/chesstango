/**
 * 
 */
package chess.board.moves.containsers;

import chess.board.moves.Move;

import java.util.ArrayList;

/**
 * @author Mauricio Coria
 *
 */
public class ArrayMoveContainer<T extends Move> extends ArrayList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	public ArrayMoveContainer() {
		super();
	}


	public ArrayMoveContainer(int initialCapacity) {
		super(initialCapacity);
	}



	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer(); 
		for (Move move : this) {
			buffer.append(move.toString() + "\n");
		}
		return buffer.toString();
	}	
}
