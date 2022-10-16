/**
 * 
 */
package net.chesstango;

import net.chesstango.board.movesgenerators.pseudo.imp.MoveGeneratorEnPassantImpTest;
import net.chesstango.board.movesgenerators.pseudo.strategies.*;
import net.chesstango.board.movesgenerators.pseudo.strategies.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ AbstractCardinalMoveGeneratorEsteTest.class, AbstractCardinalMoveGeneratorNorteEsteTest.class,
		AbstractCardinalMoveGeneratorNorteOesteTest.class, AbstractCardinalMoveGeneratorNorteTest.class,
		AbstractCardinalMoveGeneratorOesteTest.class, AbstractCardinalMoveGeneratorSurEsteTest.class,
		AbstractCardinalMoveGeneratorSurOesteTest.class, AbstractCardinalMoveGeneratorSurTest.class,
		BishopMoveGeneratorTest.class, KingBlackMoveGeneratorTest.class, KingWhiteMoveGeneratorTest.class,
		KnightMoveGeneratorTest.class, PawnBlackMoveGeneratorTest.class, PawnWhiteMoveGeneratorTest.class,
		QueenMoveGeneratorTest.class, RookMoveGeneratorTest.class, MoveGeneratorEnPassantImpTest.class })
public class PseudoMoveGeneratorTestSuite {
}
