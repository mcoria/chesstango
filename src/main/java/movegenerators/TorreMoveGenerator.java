package movegenerators;

import chess.Color;
import chess.PosicionPieza;
import chess.Square;
import iterators.Cardinal;
import moveexecutors.Move;
import moveexecutors.MoveDecoratorState;

public class TorreMoveGenerator extends CardinalMoveGenerator {

	public TorreMoveGenerator(Color color) {
		super(color, new Cardinal[] {Cardinal.Este, Cardinal.Oeste, Cardinal.Norte, Cardinal.Sur});
	}

	@Override
	protected Move createSimpleMove(PosicionPieza origen, PosicionPieza destino) {
		if (Square.a1.equals(origen.getKey())) {
			return new MoveDecoratorState(super.createSimpleMove(origen, destino),
					state -> state.setEnroqueBlancoReinaPermitido(false));
		}
		
		if (Square.h1.equals(origen.getKey())) {
			return new MoveDecoratorState(super.createSimpleMove(origen, destino),
					state -> state.setEnroqueBlancoReyPermitido(false));
		}
		
		if (Square.a8.equals(origen.getKey())) {
			return new MoveDecoratorState(super.createSimpleMove(origen, destino),
					state -> state.setEnroqueNegroReinaPermitido(false));
		}
		
		if (Square.h8.equals(origen.getKey())) {
			return new MoveDecoratorState(super.createSimpleMove(origen, destino),
					state -> state.setEnroqueNegroReyPermitido(false));
		}		
		
		return super.createSimpleMove(origen, destino);
	}
	
	
	@Override
	protected Move createCaptureMove(PosicionPieza origen, PosicionPieza destino) {
		if (Square.a1.equals(origen.getKey())) {
			return new MoveDecoratorState(super.createCaptureMove(origen, destino),
					state -> state.setEnroqueBlancoReinaPermitido(false));
		}
		
		if (Square.h1.equals(origen.getKey())) {
			return new MoveDecoratorState(super.createCaptureMove(origen, destino),
					state -> state.setEnroqueBlancoReyPermitido(false));
		}
		
		if (Square.a8.equals(origen.getKey())) {
			return new MoveDecoratorState(super.createCaptureMove(origen, destino),
					state -> state.setEnroqueNegroReinaPermitido(false));
		}
		
		if (Square.h8.equals(origen.getKey())) {
			return new MoveDecoratorState(super.createCaptureMove(origen, destino),
					state -> state.setEnroqueNegroReyPermitido(false));
		}		
		return super.createCaptureMove(origen, destino);
	}	
	
	
}
