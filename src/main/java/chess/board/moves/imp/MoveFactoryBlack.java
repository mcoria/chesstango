package chess.board.moves.imp;

import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.Cardinal;
import chess.board.moves.Move;
import chess.board.moves.MoveCastling;
import chess.board.moves.MoveFactory;
import chess.board.moves.MoveKing;


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
	public Move createSimpleMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
		return new SimpleMove(origen, destino, cardinal);
	}

	@Override
	public Move createSimplePawnMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
		return new SimplePawnMove(origen, destino, cardinal);
	}

	@Override
	public Move createCapturePawnMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
		Move move = new CapturePawnMove(origen, destino);
		Move result = move;
		if (Square.a1.equals(destino.getKey())) {
			result = new MoveDecoratorState(move, state -> state.setCastlingWhiteQueenAllowed(false));
		} else if (Square.h1.equals(destino.getKey())) {
			result = new MoveDecoratorState(move, state -> state.setCastlingWhiteKingAllowed(false));
		}
		return result;
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
	public Move createCaptureMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
		Move move = new CaptureMove(origen, destino, cardinal);
		Move result = move;
		if (Square.a1.equals(destino.getKey())) {
			result = new MoveDecoratorState(move, state -> state.setCastlingWhiteQueenAllowed(false));
		} else if (Square.h1.equals(destino.getKey())) {
			result = new MoveDecoratorState(move, state -> state.setCastlingWhiteKingAllowed(false));
		}
		return result;
	}

	@Override
	public Move createSaltoDoblePawnMove(PiecePositioned origen, PiecePositioned destino, Square saltoSimpleCasillero, Cardinal cardinal) {
		return new SimpleTwoSquaresPawnMove(origen, destino, saltoSimpleCasillero, cardinal);
	}


	@Override
	public Move createCaptureEnPassant(PiecePositioned origen, PiecePositioned destino,
			PiecePositioned capturaEnPassant) {
		return new CapturePawnEnPassant(origen, destino, capturaEnPassant);
	}


	@Override
	public Move createSimplePawnPromotion(PiecePositioned origen, PiecePositioned destino, Piece piece) {
		return new SimplePawnPromotion(origen, destino, piece);
	}


	@Override
	public Move createCapturePawnPromotion(PiecePositioned origen, PiecePositioned destino, Piece piece) {
		Move move = new CapturePawnPromotion(origen, destino, piece);
		Move result = move;
		if (Square.a1.equals(destino.getKey())) {
			result = new MoveDecoratorState(move, state -> state.setCastlingWhiteQueenAllowed(false));
		} else if (Square.h1.equals(destino.getKey())) {
			result = new MoveDecoratorState(move, state -> state.setCastlingWhiteKingAllowed(false));
		}
		return result;		
	}
	
	protected MoveKing blackLostCastlingWrapper(MoveKing move){
		return new MoveDecoratorKingState(move, state -> {
			state.setCastlingBlackQueenAllowed(false);
			state.setCastlingBlackKingAllowed(false);				
		});
	}
	
	protected MoveKing createCaptureKingMoveImp(PiecePositioned origen, PiecePositioned destino) {
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
