package net.chesstango.board.moves.generators.pseudo.imp.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.moves.imp.MoveCommand;

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
	public MovePair<MoveCommand> generateCastlingPseudoMoves() {
		MovePair<MoveCommand> moveContainer = new MovePair<>();
		if (this.positionState.isCastlingWhiteQueenAllowed()){
			if(validateCastlingQueen(	kingSquare.getKingSquareWhite(),
								PiecePositioned.KING_WHITE, 
								PiecePositioned.ROOK_WHITE_QUEEN,
								INTERMEDIO_ROOK_QUEEN_SQUARE, 
								DESTINO_QUEEN_SQUARE, 
								INTERMEDIO_KING_QUEEN_SQUARE)) {
				moveContainer.setFirst(moveFactory.createCastlingQueenMove());
			}
		}

		if (this.positionState.isCastlingWhiteKingAllowed() ){
			if(validateCastlingKing(	kingSquare.getKingSquareWhite(),
								PiecePositioned.KING_WHITE, 
								PiecePositioned.ROOK_WHITE_KING,
								DESTINO_KING_SQUARE, 
								INTERMEDIO_KING_KING_SQUARE)) {
				moveContainer.setSecond(moveFactory.createCastlingKingMove());
			}
		}
		return moveContainer;
	}
}
