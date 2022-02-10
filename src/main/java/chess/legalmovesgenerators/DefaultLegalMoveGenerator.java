package chess.legalmovesgenerators;

import java.util.Collection;

import chess.ChessPositionReader;
import chess.Square;
import chess.iterators.square.SquareIterator;
import chess.moves.Move;
import chess.pseudomovesgenerators.MoveGenerator;
import chess.pseudomovesgenerators.MoveGeneratorResult;

/**
 * @author Mauricio Coria
 *
 */
public class DefaultLegalMoveGenerator extends AbstractLegalMoveGenerator {
	
	public DefaultLegalMoveGenerator(ChessPositionReader positionReader, 
			MoveGenerator strategy, MoveFilter filter) {
		super(positionReader, strategy, filter);
	}	

	@Override
	public Collection<Move> getLegalMoves() {
		
		Collection<Move> moves = createContainer();
		
		getLegalMovesBySquare(moves);
		
		getLegalMovesSpecial(moves);
		
		return moves;
	}

	protected Collection<Move> getLegalMovesBySquare(Collection<Move> moves) {
		for (SquareIterator iterator = this.positionReader.iteratorSquare(this.positionReader.getTurnoActual()); iterator.hasNext();) {
			
			Square origenSquare = iterator.next();
			
			MoveGeneratorResult generatorResult = getPseudoMoves(origenSquare);

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
