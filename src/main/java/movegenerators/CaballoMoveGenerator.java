package movegenerators;

import chess.Color;
import iterators.SaltoSquareIterator;

public class CaballoMoveGenerator extends SaltoMoveGenerator {
	
	public CaballoMoveGenerator(Color color) {
		super(color, SaltoSquareIterator.SALTOS_CABALLO);
	}

}
