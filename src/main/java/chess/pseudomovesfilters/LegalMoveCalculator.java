package chess.pseudomovesfilters;

import java.util.Collection;

import chess.moves.Move;

// Doble  Jaque 										-> Mover el King. 								El enroque no est� permitido.
// Simple Jaque (Torre; Alfil; Queen; a mas de un paso) -> Comer jaqueador, tapar jaqueador, mover king. El enroque no est� permitido.
// Simple Jaque (Caballo; Pawn; a UN SOLO paso) 		-> Comer jaqueador, mover king. 					El enroque no est� permitido.
// Sin Jaque    										-> El enroque est� permidito.
// Movemos la validacion de enroques aqui?

/**
 * @author Mauricio Coria
 *
 */
public interface LegalMoveCalculator {
	Collection<Move> getLegalMoves();
}
