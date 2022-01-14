package chess.moves;

import chess.Piece;
import chess.PiecePositioned;
import chess.Square;


/**
 * @author Mauricio Coria
 *
 */
public class MoveFactory {		
	
	public Move createSimpleKingMoveWhite(PiecePositioned origen, PiecePositioned destino) {
		if(Square.e1.equals(origen.getKey())){
			return new MoveDecoratorKingState(new SimpleKingMove(origen, destino), state -> {
				state.setCastlingWhiteQueenAllowed(false);
				state.setCastlingWhiteKingAllowed(false);				
			});
		}
		return new SimpleKingMove(origen, destino);
	}


	public Move createCaptureKingMoveWhite(PiecePositioned origen, PiecePositioned destino) {
		if(Square.e1.equals(origen.getKey())){
			return new MoveDecoratorKingState(new CaptureKingMove(origen, destino), state -> {
				state.setCastlingWhiteQueenAllowed(false);
				state.setCastlingWhiteKingAllowed(false);				
			});
		}
		return new CaptureKingMove(origen, destino);
	}
	

	public Move createSimpleKingMoveBlack(PiecePositioned origen, PiecePositioned destino) {
		if(Square.e8.equals(origen.getKey())){
			return new MoveDecoratorKingState(new SimpleKingMove(origen, destino), state -> {
				state.setCastlingBlackQueenAllowed(false);
				state.setCastlingBlackKingAllowed(false);			
			});
		}
		return new SimpleKingMove(origen, destino);
	}

	public Move createCaptureKingMoveBlack(PiecePositioned origen, PiecePositioned destino) {
		if(Square.e8.equals(origen.getKey())){
			return new MoveDecoratorKingState(new CaptureKingMove(origen, destino), state -> {
				state.setCastlingBlackQueenAllowed(false);
				state.setCastlingBlackKingAllowed(false);			
			});
		}
		return new CaptureKingMove(origen, destino);
	}
	
	
	public Move createSimpleRookMove(PiecePositioned origen, PiecePositioned destino) {
		if (Square.a1.equals(origen.getKey())) {
			return new MoveDecoratorState(createSimpleMove(origen, destino),
					state -> state.setCastlingWhiteQueenAllowed(false));
		}
		
		if (Square.h1.equals(origen.getKey())) {
			return new MoveDecoratorState(createSimpleMove(origen, destino),
					state -> state.setCastlingWhiteKingAllowed(false));
		}
		
		if (Square.a8.equals(origen.getKey())) {
			return new MoveDecoratorState(createSimpleMove(origen, destino),
					state -> state.setCastlingBlackQueenAllowed(false));
		}
		
		if (Square.h8.equals(origen.getKey())) {
			return new MoveDecoratorState(createSimpleMove(origen, destino),
					state -> state.setCastlingBlackKingAllowed(false));
		}		
		
		return createSimpleMove(origen, destino);
	}
	
	
	public Move createCaptureRookMove(PiecePositioned origen, PiecePositioned destino) {
		if (Square.a1.equals(origen.getKey())) {
			return new MoveDecoratorState(createCaptureMove(origen, destino),
					state -> state.setCastlingWhiteQueenAllowed(false));
		}
		
		if (Square.h1.equals(origen.getKey())) {
			return new MoveDecoratorState(createCaptureMove(origen, destino),
					state -> state.setCastlingWhiteKingAllowed(false));
		}
		
		if (Square.a8.equals(origen.getKey())) {
			return new MoveDecoratorState(createCaptureMove(origen, destino),
					state -> state.setCastlingBlackQueenAllowed(false));
		}
		
		if (Square.h8.equals(origen.getKey())) {
			return new MoveDecoratorState(createCaptureMove(origen, destino),
					state -> state.setCastlingBlackKingAllowed(false));
		}		
		return createCaptureMove(origen, destino);
	}

	public Move createSimpleMove(PiecePositioned origen, PiecePositioned destino){
		return new SimpleMove(origen, destino);
	}
	
	public Move createCaptureMove(PiecePositioned origen, PiecePositioned destino) {
		return new CaptureMove(origen, destino);
	}
	
	public Move createSaltoDoblePawnMove(PiecePositioned origen, PiecePositioned destino, Square saltoSimpleCasillero) {
		return new SaltoDoblePawnMove(origen, destino, saltoSimpleCasillero);
	}

	public Move createCapturePawnPasante(PiecePositioned origen, PiecePositioned destino,
			PiecePositioned capturaPawnPasante) {
		return new CapturePawnPasante(origen, destino, capturaPawnPasante);
	}

	public Move createSimplePawnPromocion(PiecePositioned origen, PiecePositioned destino, Piece piece) {
		return new SimplePawnPromocion(origen, destino, piece);
	}

	public Move createCapturePawnPromocion(PiecePositioned origen, PiecePositioned destino, Piece piece) {
		return new CapturaPawnPromocion(origen, destino, piece);
	}
	
}
