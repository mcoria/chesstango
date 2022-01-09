package chess.pseudomovesgenerators;

import chess.CachePosiciones;
import chess.Color;
import chess.PiecePositioned;
import chess.Square;
import chess.moves.CastlingBlackKingMove;
import chess.moves.CastlingBlackQueenMove;
import chess.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public class KingBlackMoveGenerator extends KingAbstractMoveGenerator {

	protected static final Square INTERMEDIO_ROOK_QUEEN_SQUARE = Square.b8;
	protected static final Square DESTINO_QUEEN_SQUARE = Square.c8;
	protected static final Square INTERMEDIO_KING_QUEEN_SQUARE = Square.d8;
	
	protected static final Square INTERMEDIO_KING_KING_SQUARE = Square.f8;	
	protected static final Square DESTINO_KING_SQUARE = Square.g8;
	
	
	public KingBlackMoveGenerator() {
		super(Color.BLACK);
	}
	
	@Override
	public void generateMovesPseudoMoves(PiecePositioned origen) {		
		super.generateMovesPseudoMoves(origen);
		
		//this.saveMovesInCache =  ! (this.boardState.isCastlingBlackQueenAllowed() ||  this.boardState.isCastlingBlackKingAllowed()) ;
		
		if (this.positionState.isCastlingBlackQueenAllowed()){
			result.affectedByContainerAdd(INTERMEDIO_ROOK_QUEEN_SQUARE);
			result.affectedByContainerAdd(DESTINO_QUEEN_SQUARE);
			result.affectedByContainerAdd(INTERMEDIO_KING_QUEEN_SQUARE);		
			result.affectedByContainerAdd(Square.a8); //La posicion de la torre
			if(puedeEnroqueQueen(	origen, 
								CachePosiciones.KING_BLACK, 
								CachePosiciones.ROOK_BLACK_QUEEN,
								INTERMEDIO_ROOK_QUEEN_SQUARE, 
								DESTINO_QUEEN_SQUARE, 
								INTERMEDIO_KING_QUEEN_SQUARE)) {
				result.moveContainerAdd(new CastlingBlackQueenMove());
			}
		}
			
			
		if (this.positionState.isCastlingBlackKingAllowed()){
			result.affectedByContainerAdd(INTERMEDIO_KING_KING_SQUARE);
			result.affectedByContainerAdd(DESTINO_KING_SQUARE);
			result.affectedByContainerAdd(Square.h8); //La posicion de la torre
			if(puedeEnroqueKing(	origen, 
								CachePosiciones.KING_BLACK, 
								CachePosiciones.ROOK_BLACK_KING,
								DESTINO_KING_SQUARE, 
								INTERMEDIO_KING_KING_SQUARE)) {
				result.moveContainerAdd(new CastlingBlackKingMove());
			}
		}
	}

	//TODO: agregar test case (cuando el king se mueve pierde enroque) y agregar validacion en state 
	@Override
	protected Move createSimpleMove(PiecePositioned origen, PiecePositioned destino) {
		return this.moveFactory.createSimpleKingMoveNegro(origen, destino);
	}

	@Override
	protected Move createCaptureMove(PiecePositioned origen, PiecePositioned destino) {
		return this.moveFactory.createCaptureKingMoveNegro(origen, destino);
	}	

}
