package movegenerators;

import chess.Color;
import iterators.CardinalSquareIterator.Cardinal;

public class TorreMoveGenerator extends CardinalMoveGenerator {
	public TorreMoveGenerator(Color color) {
		super(color, new Cardinal[] {Cardinal.Este, Cardinal.Oeste, Cardinal.Norte, Cardinal.Sur});
	}

}
