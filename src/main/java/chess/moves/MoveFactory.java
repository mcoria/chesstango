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
				state.setCastlingWhiteReinaPermitido(false);
				state.setCastlingWhiteKingPermitido(false);				
			});
		}
		return new SimpleKingMove(origen, destino);
	}


	public Move createCaptureKingMoveBlanco(PosicionPieza origen, PosicionPieza destino) {
		if(Square.e1.equals(origen.getKey())){
			return new MoveDecoratorKingState(new CaptureKingMove(origen, destino), state -> {
				state.setCastlingWhiteReinaPermitido(false);
				state.setCastlingWhiteKingPermitido(false);				
			});
		}
		return new CaptureKingMove(origen, destino);
	}
	

	public Move createSimpleKingMoveNegro(PosicionPieza origen, PosicionPieza destino) {
		if(Square.e8.equals(origen.getKey())){
			return new MoveDecoratorKingState(new SimpleKingMove(origen, destino), state -> {
				state.setCastlingBlackReinaPermitido(false);
				state.setCastlingBlackKingPermitido(false);			
			});
		}
		return new SimpleKingMove(origen, destino);
	}

	public Move createCaptureKingMoveNegro(PosicionPieza origen, PosicionPieza destino) {
		if(Square.e8.equals(origen.getKey())){
			return new MoveDecoratorKingState(new CaptureKingMove(origen, destino), state -> {
				state.setCastlingBlackReinaPermitido(false);
				state.setCastlingBlackKingPermitido(false);			
			});
		}
		return new CaptureKingMove(origen, destino);
	}
	
	
	public Move createSimpleTorreMove(PosicionPieza origen, PosicionPieza destino) {
		if (Square.a1.equals(origen.getKey())) {
			return new MoveDecoratorState(createSimpleMove(origen, destino),
					state -> state.setCastlingWhiteReinaPermitido(false));
		}
		
		if (Square.h1.equals(origen.getKey())) {
			return new MoveDecoratorState(createSimpleMove(origen, destino),
					state -> state.setCastlingWhiteKingPermitido(false));
		}
		
		if (Square.a8.equals(origen.getKey())) {
			return new MoveDecoratorState(createSimpleMove(origen, destino),
					state -> state.setCastlingBlackReinaPermitido(false));
		}
		
		if (Square.h8.equals(origen.getKey())) {
			return new MoveDecoratorState(createSimpleMove(origen, destino),
					state -> state.setCastlingBlackKingPermitido(false));
		}		
		
		return createSimpleMove(origen, destino);
	}
	
	
	public Move createCaptureTorreMove(PosicionPieza origen, PosicionPieza destino) {
		if (Square.a1.equals(origen.getKey())) {
			return new MoveDecoratorState(createCaptureMove(origen, destino),
					state -> state.setCastlingWhiteReinaPermitido(false));
		}
		
		if (Square.h1.equals(origen.getKey())) {
			return new MoveDecoratorState(createCaptureMove(origen, destino),
					state -> state.setCastlingWhiteKingPermitido(false));
		}
		
		if (Square.a8.equals(origen.getKey())) {
			return new MoveDecoratorState(createCaptureMove(origen, destino),
					state -> state.setCastlingBlackReinaPermitido(false));
		}
		
		if (Square.h8.equals(origen.getKey())) {
			return new MoveDecoratorState(createCaptureMove(origen, destino),
					state -> state.setCastlingBlackKingPermitido(false));
		}		
		return createCaptureMove(origen, destino);
	}

	public Move createSimpleMove(PosicionPieza origen, PosicionPieza destino){
		return new SimpleMove(origen, destino);
	}
	
	public Move createCaptureMove(PosicionPieza origen, PosicionPieza destino) {
		return new CaptureMove(origen, destino);
	}
	
	public Move createSaltoDoblePawnMove(PosicionPieza origen, PosicionPieza destino, Square saltoSimpleCasillero) {
		return new SaltoDoblePawnMove(origen, destino, saltoSimpleCasillero);
	}

	public Move createCapturePawnPasante(PosicionPieza origen, PosicionPieza destino,
			PosicionPieza capturaPawnPasante) {
		return new CapturePawnPasante(origen, destino, capturaPawnPasante);
	}

	public Move createSimplePawnPromocion(PosicionPieza origen, PosicionPieza destino, Pieza pieza) {
		return new SimplePawnPromocion(origen, destino, pieza);
	}

	public Move createCapturePawnPromocion(PosicionPieza origen, PosicionPieza destino, Pieza pieza) {
		return new CapturaPawnPromocion(origen, destino, pieza);
	}
	
}
