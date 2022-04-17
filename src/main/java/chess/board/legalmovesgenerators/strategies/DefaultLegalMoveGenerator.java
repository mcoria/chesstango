package chess.board.legalmovesgenerators.strategies;

import java.util.Collection;

import chess.board.Square;
import chess.board.iterators.square.SquareIterator;
import chess.board.legalmovesgenerators.MoveFilter;
import chess.board.moves.Move;
import chess.board.moves.MoveContainer;
import chess.board.position.ChessPositionReader;
import chess.board.pseudomovesgenerators.MoveGenerator;

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
		
		Collection<Move> moves = new MoveContainer();
		
		getLegalMovesBySquare(moves);
		
		getEnPassantLegalMoves(moves);
		
		getCastlingMoves(moves);
		
		return moves;
	}

	protected Collection<Move> getLegalMovesBySquare(Collection<Move> moves) {
		for (SquareIterator iterator = this.positionReader.iteratorSquare(this.positionReader.getTurnoActual()); iterator.hasNext();) {
			
			Square origenSquare = iterator.next();

			Collection<Move> pseudoMoves = getPseudoMoves(origenSquare);

			// De almacenar movimientos en un cache, estos moviemientos son pseudo, es imposible almacenar movimientos legales en un cache !!!
			// Ejemplo supongamos que almacenamos movimientos de torre blanca en a5, king blanco se encuentra en e1 y es turno blancas.
			// En movimiento anterior Queen Negra se movió desde h7 a e7 y ahora el king blanco e1 queda en jaque.
			// Solo movimiento de torre a5 e5 es VALIDO, el resto deja al king en Jaque
			// Esto quiere decir que una vez obtenidos todos los movimientos pseudo debemos filtrarlos SI o SI
			filerMoveCollection(pseudoMoves, moves);
		}
		
		return moves;
	}
}
