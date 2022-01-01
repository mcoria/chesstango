/**
 * 
 */
package movegenerators;

import java.util.Collection;

import moveexecutors.Move;

/**
 * @author Mauricio Coria
 *
 */
public interface SpecialMoveGenerator {
	Collection<Move> getPseudoMoves();
}
