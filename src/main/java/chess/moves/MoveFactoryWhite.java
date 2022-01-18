package chess.moves;

import chess.Piece;
import chess.PiecePositioned;
import chess.Square;


/**
 * @author Mauricio Coria
 *
 */
// TODO: Implement abstract factory pattern White and Black
public class MoveFactoryWhite implements MoveFactory {
	
	@Override
	public Move createSimpleKingMove(PiecePositioned origen, PiecePositioned destino) {
		MoveKing kingMove = new SimpleKingMove(origen, destino);
		Move result = kingMove;
		if(Square.e1.equals(origen.getKey())){
			result = whiteLostCastlingWrapper(kingMove);
		}
		return result;
	}

	@Override
	public Move createCaptureKingMove(PiecePositioned origen, PiecePositioned destino) {
		MoveKing kingMove = createCaptureKingMoveImp(origen, destino);
		Move result = kingMove;
		if(Square.e1.equals(origen.getKey())){
			result = whiteLostCastlingWrapper(kingMove);
		}
		return result;
	}	
	

	@Override
	public Move createSimpleRookMove(PiecePositioned origen, PiecePositioned destino) {
		Move rookMove = createSimpleMove(origen, destino);
		Move result = rookMove;

		if (Square.a1.equals(origen.getKey())) {
			result = new MoveDecoratorState(rookMove, state -> state.setCastlingWhiteQueenAllowed(false));
		} else if (Square.h1.equals(origen.getKey())) {
			result = new MoveDecoratorState(rookMove, state -> state.setCastlingWhiteKingAllowed(false));
		}

		return result;
	}


	@Override
	public Move createCaptureRookMove(PiecePositioned origen, PiecePositioned destino) {
		Move rookMove = createCaptureMove(origen, destino);
		Move result = rookMove;

		if (Square.a1.equals(origen.getKey())) {
			result = new MoveDecoratorState(rookMove, state -> state.setCastlingWhiteQueenAllowed(false));
		} else if (Square.h1.equals(origen.getKey())) {
			result = new MoveDecoratorState(rookMove, state -> state.setCastlingWhiteKingAllowed(false));
		}

		return result;
	}


	@Override
	public Move createSimpleMove(PiecePositioned origen, PiecePositioned destino){
		return new SimpleMove(origen, destino);
	}
	

	@Override
	public Move createCaptureMove(PiecePositioned origen, PiecePositioned destino) {
		Move move = new CaptureMove(origen, destino);
		Move result = move;
		if (Square.a8.equals(destino.getKey())) {
			result = new MoveDecoratorState(move, state -> state.setCastlingBlackQueenAllowed(false));
		} else if (Square.h8.equals(destino.getKey())) {
			result = new MoveDecoratorState(move, state -> state.setCastlingBlackKingAllowed(false));
		}
		return result;
	}


	@Override
	public Move createSaltoDoblePawnMove(PiecePositioned origen, PiecePositioned destino, Square saltoSimpleCasillero) {
		return new SaltoDoblePawnMove(origen, destino, saltoSimpleCasillero);
	}


	@Override
	public Move createCapturePawnPasante(PiecePositioned origen, PiecePositioned destino,
			PiecePositioned capturaPawnPasante) {
		return new CapturePawnPasante(origen, destino, capturaPawnPasante);
	}


	@Override
	public Move createSimplePawnPromocion(PiecePositioned origen, PiecePositioned destino, Piece piece) {
		return new SimplePawnPromocion(origen, destino, piece);
	}


	@Override
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
	
	private MoveKing createCaptureKingMoveImp(PiecePositioned origen, PiecePositioned destino) {
		MoveKing move = new CaptureKingMove(origen, destino);
		MoveKing result = move;

		if (Square.a8.equals(destino.getKey())) {
			result = new MoveDecoratorKingState(move, state -> state.setCastlingBlackQueenAllowed(false));
		} else if (Square.h8.equals(destino.getKey())) {
			result = new MoveDecoratorKingState(move, state -> state.setCastlingBlackKingAllowed(false));
		}

		return result;
	}
	
}
