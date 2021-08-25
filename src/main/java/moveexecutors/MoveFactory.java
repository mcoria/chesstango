package moveexecutors;

import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;

public class MoveFactory {
	
	public Move createSimpleMove(PosicionPieza origen, PosicionPieza destino){
		return new SimpleMove(origen, destino);
	}
	
	public Move createCaptureMove(PosicionPieza origen, PosicionPieza destino) {
		return new CaptureMove(origen, destino);
	}		
	
	public Move createSimpleReyMoveBlanco(PosicionPieza origen, PosicionPieza destino) {
		if(Square.e1.equals(origen.getKey())){
			return new MoveDecoratorReyState(new SimpleReyMove(origen, destino), state -> {
				state.setEnroqueBlancoReinaPermitido(false);
				state.setEnroqueBlancoReyPermitido(false);				
			});
		}
		return new SimpleReyMove(origen, destino);
	}


	public Move createCaptureReyMoveBlanco(PosicionPieza origen, PosicionPieza destino) {
		if(Square.e1.equals(origen.getKey())){
			return new MoveDecoratorReyState(new CaptureReyMove(origen, destino), state -> {
				state.setEnroqueBlancoReinaPermitido(false);
				state.setEnroqueBlancoReyPermitido(false);				
			});
		}
		return new CaptureReyMove(origen, destino);
	}
	

	public Move createSimpleReyMoveNegro(PosicionPieza origen, PosicionPieza destino) {
		if(Square.e8.equals(origen.getKey())){
			return new MoveDecoratorReyState(new SimpleReyMove(origen, destino), state -> {
				state.setEnroqueNegroReinaPermitido(false);
				state.setEnroqueNegroReyPermitido(false);			
			});
		}
		return new SimpleReyMove(origen, destino);
	}

	public Move createCaptureReyMoveNegro(PosicionPieza origen, PosicionPieza destino) {
		if(Square.e8.equals(origen.getKey())){
			return new MoveDecoratorReyState(new CaptureReyMove(origen, destino), state -> {
				state.setEnroqueNegroReinaPermitido(false);
				state.setEnroqueNegroReyPermitido(false);			
			});
		}
		return new CaptureReyMove(origen, destino);
	}
	
	
	public Move createSimpleTorreMove(PosicionPieza origen, PosicionPieza destino) {
		if (Square.a1.equals(origen.getKey())) {
			return new MoveDecoratorState(createSimpleMove(origen, destino),
					state -> state.setEnroqueBlancoReinaPermitido(false));
		}
		
		if (Square.h1.equals(origen.getKey())) {
			return new MoveDecoratorState(createSimpleMove(origen, destino),
					state -> state.setEnroqueBlancoReyPermitido(false));
		}
		
		if (Square.a8.equals(origen.getKey())) {
			return new MoveDecoratorState(createSimpleMove(origen, destino),
					state -> state.setEnroqueNegroReinaPermitido(false));
		}
		
		if (Square.h8.equals(origen.getKey())) {
			return new MoveDecoratorState(createSimpleMove(origen, destino),
					state -> state.setEnroqueNegroReyPermitido(false));
		}		
		
		return createSimpleMove(origen, destino);
	}
	
	
	public Move createCaptureTorreMove(PosicionPieza origen, PosicionPieza destino) {
		if (Square.a1.equals(origen.getKey())) {
			return new MoveDecoratorState(createCaptureMove(origen, destino),
					state -> state.setEnroqueBlancoReinaPermitido(false));
		}
		
		if (Square.h1.equals(origen.getKey())) {
			return new MoveDecoratorState(createCaptureMove(origen, destino),
					state -> state.setEnroqueBlancoReyPermitido(false));
		}
		
		if (Square.a8.equals(origen.getKey())) {
			return new MoveDecoratorState(createCaptureMove(origen, destino),
					state -> state.setEnroqueNegroReinaPermitido(false));
		}
		
		if (Square.h8.equals(origen.getKey())) {
			return new MoveDecoratorState(createCaptureMove(origen, destino),
					state -> state.setEnroqueNegroReyPermitido(false));
		}		
		return createCaptureMove(origen, destino);
	}

	public Move createSaltoDoblePeonMove(PosicionPieza origen, PosicionPieza destino, Square saltoSimpleCasillero) {
		return new SaltoDoblePeonMove(origen, destino, saltoSimpleCasillero);
	}

	public Move createCapturePeonPasante(PosicionPieza origen, PosicionPieza destino,
			PosicionPieza capturaPeonPasante) {
		return new CapturePeonPasante(origen, destino, capturaPeonPasante);
	}

	public Move createSimplePeonPromocion(PosicionPieza origen, PosicionPieza destino, Pieza pieza) {
		return new SimplePeonPromocion(origen, destino, pieza);
	}

	public Move createCapturePeonPromocion(PosicionPieza origen, PosicionPieza destino, Pieza pieza) {
		return new CapturaPeonPromocion(origen, destino, pieza);
	}
	
}
