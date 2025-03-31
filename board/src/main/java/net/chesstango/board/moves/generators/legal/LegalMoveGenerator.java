package net.chesstango.board.moves.generators.legal;

import net.chesstango.board.analyzer.AnalyzerResult;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.moves.PseudoMove;

// Doble  Jaque 										-> Mover el King. 								 El castling no esta permitido.
// Simple Jaque (Rook; Bishop; Queen; a mas de un paso) -> Comer jaqueador, tapar jaqueador, mover king. El castling no esta permitido.
// Simple Jaque (CapturerByKnight; Pawn; a UN SOLO paso) 			-> Comer jaqueador, mover king. 	 El castling no esta permitido.
// Sin Jaque    										-> El castling esta permidito. Solo considerar posiciones pinned.
// Movemos la validacion de castlings aqui?

/**
 * Interface representing a generator for legal chess moves.
 * This interface provides a method to get legal moves based on the analysis result.
 * The legal moves are filtered from pseudo moves to ensure they comply with the rules of chess.
 *
 * The method getLegalMoves takes an AnalyzerResult as a parameter and returns a MoveContainerReader
 * containing PseudoMove objects that are legal according to the analysis.
 *
 * @see AnalyzerResult
 * @see MoveContainerReader
 * @see PseudoMove
 *
 * @author Mauricio Coria
 */
public interface LegalMoveGenerator {
	
	// De almacenar movimientos en un cache, estos moviemientos son pseudo, es imposible almacenar movimientos legales en un cache !!!
	// Ejemplo supongamos que almacenamos movimientos de torre blanca en a5, king blanco se encuentra en e1 y es turno blancas.
	// En movimiento anterior Queen Negra se movi� desde h7 a e7 y ahora el king blanco e1 queda en jaque.
	// Solo movimiento de torre a5 e5 es VALIDO, el resto deja al king en Jaque
	// Esto quiere decir que una vez obtenidos todos los movimientos pseudo debemos filtrarlos SI o SI
	/**
	 * Gets the legal moves based on the provided analysis result.
	 * The legal moves are filtered from pseudo moves to ensure they comply with the rules of chess.
	 *
	 * @param analysis the analysis result used to determine the legal moves
	 * @return a MoveContainerReader containing the legal pseudo moves
	 */
	MoveContainerReader<Move> getLegalMoves(AnalyzerResult analysis);
}
