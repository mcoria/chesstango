package chess.board.legalmovesgenerators;

import java.util.Collection;

import chess.board.analyzer.AnalyzerResult;
import chess.board.moves.Move;

// Doble  Jaque 										-> Mover el King. 								El castling no está permitido.
// Simple Jaque (Rook; Bishop; Queen; a mas de un paso) -> Comer jaqueador, tapar jaqueador, mover king. El castling no está permitido.
// Simple Jaque (Knight; Pawn; a UN SOLO paso) 			-> Comer jaqueador, mover king. 					El castling no está permitido.
// Sin Jaque    										-> El castling está permidito.
// Movemos la validacion de castlings aqui?

/**
 * @author Mauricio Coria
 *
 */
public interface LegalMoveGenerator {
	//Collection<Move> getLegalMoves(AnalyzerResult); TODO: deberia haber un facade que seleccione el algoritmo adecuado
	
	// De almacenar movimientos en un cache, estos moviemientos son pseudo, es imposible almacenar movimientos legales en un cache !!!
	// Ejemplo supongamos que almacenamos movimientos de torre blanca en a5, king blanco se encuentra en e1 y es turno blancas.
	// En movimiento anterior Queen Negra se movió desde h7 a e7 y ahora el king blanco e1 queda en jaque.
	// Solo movimiento de torre a5 e5 es VALIDO, el resto deja al king en Jaque
	// Esto quiere decir que una vez obtenidos todos los movimientos pseudo debemos filtrarlos SI o SI	
	Collection<Move> getLegalMoves(AnalyzerResult analysis);
}
