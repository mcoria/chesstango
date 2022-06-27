/**
 * 
 */
package net.chesstango.board.moves.containers;

import net.chesstango.board.moves.Move;

import java.util.ArrayList;

/**
 * @author Mauricio Coria
 *
 */
public class MoveList extends ArrayList<Move> {

	private static final long serialVersionUID = 1L;


	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer(); 
		for (Move move : this) {
			buffer.append(move.toString() + "\n");
		}
		return buffer.toString();
	}	
}
