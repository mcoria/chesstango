/**
 * 
 */
package net.chesstango.board.perft;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.builders.GameBuilder;
import net.chesstango.board.debug.builder.ChessFactoryDebug;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;

import java.util.Map;

import static org.junit.Assert.assertEquals;

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

	protected void printForUnitTest(PerftResult result) {

		Map<Move, Long> childs = result.getChilds();

		childs.forEach((move, count) ->{
			System.out.printf("assertEquals(%d, result.getChildNode(Square.%s, Square.%s));\n", count, move.getFrom().getSquare(), move.getTo().getSquare());
		});

		System.out.printf("assertEquals(%d, result.getMovesCount());\n", childs.size());
		System.out.printf("assertEquals(%d, result.getTotalNodes());\n", result.getTotalNodes());
	}
}
