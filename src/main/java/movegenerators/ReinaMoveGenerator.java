package movegenerators;

import chess.Color;
import iterators.CardinalSquareIterator.Cardinal;

public class ReinaMoveGenerator extends CardinalMoveGenerator{

	public ReinaMoveGenerator(Color color) {
		super(color, new Cardinal[] {Cardinal.NorteEste, Cardinal.SurEste, Cardinal.SurOeste, Cardinal.NorteOeste, Cardinal.Este, Cardinal.Oeste, Cardinal.Norte, Cardinal.Sur});
	}


}
