/**
 * 
 */
package net.chesstango.board.perft;

import net.chesstango.board.Game;
import net.chesstango.board.builders.GameBuilder;
import net.chesstango.board.representations.fen.FENDecoder;

/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractPerftTest {

	protected Game getGame(String string) {		
		//GameBuilder builder = new GameBuilder(new ChessFactoryDebug());
		GameBuilder builder = new GameBuilder();

		FENDecoder parser = new FENDecoder(builder);
		
		parser.parseFEN(string);
		
		return builder.getChessRepresentation();
	}	
}
