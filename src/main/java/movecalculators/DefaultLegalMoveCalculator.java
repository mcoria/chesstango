package movecalculators;

import java.util.Collection;

import chess.BoardAnalyzer;
import chess.BoardState;
import chess.Move;
import chess.Square;
import iterators.SquareIterator;
import layers.ColorBoard;
import layers.DummyBoard;
import layers.MoveCacheBoard;
import movegenerators.MoveGeneratorStrategy;
import positioncaptures.ImprovedCapturer;

public class DefaultLegalMoveCalculator extends AbstractLegalMoveCalculator {
	
	
	public DefaultLegalMoveCalculator(DummyBoard dummyBoard, ColorBoard colorBoard, MoveCacheBoard moveCache, BoardState boardState,
			MoveGeneratorStrategy strategy, BoardAnalyzer analyzer) {
		super(dummyBoard, colorBoard, moveCache, boardState, strategy, analyzer);
		this.capturer = new ImprovedCapturer(dummyBoard);
	}	

	@Override
	public Collection<Move> getLegalMovesNotKing() {
		turnoActual = boardState.getTurnoActual();
		opositeTurnoActual = turnoActual.opositeColor();

		Collection<Move> moves = createContainer();
		

		for (SquareIterator iterator = colorBoard.iteratorSquareWhitoutKing(turnoActual); iterator.hasNext();) {
			
			Square origenSquare = iterator.next();
			
			Collection<Move> pseudoMoves = getPseudoMoves(origenSquare);			

			// De almacenar movimientos en un cache, estos moviemientos son pseudo, es imposible almacenar movimientos legales en un cache !!!
			// Ejemplo supongamos que almacenamos movimientos de torre blanca en a5, rey blanco se encuentra en e1 y es turno blancas.
			// En movimiento anterior Reina Negra se movió desde h7 a e7 y ahora el rey blanco e1 queda en jaque.
			// Solo movimiento de torre a5 e5 es VALIDO, el resto deja al rey en Jaque
			// Esto quiere decir que una vez obtenidos todos los movimientos pseudo debemos filtrarlos SI o SI
			for (Move move : pseudoMoves) {
				if(filterMove(move)){
					moves.add(move);
				}
			}
			
			//boardCache.validarCacheSqueare(dummyBoard);
		}
		
		return moves;
	}	
}
