package net.chesstango.board.movesgenerators.legal;

import net.chesstango.board.analyzer.AnalyzerResult;
import net.chesstango.board.moves.MoveContainerReader;

// Doble  Jaque 										-> Mover el King. 								El castling no est� permitido.
// Simple Jaque (Rook; Bishop; Queen; a mas de un paso) -> Comer jaqueador, tapar jaqueador, mover king. El castling no est� permitido.
// Simple Jaque (CapturerByKnight; Pawn; a UN SOLO paso) 			-> Comer jaqueador, mover king. 					El castling no est� permitido.
// Sin Jaque    										-> El castling esta permidito.
// Movemos la validacion de castlings aqui?

/**
 * @author Mauricio Coria
 *
 */
public interface LegalMoveGenerator {
	//Collection<Move> getLegalMoves(AnalyzerResult); TODO: deberia haber un facade que seleccione el algoritmo adecuado
	
	// De almacenar movimientos en un cache, estos moviemientos son pseudo, es imposible almacenar movimientos legales en un cache !!!
	// Ejemplo supongamos que almacenamos movimientos de torre blanca en a5, king blanco se encuentra en e1 y es turno blancas.
	// En movimiento anterior Queen Negra se movi� desde h7 a e7 y ahora el king blanco e1 queda en jaque.
	// Solo movimiento de torre a5 e5 es VALIDO, el resto deja al king en Jaque
	// Esto quiere decir que una vez obtenidos todos los movimientos pseudo debemos filtrarlos SI o SI	
	MoveContainerReader getLegalMoves(AnalyzerResult analysis);
}
