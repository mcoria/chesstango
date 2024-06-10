package net.chesstango;

import net.chesstango.board.moves.generators.pseudo.imp.MoveGeneratorEnPassantImpTest;
import net.chesstango.board.moves.generators.pseudo.strategies.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 *
 */
@Suite
@SelectClasses({ AbstractCardinalMoveGeneratorEsteTest.class, AbstractCardinalMoveGeneratorNorteEsteTest.class,
		AbstractCardinalMoveGeneratorNorteOesteTest.class, AbstractCardinalMoveGeneratorNorteTest.class,
		AbstractCardinalMoveGeneratorOesteTest.class, AbstractCardinalMoveGeneratorSurEsteTest.class,
		AbstractCardinalMoveGeneratorSurOesteTest.class, AbstractCardinalMoveGeneratorSurTest.class,
		BishopMoveGeneratorTest.class, KingBlackMoveGeneratorTest.class, KingWhiteMoveGeneratorTest.class,
		KnightMoveGeneratorTest.class, PawnBlackMoveGeneratorTest.class, PawnWhiteMoveGeneratorTest.class,
		QueenMoveGeneratorTest.class, RookMoveGeneratorTest.class, MoveGeneratorEnPassantImpTest.class })
public class PseudoMoveGeneratorTestSuite {
}
