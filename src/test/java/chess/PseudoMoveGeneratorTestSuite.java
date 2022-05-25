/**
 * 
 */
package chess;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import chess.board.movesgenerators.pseudo.imp.MoveGeneratorEnPassantImpTest;
import chess.board.movesgenerators.pseudo.strategies.AbstractCardinalMoveGeneratorEsteTest;
import chess.board.movesgenerators.pseudo.strategies.AbstractCardinalMoveGeneratorNorteEsteTest;
import chess.board.movesgenerators.pseudo.strategies.AbstractCardinalMoveGeneratorNorteOesteTest;
import chess.board.movesgenerators.pseudo.strategies.AbstractCardinalMoveGeneratorNorteTest;
import chess.board.movesgenerators.pseudo.strategies.AbstractCardinalMoveGeneratorOesteTest;
import chess.board.movesgenerators.pseudo.strategies.AbstractCardinalMoveGeneratorSurEsteTest;
import chess.board.movesgenerators.pseudo.strategies.AbstractCardinalMoveGeneratorSurOesteTest;
import chess.board.movesgenerators.pseudo.strategies.AbstractCardinalMoveGeneratorSurTest;
import chess.board.movesgenerators.pseudo.strategies.BishopMoveGeneratorTest;
import chess.board.movesgenerators.pseudo.strategies.KingBlackMoveGeneratorTest;
import chess.board.movesgenerators.pseudo.strategies.KingWhiteMoveGeneratorTest;
import chess.board.movesgenerators.pseudo.strategies.KnightMoveGeneratorTest;
import chess.board.movesgenerators.pseudo.strategies.PawnBlackMoveGeneratorTest;
import chess.board.movesgenerators.pseudo.strategies.PawnWhiteMoveGeneratorTest;
import chess.board.movesgenerators.pseudo.strategies.QueenMoveGeneratorTest;
import chess.board.movesgenerators.pseudo.strategies.RookMoveGeneratorTest;

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
