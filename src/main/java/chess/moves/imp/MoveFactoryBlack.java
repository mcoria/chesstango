package chess.moves.imp;

import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.moves.Move;
import chess.moves.MoveCastling;
import chess.moves.MoveFactory;
import chess.moves.MoveKing;


/**
 * @author Mauricio Coria
 *
 */
public class MoveFactoryBlack implements MoveFactory {
	
	public static final MoveCastling castlingKingMove = new CastlingBlackKingMove();
	public static final MoveCastling castlingQueenMove = new CastlingBlackQueenMove();	

	@Override
	public MoveKing createSimpleKingMove(PiecePositioned origen, PiecePositioned destino) {
		MoveKing kingMove = new SimpleKingMove(origen, destino);
		MoveKing result = kingMove;
		if(Square.e8.equals(origen.getKey())){
			result = blackLostCastlingWrapper(kingMove);
		}
		return result;
	}


	@Override
	public MoveKing createCaptureKingMove(PiecePositioned origen, PiecePositioned destino) {
		MoveKing kingMove = createCaptureKingMoveImp(origen, destino);
		MoveKing result = kingMove;
		if(Square.e8.equals(origen.getKey())){
			result = blackLostCastlingWrapper(kingMove);
		}
		return result;
	}	
	

	@Override
	public Move createSimpleRookMove(PiecePositioned origen, PiecePositioned destino) {
		Move rookMove = createSimpleMove(origen, destino);
		Move result = rookMove;

		if (Square.a8.equals(origen.getKey())) {
			result = new MoveDecoratorState(rookMove, state -> state.setCastlingBlackQueenAllowed(false));
		} else if (Square.h8.equals(origen.getKey())) {
			result = new MoveDecoratorState(rookMove, state -> state.setCastlingBlackKingAllowed(false));
		}

		return result;
	}


	@Override
	public Move createCaptureRookMove(PiecePositioned origen, PiecePositioned destino) {
		Move rookMove = createCaptureMove(origen, destino);
		Move result = rookMove;

		if (Square.a8.equals(origen.getKey())) {
			result = new MoveDecoratorState(rookMove, state -> state.setCastlingBlackQueenAllowed(false));
		} else if (Square.h8.equals(origen.getKey())) {
			result = new MoveDecoratorState(rookMove, state -> state.setCastlingBlackKingAllowed(false));
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
		if (Square.a1.equals(destino.getKey())) {
			result = new MoveDecoratorState(move, state -> state.setCastlingWhiteQueenAllowed(false));
		} else if (Square.h1.equals(destino.getKey())) {
			result = new MoveDecoratorState(move, state -> state.setCastlingWhiteKingAllowed(false));
		}

		return result;
	}

	@Override
	public Move createSaltoDoblePawnMove(PiecePositioned origen, PiecePositioned destino, Square saltoSimpleCasillero) {
		return new SaltoDoblePawnMove(origen, destino, saltoSimpleCasillero);
	}


	@Override
	public Move createCaptureEnPassant(PiecePositioned origen, PiecePositioned destino,
			PiecePositioned capturaEnPassant) {
		return new CaptureEnPassant(origen, destino, capturaEnPassant);
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
	
	protected MoveKing blackLostCastlingWrapper(MoveKing move){
		return new MoveDecoratorKingState(move, state -> {
			state.setCastlingBlackQueenAllowed(false);
			state.setCastlingBlackKingAllowed(false);				
		});
	}
	
	private MoveKing createCaptureKingMoveImp(PiecePositioned origen, PiecePositioned destino) {
		MoveKing move = new CaptureKingMove(origen, destino);
		MoveKing result = move;

		if (Square.a1.equals(destino.getKey())) {
			result = new MoveDecoratorKingState(move, state -> state.setCastlingWhiteQueenAllowed(false));
		} else if (Square.h1.equals(destino.getKey())) {
			result = new MoveDecoratorKingState(move, state -> state.setCastlingWhiteKingAllowed(false));
		}

		return result;
	}
	
	
	@Override
	public MoveCastling createCastlingQueenMove() {
		return castlingQueenMove;
	}

	@Override
	public MoveCastling createCastlingKingMove() {
		return castlingKingMove;
	}	
}
