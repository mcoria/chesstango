package chess.board.pseudomovesgenerators.strategies;

import java.util.Collection;

import chess.board.Color;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public class KingBlackMoveGenerator extends AbstractKingMoveGenerator {

	protected static final Square INTERMEDIO_ROOK_QUEEN_SQUARE = Square.b8;
	protected static final Square DESTINO_QUEEN_SQUARE = Square.c8;
	protected static final Square INTERMEDIO_KING_QUEEN_SQUARE = Square.d8;
	
	protected static final Square INTERMEDIO_KING_KING_SQUARE = Square.f8;	
	protected static final Square DESTINO_KING_SQUARE = Square.g8;
	
	
	public KingBlackMoveGenerator() {
		super(Color.BLACK);
	}

	@Override
	public Collection<Move> generateCastlingPseudoMoves() {
		Collection<Move> moveContainer = createContainer();
		if (this.positionState.isCastlingBlackQueenAllowed()){
			if(puedeEnroqueQueen(	kingCacheBoard.getSquareKingBlackCache(), 
								PiecePositioned.KING_BLACK, 
								PiecePositioned.ROOK_BLACK_QUEEN,
								INTERMEDIO_ROOK_QUEEN_SQUARE, 
								DESTINO_QUEEN_SQUARE, 
								INTERMEDIO_KING_QUEEN_SQUARE)) {
				moveContainer.add(moveFactory.createCastlingQueenMove());
			}
		}
			
			
		if (this.positionState.isCastlingBlackKingAllowed()){
			if(puedeEnroqueKing(	kingCacheBoard.getSquareKingBlackCache(), 
								PiecePositioned.KING_BLACK, 
								PiecePositioned.ROOK_BLACK_KING,
								DESTINO_KING_SQUARE, 
								INTERMEDIO_KING_KING_SQUARE)) {
				moveContainer.add(moveFactory.createCastlingKingMove());
			}
		}
		return moveContainer;
	}	

	//TODO: agregar test case (cuando el king se muevOe pierde castling) y agregar validacion en state 
	@Override
	protected Move createSimpleMove(PiecePositioned origen, PiecePositioned destino) {
		return this.moveFactory.createSimpleKingMove(origen, destino);
	}

	@Override
	protected Move createCaptureMove(PiecePositioned origen, PiecePositioned destino) {
		return this.moveFactory.createCaptureKingMove(origen, destino);
	}	

}
