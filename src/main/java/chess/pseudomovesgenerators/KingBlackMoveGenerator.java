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

	protected static final Square INTERMEDIO_TORRE_REYNA_SQUARE = Square.b8;
	protected static final Square DESTINO_REYNA_SQUARE = Square.c8;
	protected static final Square INTERMEDIO_REY_REYNA_SQUARE = Square.d8;
	
	protected static final Square INTERMEDIO_REY_REY_SQUARE = Square.f8;	
	protected static final Square DESTINO_REY_SQUARE = Square.g8;
	
	
	public KingBlackMoveGenerator() {
		super(Color.BLACK);
	}
	
	@Override
	public void generateMovesPseudoMoves(PiecePositioned origen) {		
		super.generateMovesPseudoMoves(origen);
		
		//this.saveMovesInCache =  ! (this.boardState.isCastlingBlackQueenPermitido() ||  this.boardState.isCastlingBlackKingPermitido()) ;
		
		if (this.boardState.isCastlingBlackQueenPermitido()){
			result.affectedByContainerAdd(INTERMEDIO_TORRE_REYNA_SQUARE);
			result.affectedByContainerAdd(DESTINO_REYNA_SQUARE);
			result.affectedByContainerAdd(INTERMEDIO_REY_REYNA_SQUARE);		
			result.affectedByContainerAdd(Square.a8); //La posicion de la torre
			if(puedeEnroqueQueen(	origen, 
								CachePosiciones.REY_BLACK, 
								CachePosiciones.TORRE_BLACK_REYNA,
								INTERMEDIO_TORRE_REYNA_SQUARE, 
								DESTINO_REYNA_SQUARE, 
								INTERMEDIO_REY_REYNA_SQUARE)) {
				result.moveContainerAdd(new CastlingBlackQueenMove());
			}
		}
			
			
		if (this.boardState.isCastlingBlackKingPermitido()){
			result.affectedByContainerAdd(INTERMEDIO_REY_REY_SQUARE);
			result.affectedByContainerAdd(DESTINO_REY_SQUARE);
			result.affectedByContainerAdd(Square.h8); //La posicion de la torre
			if(puedeEnroqueKing(	origen, 
								CachePosiciones.REY_BLACK, 
								CachePosiciones.TORRE_BLACK_REY,
								DESTINO_REY_SQUARE, 
								INTERMEDIO_REY_REY_SQUARE)) {
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
