package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MoveCache {
	protected List<Collection<Move>> tablero = new ArrayList<Collection<Move>>();
	
	public MoveCache() {
		for (int i = 0; i < 64; i++) {
			tablero.add(createMoveContainer());
		}
	}
	

	public Collection<Move> getMoveContainer(Square key) {
		return tablero.get(key.ordinal());
	}
	
	private static Collection<Move> createMoveContainer(){
		return new ArrayList<Move>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2237718042714336104L;

			@Override
			public String toString() {
				StringBuffer buffer = new StringBuffer(); 
				for (Move move : this) {
					buffer.append(move.toString() + "\n");
				}
				return buffer.toString();
			}
		};
	}	
}
