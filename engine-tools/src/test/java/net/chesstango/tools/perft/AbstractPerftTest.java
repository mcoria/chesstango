/**
 * 
 */
package net.chesstango.tools.perft;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.builders.GameBuilder;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.tools.perft.imp.PerftBrute;

import java.util.Map;

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

	protected Perft createPerft(){
		return new PerftBrute();
		//return new PerftWithMapIterateDeeping<Long>(PerftWithMapIterateDeeping::getZobristGameId);
		//return new PerftWithMap<Long>(PerftWithMap::getZobristGameId);
	}

	protected boolean contieneMove(MoveContainerReader movimientos, Square from, Square to) {
		for (Move move : movimientos) {
			if(from.equals(move.getFrom().getSquare()) && to.equals(move.getTo().getSquare())){
				return true;
			}
		}
		return false;
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
