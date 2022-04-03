/**
 * 
 */
package chess.board.pseudomovesgenerators;

import java.util.Collection;

import chess.board.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveGeneratorEnPassant {

	Collection<Move> generateEnPassantPseudoMoves();

}