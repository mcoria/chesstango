package chess.pseudomovesgenerators;

import chess.Color;
import chess.PosicionPieza;
import chess.iterators.Cardinal;
import chess.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public class TorreMoveGenerator extends AbstractCardinalMoveGenerator {

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