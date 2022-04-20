package chess.board.legalmovesgenerators.strategies;

import java.util.Collection;

import chess.board.Color;
import chess.board.Square;
import chess.board.analyzer.AnalyzerResult;
import chess.board.iterators.square.SquareIterator;
import chess.board.legalmovesgenerators.MoveFilter;
import chess.board.moves.Move;
import chess.board.moves.MoveContainer;
import chess.board.position.ChessPositionReader;
import chess.board.pseudomovesgenerators.MoveGenerator;

//TODO: deberiamos contabilizar aquellas piezas que se exploraron en busca de movimientos validos y no producieron resultados validos.
//      de esta forma cuando se busca en getLegalMovesNotKing() no volver a filtrar los mismos movimientos

/**
 * @author Mauricio Coria
 *
 */
public class NoCheckLegalMoveGenerator extends AbstractLegalMoveGenerator {

	private static final int CAPACITY_MOVE_CONTAINER = 70;

	public NoCheckLegalMoveGenerator(ChessPositionReader positionReader, MoveGenerator strategy, MoveFilter filter) {
		super(positionReader, strategy, filter);
	}

	@Override
	public Collection<Move> getLegalMoves(AnalyzerResult analysis) {
		Collection<Move> moves = new MoveContainer(CAPACITY_MOVE_CONTAINER);
		
		getLegalMovesNotKing(analysis.getPinnedSquares(), moves);
		
		getLegalMovesKing(moves);
		
		getEnPassantMoves(moves);
		
		getCastlingMoves(moves);
		
		return moves;
	}

	protected Collection<Move> getLegalMovesNotKing(long pinnedSquares, Collection<Move> moves) {
		final Color turnoActual = this.positionReader.getTurnoActual();

		for (SquareIterator iterator = this.positionReader.iteratorSquareWhitoutKing(turnoActual); iterator.hasNext();) {

			Square origenSquare = iterator.next();

			Collection<Move> pseudoMoves = getPseudoMoves(origenSquare);

			if ( (pinnedSquares & origenSquare.getPosicion()) != 0 ) {

				filterMoveCollection(pseudoMoves, moves);

			} else {
				
				//moves.addAll(pseudoMoves);
				
				pseudoMoves.forEach(move -> moves.add(move));
				
			}

		}

		return moves;
	}	
	
	protected Collection<Move> getLegalMovesKing(Collection<Move> moves) {		
		Square 	kingSquare = getCurrentKingSquare();
		
		Collection<Move> pseudoMovesKing = getPseudoMoves(kingSquare);

		filterMoveCollection(pseudoMovesKing, moves);
		
		return moves;
	}
	

}
