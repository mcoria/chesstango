package movecalculators;

import java.util.Collection;

import chess.BoardState;
import chess.Square;
import iterators.SquareIterator;
import layers.ColorBoard;
import layers.KingCacheBoard;
import layers.MoveCacheBoard;
import layers.PosicionPiezaBoard;
import moveexecutors.Move;
import movegenerators.MoveGeneratorResult;
import movegenerators.MoveGeneratorStrategy;

/**
 * @author Mauricio Coria
 *
 */
public class DefaultLegalMoveCalculator extends AbstractLegalMoveCalculator {
	public static int count = 0;
	
	public DefaultLegalMoveCalculator(PosicionPiezaBoard dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard, MoveCacheBoard moveCache, BoardState boardState,
			MoveGeneratorStrategy strategy, MoveFilter filter) {
		super(dummyBoard, kingCacheBoard, colorBoard, moveCache, boardState, strategy, filter);
	}	

	@Override
	public Collection<Move> getLegalMoves() {
		
		Collection<Move> moves = createContainer();
		
		getLegalMovesBySquare(moves);
		
		getLegalMovesSpecial(moves);
		
		return moves;
	}

	protected Collection<Move> getLegalMovesBySquare(Collection<Move> moves) {
		for (SquareIterator iterator = colorBoard.iteratorSquare(boardState.getTurnoActual()); iterator.hasNext();) {
			
			Square origenSquare = iterator.next();
			
			MoveGeneratorResult generatorResult = getPseudoMovesResult(origenSquare);

			Collection<Move> pseudoMoves = generatorResult.getPseudoMoves();			

			// De almacenar movimientos en un cache, estos moviemientos son pseudo, es imposible almacenar movimientos legales en un cache !!!
			// Ejemplo supongamos que almacenamos movimientos de torre blanca en a5, rey blanco se encuentra en e1 y es turno blancas.
			// En movimiento anterior Reina Negra se movió desde h7 a e7 y ahora el rey blanco e1 queda en jaque.
			// Solo movimiento de torre a5 e5 es VALIDO, el resto deja al rey en Jaque
			// Esto quiere decir que una vez obtenidos todos los movimientos pseudo debemos filtrarlos SI o SI
			for (Move move : pseudoMoves) {
				if(move.filter(filter)){
					moves.add(move);
				}
			}

		}
		
		return moves;
	}
}
