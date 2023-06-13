package net.chesstango.board.movesgenerators.pseudo.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MovePair;


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
	public MovePair generateCastlingPseudoMoves() {
		MovePair moveContainer = new MovePair();
		if (this.positionState.isCastlingWhiteQueenAllowed()){
			if(puedeEnroqueQueen(	kingSquare.getKingSquareWhite(),
								PiecePositioned.KING_WHITE, 
								PiecePositioned.ROOK_WHITE_QUEEN,
								INTERMEDIO_ROOK_QUEEN_SQUARE, 
								DESTINO_QUEEN_SQUARE, 
								INTERMEDIO_KING_QUEEN_SQUARE)) {
				moveContainer.setFirst(moveFactory.createCastlingQueenMove());
			}
		}
		
		
		if (this.positionState.isCastlingWhiteKingAllowed() ){
			if(puedeEnroqueKing(	kingSquare.getKingSquareWhite(),
								PiecePositioned.KING_WHITE, 
								PiecePositioned.ROOK_WHITE_KING,
								DESTINO_KING_SQUARE, 
								INTERMEDIO_KING_KING_SQUARE)) {
				moveContainer.setSecond(moveFactory.createCastlingKingMove());
			}
		}
		return moveContainer;
	}		

	@Override
	protected Move createSimpleMove(PiecePositioned origen, PiecePositioned destino) {
		return this.moveFactory.createSimpleKingMove(origen, destino);
	}

	@Override
	protected Move createCaptureMove(PiecePositioned origen, PiecePositioned destino) {
		return this.moveFactory.createCaptureKingMove(origen, destino);
	}


}
