package net.chesstango.board.position.imp;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.GameBuilder;
import net.chesstango.board.builders.GameBuilderDebug;
import net.chesstango.board.internal.position.MoveCacheBoardImp;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.internal.moves.factories.MoveFactoryWhite;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPieceResult;
import net.chesstango.board.position.MoveCacheBoard;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
/**
 * @author Mauricio Coria
 *
 */
public class MoveCacheBoardImpTest {

	private MoveCacheBoard cache;

	private MoveFactory moveFactoryImp;

	@BeforeEach
	public void setUp() throws Exception {
		moveFactoryImp = new MoveFactoryWhite();
		cache = new MoveCacheBoardImp();
	}

	@Test
	public void test01() {
		MoveGeneratorByPieceResult result = new MoveGeneratorByPieceResult(PiecePositioned.of(Square.a2, Piece.PAWN_WHITE));
		result.addPseudoMove(moveFactoryImp.createSimpleKnightMove(PiecePositioned.of(Square.a2, Piece.PAWN_WHITE), PiecePositioned.of(Square.a3, null)));
		result.addPseudoMove(moveFactoryImp.createSimpleKnightMove(PiecePositioned.of(Square.a2, Piece.PAWN_WHITE), PiecePositioned.of(Square.a4, null)));
		cache.setPseudoMoves(Square.a2, result);

		assertNotNull(cache.getPseudoMovesResult(Square.a2));
	}


	//Test
	public void possibleAffectedPseudoMovesPositions(){
		long posiciones[] = new long[64];
		for (Square square: Square.values()) {
			long possibleAffects = 0;
			for (Cardinal cardinal: Cardinal.values()){
				possibleAffects |= cardinal.getSquaresInDirection(square);
			}
			possibleAffects |= square.getKingJumps();
			possibleAffects |= square.getKnightJumps();
			posiciones[square.toIdx()] = possibleAffects;
		}

		for (long posicion : posiciones) {
			System.out.printf("0x%sL,%n", Long.toHexString(posicion));
		}
	}

	private Game getGame(String string) {
		GameBuilder builder = new GameBuilderDebug();

		FENDecoder parser = new FENDecoder(builder);

		parser.parseFEN(string);

		return builder.getChessRepresentation();
	}

}
