package chess.pseudomovesfilters;

import java.util.Collection;

import chess.moves.Move;

// Doble  Jaque 										-> Mover el King. 								El enroque no está permitido.
// Simple Jaque (Torre; Alfil; Reina; a mas de un paso) -> Comer jaqueador, tapar jaqueador, mover rey. El enroque no está permitido.
// Simple Jaque (Caballo; Peon; a UN SOLO paso) 		-> Comer jaqueador, mover rey. 					El enroque no está permitido.
// Sin Jaque    										-> El enroque está permidito.
// Movemos la validacion de enroques aqui?

/**
 * @author Mauricio Coria
 *
 */
public interface LegalMoveCalculator {
	Collection<Move> getLegalMoves();
}
