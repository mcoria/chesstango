package chess.pseudomovesfilters;

import java.util.Collection;

import chess.Square;
import chess.iterators.square.SquareIterator;
import chess.moves.Move;
import chess.position.ColorBoard;
import chess.position.KingCacheBoard;
import chess.position.MoveCacheBoard;
import chess.position.PiecePlacement;
import chess.position.PositionState;
import chess.pseudomovesgenerators.MoveGeneratorResult;
import chess.pseudomovesgenerators.MoveGeneratorStrategy;

/**
 * @author Mauricio Coria
 *
 */
public class DefaultLegalMoveCalculator extends AbstractLegalMoveCalculator {
	
	public DefaultLegalMoveCalculator(PiecePlacement dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard, MoveCacheBoard moveCache, PositionState positionState,
			MoveGeneratorStrategy strategy, MoveFilter filter) {
		super(dummyBoard, kingCacheBoard, colorBoard, moveCache, positionState, strategy, filter);
	}	

	@Override
	public Collection<Move> getLegalMoves() {
		
		Collection<Move> moves = createContainer();
		
		getLegalMovesBySquare(moves);
		
		getLegalMovesSpecial(moves);
		
		return moves;
	}

	protected Collection<Move> getLegalMovesBySquare(Collection<Move> moves) {
		for (SquareIterator iterator = colorBoard.iteratorSquare(positionState.getTurnoActual()); iterator.hasNext();) {
			
			Square origenSquare = iterator.next();
			
			MoveGeneratorResult generatorResult = getPseudoMovesResult(origenSquare);

			Collection<Move> pseudoMoves = generatorResult.getPseudoMoves();			

			// De almacenar movimientos en un cache, estos moviemientos son pseudo, es imposible almacenar movimientos legales en un cache !!!
			// Ejemplo supongamos que almacenamos movimientos de torre blanca en a5, king blanco se encuentra en e1 y es turno blancas.
			// En movimiento anterior Queen Negra se movió desde h7 a e7 y ahora el king blanco e1 queda en jaque.
			// Solo movimiento de torre a5 e5 es VALIDO, el resto deja al king en Jaque
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
