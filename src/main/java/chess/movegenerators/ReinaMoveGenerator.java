package chess.movegenerators;

import chess.Color;
import chess.PosicionPieza;
import chess.iterators.Cardinal;
import chess.moveexecutors.Move;

/**
 * @author Mauricio Coria
 *
 */
public class ReinaMoveGenerator extends AbstractCardinalMoveGenerator{

	public ReinaMoveGenerator(Color color) {
		super(color, new Cardinal[] {Cardinal.NorteEste, Cardinal.SurEste, Cardinal.SurOeste, Cardinal.NorteOeste, Cardinal.Este, Cardinal.Oeste, Cardinal.Norte, Cardinal.Sur});
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
