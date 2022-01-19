/**
 * 
 */
package chess.moves;

/**
 * @author Mauricio Coria
 *
 */

//TODO: implement bridge pattern.
public interface MoveCastling extends MoveKing {
	Move getRookMove();
}
