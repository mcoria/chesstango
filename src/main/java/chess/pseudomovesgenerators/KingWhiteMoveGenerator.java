package chess.pseudomovesgenerators;

import chess.CachePosiciones;
import chess.Color;
import chess.PiecePositioned;
import chess.Square;
import chess.moves.CastlingWhiteKingMove;
import chess.moves.CastlingWhiteQueenMove;
import chess.moves.Move;


/**
 * @author Mauricio Coria
 *
 */
public class KingWhiteMoveGenerator extends KingAbstractMoveGenerator {

	protected static final Square INTERMEDIO_ROOK_QUEEN_SQUARE = Square.b1;
	protected static final Square DESTINO_QUEEN_SQUARE = Square.c1;
	protected static final Square INTERMEDIO_KING_QUEEN_SQUARE = Square.d1;
	
	protected static final Square INTERMEDIO_KING_KING_SQUARE = Square.f1;	
	protected static final Square DESTINO_KING_SQUARE = Square.g1;
	
	
	public KingWhiteMoveGenerator() {
		super(Color.WHITE);
	}
	
	@Override
	public void generateMovesPseudoMoves(PiecePositioned origen) {
		super.generateMovesPseudoMoves(origen);
		
		if (this.boardState.isCastlingWhiteQueenPermitido()){
			result.affectedByContainerAdd(INTERMEDIO_ROOK_QUEEN_SQUARE);
			result.affectedByContainerAdd(DESTINO_QUEEN_SQUARE);
			result.affectedByContainerAdd(INTERMEDIO_KING_QUEEN_SQUARE);
			result.affectedByContainerAdd(Square.a1); //La posicion de la torre
			if(puedeEnroqueQueen(	origen, 
								CachePosiciones.KING_WHITE, 
								CachePosiciones.ROOK_WHITE_QUEEN,
								INTERMEDIO_ROOK_QUEEN_SQUARE, 
								DESTINO_QUEEN_SQUARE, 
								INTERMEDIO_KING_QUEEN_SQUARE)) {
				result.moveContainerAdd(new CastlingWhiteQueenMove());
			}
		}
		
		
		if (this.boardState.isCastlingWhiteKingPermitido() ){
			result.affectedByContainerAdd(INTERMEDIO_KING_KING_SQUARE);
			result.affectedByContainerAdd(DESTINO_KING_SQUARE);
			result.affectedByContainerAdd(Square.h1); //La posicion de la torre		
			if(puedeEnroqueKing(	origen, 
								CachePosiciones.KING_WHITE, 
								CachePosiciones.ROOK_WHITE_KING,
								DESTINO_KING_SQUARE, 
								INTERMEDIO_KING_KING_SQUARE)) {
				this.result.moveContainerAdd(new CastlingWhiteKingMove());
			}
		}
	}
	
	//TODO: agregar test case (cuando el king se mueve pierde enroque) y agregar validacion en state 
	@Override
	protected Move createSimpleMove(PiecePositioned origen, PiecePositioned destino) {
		return this.moveFactory.createSimpleKingMoveBlanco(origen, destino);
	}

	@Override
	protected Move createCaptureMove(PiecePositioned origen, PiecePositioned destino) {
		return this.moveFactory.createCaptureKingMoveBlanco(origen, destino);
	}	

}
