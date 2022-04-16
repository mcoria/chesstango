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
public class KingWhiteMoveGenerator extends AbstractKingMoveGenerator {

	protected static final Square INTERMEDIO_ROOK_QUEEN_SQUARE = Square.b1;
	protected static final Square DESTINO_QUEEN_SQUARE = Square.c1;
	protected static final Square INTERMEDIO_KING_QUEEN_SQUARE = Square.d1;
	
	protected static final Square INTERMEDIO_KING_KING_SQUARE = Square.f1;	
	protected static final Square DESTINO_KING_SQUARE = Square.g1;
	
	
	public KingWhiteMoveGenerator() {
		super(Color.WHITE);
	}
	
	@Override
	public Collection<Move> generateCastlingPseudoMoves() {
		Collection<Move> moveContainer = createContainer();
		if (this.positionState.isCastlingWhiteQueenAllowed()){
			if(puedeEnroqueQueen(	kingCacheBoard.getSquareKingWhiteCache(), 
								PiecePositioned.KING_WHITE, 
								PiecePositioned.ROOK_WHITE_QUEEN,
								INTERMEDIO_ROOK_QUEEN_SQUARE, 
								DESTINO_QUEEN_SQUARE, 
								INTERMEDIO_KING_QUEEN_SQUARE)) {
				moveContainer.add(moveFactory.createCastlingQueenMove());
			}
		}
		
		
		if (this.positionState.isCastlingWhiteKingAllowed() ){
			if(puedeEnroqueKing(	kingCacheBoard.getSquareKingWhiteCache(), 
								PiecePositioned.KING_WHITE, 
								PiecePositioned.ROOK_WHITE_KING,
								DESTINO_KING_SQUARE, 
								INTERMEDIO_KING_KING_SQUARE)) {
				moveContainer.add(moveFactory.createCastlingKingMove());
			}
		}
		return moveContainer;
	}		
	
	//TODO: agregar test case (cuando el king se mueve pierde castling) y agregar validacion en state 
	@Override
	protected Move createSimpleMove(PiecePositioned origen, PiecePositioned destino) {
		return this.moveFactory.createSimpleKingMove(origen, destino);
	}

	@Override
	protected Move createCaptureMove(PiecePositioned origen, PiecePositioned destino) {
		return this.moveFactory.createCaptureKingMove(origen, destino);
	}


}
