package chess.board.legalmovesgenerators.strategies;

import java.util.Collection;

import chess.board.Color;
import chess.board.Square;
import chess.board.analyzer.Pinned;
import chess.board.iterators.square.SquareIterator;
import chess.board.legalmovesgenerators.MoveFilter;
import chess.board.moves.Move;
import chess.board.position.ChessPositionReader;
import chess.board.pseudomovesgenerators.MoveGenerator;
import chess.board.pseudomovesgenerators.MoveGeneratorResult;

//TODO: deberiamos contabilizar aquellas piezas que se exploraron en busca de movimientos validos y no producieron resultados validos.
//      de esta forma cuendo se busca en getLegalMovesNotKing() no volver a filtrar los mismos movimientos

/**
 * @author Mauricio Coria
 *
 */
public class NoCheckLegalMoveGenerator extends AbstractLegalMoveGenerator {
	
	private final Pinned pinnedAlanyzer;

	public NoCheckLegalMoveGenerator(ChessPositionReader positionReader, MoveGenerator strategy, MoveFilter filter) {
		super(positionReader, strategy, filter);
		
		pinnedAlanyzer = new Pinned(positionReader);
	}

	@Override
	public Collection<Move> getLegalMoves() {
		Collection<Move> moves = createContainer();
		
		getLegalMovesNotKing(moves);
		
		getLegalMovesKing(moves);
		
		getEnPassantLegalMoves(moves);		
		
		return moves;
	}

	protected Collection<Move> getLegalMovesNotKing(Collection<Move> moves) {
		final Color turnoActual = this.positionReader.getTurnoActual();

		// Casilleros donde se encuentran piezas propias que de moverse pueden dejar en jaque al King.
		//TODO: esto deberia venir precalculado
		long pinnedSquares = pinnedAlanyzer.getPinnedSquare(turnoActual); 

		for (SquareIterator iterator = this.positionReader.iteratorSquareWhitoutKing(turnoActual); iterator.hasNext();) {

			Square origenSquare = iterator.next();
			
			MoveGeneratorResult generatorResult = getPseudoMoves(origenSquare);

			Collection<Move> pseudoMoves = generatorResult.getPseudoMoves();

			if ( (pinnedSquares & origenSquare.getPosicion()) != 0 ) {
				for (Move move : pseudoMoves) {
					if(move.filter(filter)){
						moves.add(move);
					}
				}

			} else {
				moves.addAll(pseudoMoves);
			}

		}

		return moves;
	}	
	
	protected Collection<Move> getLegalMovesKing(Collection<Move> moves) {		
		Square 	kingSquare = getCurrentKingSquare();
		
		MoveGeneratorResult generatorResult = getPseudoMoves(kingSquare);
		
		Collection<Move> pseudoMovesKing = generatorResult.getPseudoMoves();

		for (Move move : pseudoMovesKing) {
			if(move.filter(filter)){
				moves.add(move);
			}
		}
		return moves;
	}
	

}