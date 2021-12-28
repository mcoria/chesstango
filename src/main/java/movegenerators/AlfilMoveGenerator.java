package movegenerators;

import chess.Color;
import chess.PosicionPieza;
import iterators.Cardinal;
import moveexecutors.Move;


/**
 * @author Mauricio Coria
 *
 */
public class AlfilMoveGenerator extends CardinalMoveGenerator {

	public AlfilMoveGenerator(Color color) {
		super(color, new Cardinal[] {Cardinal.NorteEste, Cardinal.SurEste, Cardinal.SurOeste, Cardinal.NorteOeste});
	}

	@Override
	protected Move createSimpleMove(PosicionPieza origen, PosicionPieza destino) {
		return moveFactory.createSimpleMove(origen, destino);
	}
	
	
	@Override
	protected Move createCaptureMove(PosicionPieza origen, PosicionPieza destino) {
		return moveFactory.createCaptureMove(origen, destino); 
	}		
}
