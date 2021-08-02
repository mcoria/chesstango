package movegenerators;

import chess.Color;
import chess.Move;
import chess.PosicionPieza;
import iterators.Cardinal;
import moveexecutors.EnroqueBlancoReyMove;
import moveexecutors.EnroqueBlancoReynaMove;
import moveexecutors.EnroqueNegroReyMove;
import moveexecutors.EnroqueNegroReynaMove;
import moveexecutors.MoveDecoratorState;

public class TorreMoveGenerator extends CardinalMoveGenerator {

	public TorreMoveGenerator(Color color) {
		super(color, new Cardinal[] {Cardinal.Este, Cardinal.Oeste, Cardinal.Norte, Cardinal.Sur});
	}

	@Override
	protected Move createSimpleMove(PosicionPieza origen, PosicionPieza destino) {
		if (EnroqueBlancoReynaMove.TORRE_FROM.equals(origen)) {
			return new MoveDecoratorState(super.createSimpleMove(origen, destino),
					state -> state.setEnroqueBlancoReinaPermitido(false));
		}
		
		if (EnroqueBlancoReyMove.TORRE_FROM.equals(origen)) {
			return new MoveDecoratorState(super.createSimpleMove(origen, destino),
					state -> state.setEnroqueBlancoReyPermitido(false));
		}
		
		if (EnroqueNegroReynaMove.TORRE_FROM.equals(origen)) {
			return new MoveDecoratorState(super.createSimpleMove(origen, destino),
					state -> state.setEnroqueNegroReinaPermitido(false));
		}
		
		if (EnroqueNegroReyMove.TORRE_FROM.equals(origen)) {
			return new MoveDecoratorState(super.createSimpleMove(origen, destino),
					state -> state.setEnroqueNegroReyPermitido(false));
		}		
		
		return super.createSimpleMove(origen, destino);
	}
}
