package chess.movegenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import chess.builder.ChessBuilderParts;
import chess.debug.builder.DebugChessFactory;
import chess.layers.ColorBoard;
import chess.layers.PosicionPiezaBoard;
import chess.moveexecutors.Move;
import chess.moveexecutors.MoveFactory;
import chess.movegenerators.CaballoMoveGenerator;
import chess.movegenerators.MoveGeneratorResult;
import chess.parsers.FENParser;

/**
 * @author Mauricio Coria
 *
 */
public class CaballoMoveGeneratorTest {
	
	private CaballoMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 

	private MoveFactory moveFactory;
	
	@Before
	public void setUp() throws Exception {
		moveFactory = new MoveFactory();
		moveGenerator = new CaballoMoveGenerator(Color.BLANCO);
		moveGenerator.setMoveFactory(moveFactory);
		moves = new ArrayList<Move>();
	}
	
	@Test
	public void test() {
		PosicionPiezaBoard tablero =  getTablero("8/3P1p2/8/4N3/8/8/8/8");
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		Square from = Square.e5;
		assertEquals(Pieza.CABALLO_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.d7));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.f7));		
	
		PosicionPieza origen = new PosicionPieza(from, Pieza.CABALLO_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.g6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.g4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f3) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d3) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.c4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.c6) ));
		// Peon Blanco en d7
		assertTrue(moves.contains( createCaptureMove(origen, Square.f7, Pieza.PEON_NEGRO) ));
		
		assertEquals(7, moves.size());
	}

	private Move createSimpleMove(PosicionPieza origen, Square destinoSquare) {
		return moveFactory.createSimpleMove(origen, new PosicionPieza(destinoSquare, null));
	}
	
	private Move createCaptureMove(PosicionPieza origen, Square destinoSquare, Pieza destinoPieza) {
		return moveFactory.createCaptureMove(origen, new PosicionPieza(destinoSquare, destinoPieza));
	}
	
	private PosicionPiezaBoard getTablero(String string) {		
		ChessBuilderParts builder = new ChessBuilderParts(new DebugChessFactory());
		FENParser parser = new FENParser(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getPosicionPiezaBoard();
	}	
	
}
