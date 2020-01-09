package chess;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

import chess.Move.MoveType;
import parsers.FENParser;

public class BoardTest {

	@Test
	public void test() {
		Set<Move> movimeintosPosibles = null;
		Board board = FENParser.parseFEN(FENParser.INITIAL_FEN);
		
		movimeintosPosibles = board.getMovimientosPosibles();
		assertEquals(20, movimeintosPosibles.size());
		board.move(new Move(Square.e2, Square.e4, MoveType.SIMPLE));
		
		
		movimeintosPosibles = board.getMovimientosPosibles();
		assertEquals(20, movimeintosPosibles.size());
	}

}
