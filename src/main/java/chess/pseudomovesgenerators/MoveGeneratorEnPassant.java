/**
 * 
 */
package chess.pseudomovesgenerators;

import java.util.Collection;

import chess.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveGeneratorEnPassant {

	Collection<Move> generateEnPassantPseudoMoves();

}