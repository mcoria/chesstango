package chess.moves;

import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;


/**
 * @author Mauricio Coria
 *
 */
public class MoveFactory {		
	
	public Move createSimpleKingMoveBlanco(PosicionPieza origen, PosicionPieza destino) {
		if(Square.e1.equals(origen.getKey())){
			return new MoveDecoratorKingState(new SimpleKingMove(origen, destino), state -> {
				state.setCastleWhiteReinaPermitido(false);
				state.setCastleWhiteKingPermitido(false);				
			});
		}
		return new SimpleKingMove(origen, destino);
	}


	public Move createCaptureKingMoveBlanco(PosicionPieza origen, PosicionPieza destino) {
		if(Square.e1.equals(origen.getKey())){
			return new MoveDecoratorKingState(new CaptureKingMove(origen, destino), state -> {
				state.setCastleWhiteReinaPermitido(false);
				state.setCastleWhiteKingPermitido(false);				
			});
		}
		return new CaptureKingMove(origen, destino);
	}
	

	public Move createSimpleKingMoveNegro(PosicionPieza origen, PosicionPieza destino) {
		if(Square.e8.equals(origen.getKey())){
			return new MoveDecoratorKingState(new SimpleKingMove(origen, destino), state -> {
				state.setCastleBlackReinaPermitido(false);
				state.setCastleBlackKingPermitido(false);			
			});
		}
		return new SimpleKingMove(origen, destino);
	}

	public Move createCaptureKingMoveNegro(PosicionPieza origen, PosicionPieza destino) {
		if(Square.e8.equals(origen.getKey())){
			return new MoveDecoratorKingState(new CaptureKingMove(origen, destino), state -> {
				state.setCastleBlackReinaPermitido(false);
				state.setCastleBlackKingPermitido(false);			
			});
		}
		return new CaptureKingMove(origen, destino);
	}
	
	
	public Move createSimpleTorreMove(PosicionPieza origen, PosicionPieza destino) {
		if (Square.a1.equals(origen.getKey())) {
			return new MoveDecoratorState(createSimpleMove(origen, destino),
					state -> state.setCastleWhiteReinaPermitido(false));
		}
		
		if (Square.h1.equals(origen.getKey())) {
			return new MoveDecoratorState(createSimpleMove(origen, destino),
					state -> state.setCastleWhiteKingPermitido(false));
		}
		
		if (Square.a8.equals(origen.getKey())) {
			return new MoveDecoratorState(createSimpleMove(origen, destino),
					state -> state.setCastleBlackReinaPermitido(false));
		}
		
		if (Square.h8.equals(origen.getKey())) {
			return new MoveDecoratorState(createSimpleMove(origen, destino),
					state -> state.setCastleBlackKingPermitido(false));
		}		
		
		return createSimpleMove(origen, destino);
	}
	
	
	public Move createCaptureTorreMove(PosicionPieza origen, PosicionPieza destino) {
		if (Square.a1.equals(origen.getKey())) {
			return new MoveDecoratorState(createCaptureMove(origen, destino),
					state -> state.setCastleWhiteReinaPermitido(false));
		}
		
		if (Square.h1.equals(origen.getKey())) {
			return new MoveDecoratorState(createCaptureMove(origen, destino),
					state -> state.setCastleWhiteKingPermitido(false));
		}
		
		if (Square.a8.equals(origen.getKey())) {
			return new MoveDecoratorState(createCaptureMove(origen, destino),
					state -> state.setCastleBlackReinaPermitido(false));
		}
		
		if (Square.h8.equals(origen.getKey())) {
			return new MoveDecoratorState(createCaptureMove(origen, destino),
					state -> state.setCastleBlackKingPermitido(false));
		}		
		return createCaptureMove(origen, destino);
	}

	public Move createSimpleMove(PosicionPieza origen, PosicionPieza destino){
		return new SimpleMove(origen, destino);
	}
	
	public Move createCaptureMove(PosicionPieza origen, PosicionPieza destino) {
		return new CaptureMove(origen, destino);
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
