package chess.moves;

import chess.Piece;
import chess.PiecePositioned;
import chess.Square;


/**
 * @author Mauricio Coria
 *
 */
// TODO: Implement abstract factory pattern White and Black
public class MoveFactory {		
	
	public Move createSimpleKingMoveWhite(PiecePositioned origen, PiecePositioned destino) {
		MoveKing kingMove = new SimpleKingMove(origen, destino);
		Move result = kingMove;
		if(Square.e1.equals(origen.getKey())){
			result = whiteLostCastlingWrapper(kingMove);
		}
		return result;
	}


	public Move createCaptureKingMoveWhite(PiecePositioned origen, PiecePositioned destino) {
		MoveKing kingMove = createCaptureKingMove(origen, destino);
		Move result = kingMove;
		if(Square.e1.equals(origen.getKey())){
			result = whiteLostCastlingWrapper(kingMove);
		}
		return result;
	}


	public Move createSimpleKingMoveBlack(PiecePositioned origen, PiecePositioned destino) {
		SimpleKingMove kingMove = new SimpleKingMove(origen, destino);
		Move result = kingMove;		
		if(Square.e8.equals(origen.getKey())){
			result = blackLostCastlingWrapper(kingMove);
		}
		return result;
	}

	public Move createCaptureKingMoveBlack(PiecePositioned origen, PiecePositioned destino) {
		CaptureKingMove kingMove = new CaptureKingMove(origen, destino);
		Move result = kingMove;
		if(Square.e8.equals(origen.getKey())){
			result = blackLostCastlingWrapper(kingMove);
		}
		return result;
	}	
	
	public Move createSimpleRookMove(PiecePositioned origen, PiecePositioned destino) {
		Move rookMove = createSimpleMove(origen, destino);
		Move result = rookMove;
		if (Square.a1.equals(origen.getKey())) {
			result =  new MoveDecoratorState(rookMove, state -> state.setCastlingWhiteQueenAllowed(false));
		}

		if (Square.h1.equals(origen.getKey())) {
			result =  new MoveDecoratorState(rookMove, state -> state.setCastlingWhiteKingAllowed(false));
		}

		if (Square.a8.equals(origen.getKey())) {
			result =  new MoveDecoratorState(rookMove, state -> state.setCastlingBlackQueenAllowed(false));
		}

		if (Square.h8.equals(origen.getKey())) {
			result =  new MoveDecoratorState(rookMove, state -> state.setCastlingBlackKingAllowed(false));
		}

		return result;
	}

	public Move createCaptureRookMove(PiecePositioned origen, PiecePositioned destino) {
		Move rookMove = createCaptureMove(origen, destino);
		Move result = rookMove;
		if (Square.a1.equals(origen.getKey())) {
			result =  new MoveDecoratorState(rookMove, state -> state.setCastlingWhiteQueenAllowed(false));
		}

		if (Square.h1.equals(origen.getKey())) {
			result =  new MoveDecoratorState(rookMove, state -> state.setCastlingWhiteKingAllowed(false));
		}

		if (Square.a8.equals(origen.getKey())) {
			result =  new MoveDecoratorState(rookMove, state -> state.setCastlingBlackQueenAllowed(false));
		}

		if (Square.h8.equals(origen.getKey())) {
			result =  new MoveDecoratorState(rookMove, state -> state.setCastlingBlackKingAllowed(false));
		}
		
		return result;
	}

	public Move createSimpleMove(PiecePositioned origen, PiecePositioned destino){
		return new SimpleMove(origen, destino);
	}
	
	public Move createCaptureMove(PiecePositioned origen, PiecePositioned destino) {
		Move move = new CaptureMove(origen, destino);
		Move result = move;
		if (Square.a1.equals(destino.getKey())) {
			result = new MoveDecoratorState(move, state -> state.setCastlingWhiteQueenAllowed(false));
		}

		if (Square.h1.equals(destino.getKey())) {
			result = new MoveDecoratorState(move, state -> state.setCastlingWhiteKingAllowed(false));
		}

		if (Square.a8.equals(destino.getKey())) {
			result = new MoveDecoratorState(move, state -> state.setCastlingBlackQueenAllowed(false));
		}

		if (Square.h8.equals(destino.getKey())) {
			result = new MoveDecoratorState(move, state -> state.setCastlingBlackKingAllowed(false));
		}
		return result;
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
	
	protected Move whiteLostCastlingWrapper(MoveKing move){
		return new MoveDecoratorKingState(move, state -> {
			state.setCastlingWhiteQueenAllowed(false);
			state.setCastlingWhiteKingAllowed(false);				
		});
	}
	
	protected Move blackLostCastlingWrapper(MoveKing move){
		return new MoveDecoratorKingState(move, state -> {
			state.setCastlingBlackQueenAllowed(false);
			state.setCastlingBlackKingAllowed(false);				
		});
	}
	
	private MoveKing createCaptureKingMove(PiecePositioned origen, PiecePositioned destino) {
		MoveKing move = new CaptureKingMove(origen, destino);
		MoveKing result = move;
		if (Square.a1.equals(destino.getKey())) {
			result = new MoveDecoratorKingState(move, state -> state.setCastlingWhiteQueenAllowed(false));
		}

		if (Square.h1.equals(destino.getKey())) {
			result = new MoveDecoratorKingState(move, state -> state.setCastlingWhiteKingAllowed(false));
		}

		if (Square.a8.equals(destino.getKey())) {
			result = new MoveDecoratorKingState(move, state -> state.setCastlingBlackQueenAllowed(false));
		}

		if (Square.h8.equals(destino.getKey())) {
			result = new MoveDecoratorKingState(move, state -> state.setCastlingBlackKingAllowed(false));
		}
		return result;
	}	
	
}
