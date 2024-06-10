package net.chesstango.board.moves.generators.pseudo.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.moves.imp.MoveImp;

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
	public MovePair generateCastlingPseudoMoves() {
		MovePair moveContainer = new MovePair();
		if (this.positionState.isCastlingBlackQueenAllowed()){
			if(puedeEnroqueQueen(	kingSquare.getKingSquareBlack(),
								PiecePositioned.KING_BLACK, 
								PiecePositioned.ROOK_BLACK_QUEEN,
								INTERMEDIO_ROOK_QUEEN_SQUARE, 
								DESTINO_QUEEN_SQUARE, 
								INTERMEDIO_KING_QUEEN_SQUARE)) {
				moveContainer.setFirst(moveFactory.createCastlingQueenMove());
			}
		}
			
			
		if (this.positionState.isCastlingBlackKingAllowed()){
			if(puedeEnroqueKing(	kingSquare.getKingSquareBlack(),
								PiecePositioned.KING_BLACK, 
								PiecePositioned.ROOK_BLACK_KING,
								DESTINO_KING_SQUARE, 
								INTERMEDIO_KING_KING_SQUARE)) {
				moveContainer.setSecond(moveFactory.createCastlingKingMove());
			}
		}
		return moveContainer;
	}	

	@Override
	protected MoveImp createSimpleMove(PiecePositioned from, PiecePositioned to) {
		return this.moveFactory.createSimpleKingMove(from, to);
	}

	@Override
	protected MoveImp createCaptureMove(PiecePositioned from, PiecePositioned to) {
		return this.moveFactory.createCaptureKingMove(from, to);
	}	

}
