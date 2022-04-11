/**
 * 
 */
package chess.board.perft;

import chess.board.Game;
import chess.board.builder.imp.GameBuilder;
import chess.board.debug.builder.DebugChessFactory;
import chess.board.fen.FENDecoder;

/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractPerftTest {

	protected Game getGame(String string) {		
		GameBuilder builder = new GameBuilder(new DebugChessFactory());
		//GameBuilder builder = new GameBuilder();

		FENDecoder parser = new FENDecoder(builder);
		
		parser.parseFEN(string);
		
		return builder.getResult();
	}	
}
