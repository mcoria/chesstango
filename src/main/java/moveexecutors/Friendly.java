package moveexecutors;

import chess.Move;

interface Friendly extends Comparable<Move> {

	String getType();
}
