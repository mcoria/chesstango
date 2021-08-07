package movegenerators;

import chess.Color;
import chess.PosicionPieza;
import iterators.Cardinal;
import moveexecutors.Move;

public class TorreMoveGenerator extends CardinalMoveGenerator {

	public TorreMoveGenerator(Color color) {
		super(color, new Cardinal[] {Cardinal.Este, Cardinal.Oeste, Cardinal.Norte, Cardinal.Sur});
	}

	@Override
	protected Move createSimpleMove(PosicionPieza origen, PosicionPieza destino) {
		return moveFactory.createSimpleTorreMove(origen, destino);
	}
	
	
	@Override
	protected Move createCaptureMove(PosicionPieza origen, PosicionPieza destino) {
		return moveFactory.createCaptureTorreMove(origen, destino); 
	}	
	
	
}
